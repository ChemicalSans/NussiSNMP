package net.nussi.snmp.pdu;

import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import java.io.IOException;
import java.util.ArrayList;

public class Avocent3009h extends PowerDistributionUnit {

    public static void main(String[] args) throws IOException, InterruptedException {
        Avocent3009h pdu = new Avocent3009h("192.168.0.119", 161, "public_read");
//        int[] outletIDs = new int[]{1};
        int[] outletIDs = new int[]{1,2,3,4,5,6,7,8,9,10};

//        for(int i: outletIDs) {
//            pdu.outletOnDelay(i, 0);
//            pdu.outletOffDelay(i, 0);
//        }

        pdu.outletOffDelay(outletIDs, 0);
        pdu.outletOnDelay(outletIDs, 0);

        pdu.outletControl(outletIDs, OutletControlAction.powerOff);
        pdu.outletControl(outletIDs, OutletControlAction.powerOn);

        pdu.outletOffDelay(outletIDs, 1);
        pdu.outletOnDelay(outletIDs, 1);

        pdu.outletControl(outletIDs, OutletControlAction.powerOff);
        pdu.outletControl(outletIDs, OutletControlAction.powerOn);

//        for(int i: outletIDs) {
//            pdu.outletControl(i, OutletControlAction.powerOff);
//            System.out.println("Outlet " + i + " --> " + pdu.outletStatus(i));
//            Thread.sleep(100);
//        }
//
//        for(int i: outletIDs) {
//
//            pdu.outletControl(i, OutletControlAction.powerOn);
//            System.out.println("Outlet " + i + " --> " + pdu.outletStatus(i));
//            Thread.sleep(100);
//        }


//        System.out.println(pdu.outletOnDelay(1, 2));


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

    @Override
    public boolean outletControl(int[] outletID, OutletControlAction action) {
        try {
            ArrayList<VariableBinding> bindings = new ArrayList<>();
            for(int i : outletID) {
                bindings.add(new VariableBinding(getOutletControlOID(i), action.getActionInt32()));
            }

            setPackage(bindings);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    // OUTLET STATUS
    String OutletStatusBaseOID = ".1.3.6.1.4.1.10418.17.2.5.5.1.5.1.1.";

    private OID getOutletStatusOID(int outletID) {
        return new OID(OutletStatusBaseOID + outletID);
    }

    @Override
    public OutletStatus outletStatus(int outletID) {
        OID oid = getOutletStatusOID(outletID);
        int status;
        try {
            PDU data = getPackage(new VariableBinding(oid));
            if(data == null) return OutletStatus.notSet;
            status = data.getVariable(oid).toInt();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get outletStatus!");
        }
        return OutletStatus.fromInt(status);
    }

    // OUTLET ON Delay
    String OutletOnDelayBaseOID = ".1.3.6.1.4.1.10418.17.2.5.5.1.9.1.1.";

    private OID getOutletOnDelayOID(int outletID) {
        return new OID(OutletOnDelayBaseOID + outletID);
    }

    public boolean outletOnDelay(int outletID, int seconds) {
        OID oid = getOutletOnDelayOID(outletID);
        try {
            PDU data = setPackage(new VariableBinding(oid, new Integer32(seconds)));
            data.getVariable(oid).toInt();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get outletOnDelay!");
        }
        return true;
    }

    public boolean outletOnDelay(int[] outletID, int seconds) {
        try {
            ArrayList<VariableBinding> bindings = new ArrayList<>();
            for(int i : outletID) {
                bindings.add(new VariableBinding(
                        getOutletOnDelayOID(i),
                        new Integer32(seconds)
                ));
            }

            PDU data = setPackage(bindings);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get outletOnDelay!");
        }
        return true;
    }

    public int outletOnDelay(int outletID) {
        OID oid = getOutletOnDelayOID(outletID);
        int status;
        try {
            PDU data = getPackage(new VariableBinding(oid));
            status = data.getVariable(oid).toInt();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get outletOnDelay!");
        }
        return status;
    }


    // OUTLET OFF Delay
    String OutletOffDelayBaseOID = ".1.3.6.1.4.1.10418.17.2.5.5.1.10.1.1.";

    private OID getOutletOffDelayOID(int outletID) {
        return new OID(OutletOffDelayBaseOID + outletID);
    }

    public boolean outletOffDelay(int outletID, int seconds) {
        OID oid = getOutletOffDelayOID(outletID);
        int status;
        try {
            PDU data = setPackage(new VariableBinding(oid, new Integer32(seconds)));
            status = data.getVariable(oid).toInt();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get outletOnDelay!");
        }
        return true;
    }

    public boolean outletOffDelay(int[] outletID, int seconds) {
        try {
            ArrayList<VariableBinding> bindings = new ArrayList<>();
            for(int i : outletID) {
                bindings.add(new VariableBinding(
                        getOutletOffDelayOID(i),
                        new Integer32(seconds)
                ));
            }

            setPackage(bindings);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get outletOnDelay!");
        }
        return true;
    }

    public int outletOffDelay(int outletID) {
        OID oid = getOutletOffDelayOID(outletID);
        int status;
        try {
            PDU data = getPackage(new VariableBinding(oid));
            status = data.getVariable(oid).toInt();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get outletOnDelay!");
        }
        return status;
    }


}
