package net.nussi.snmp.pdu;

import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import java.io.IOException;

public class Avocent3009h extends PowerDistributionUnit {

    public static void main(String[] args) throws IOException {
        PowerDistributionUnit pdu = new Avocent3009h("192.168.0.119", 161, "public_read");
        pdu.outletControl(1,OutletControlAction.powerOn);
    }

    public Avocent3009h(String HOSTNAME, int PORT, String COMMUNITY) throws IOException {
        super(HOSTNAME, PORT, COMMUNITY);
    }

    // OUTLET CONTROL
    String OutletControlBaseOID = ".1.3.6.1.4.1.10418.17.2.5.5.1.6.1.1.";

    private OID getOutletControlOID(int outletID) {
        return new OID(OutletControlBaseOID + outletID);
    }

    @Override
    public boolean outletControl(int outletID, OutletControlAction action) {
        try {
            OID oid = getOutletControlOID(outletID);
            Integer32 actionID = action.getActionInt32();
            setPackage(new VariableBinding(oid, actionID));
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    // OUTLET STATUS
    String OutletStatusBaseOID = ".1.3.6.1.4.1.10418.17.2.5.5.1.6.1.1.";

    private OID getOutletStatusOID(int outletID) {
        return new OID(OutletStatusBaseOID + outletID);
    }


    @Override
    public OutletStatus outletStatus(int outletID) {
        OID oid = getOutletStatusOID(outletID);
        try {
            PDU data = getPackage(new VariableBinding(oid));
            Integer32 da = (Integer32) data.getVariable(oid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
