package nussi.net.pduControl.pdu.products;

import nussi.net.pduControl.pdu.OutletControlAction;
import nussi.net.pduControl.pdu.OutletStatus;
import nussi.net.pduControl.pdu.PowerDistributionUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Avocent3009h extends PowerDistributionUnit {

    public static void main(String[] args) throws IOException {
        Avocent3009h pdu1 = new Avocent3009h("192.168.0.119", 161, "public_read");
        Avocent3009h pdu2 = new Avocent3009h("192.168.0.120", 161, "public_read");


        System.out.println(pdu1.getOutletStatus(pdu1.validOutletIDS()).toString());


//        pdu1.setOutletOffDelay(outletIDs, 1);
//        pdu1.setOutletOnDelay(outletIDs, 1);
//
//        pdu2.setOutletOffDelay(outletIDs, 1);
//        pdu2.setOutletOnDelay(outletIDs, 1);
//
//        pdu1.doOutletAction(outletIDs, OutletControlAction.powerOff);
//        pdu2.doOutletAction(outletIDs, OutletControlAction.powerOff);
//        pdu1.doOutletAction(outletIDs, OutletControlAction.powerOn);
//        pdu2.doOutletAction(outletIDs, OutletControlAction.powerOn);




    }

    // DEFINITIONS
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String LoggerPreFix = "Avocent3009h["+this.HOSTNAME+"] ";


    @Override
    public int[] validOutletIDS() {
        return new int[]{1,2,3,4,5,6,7,8,9,10};
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
    public void doOutletAction(int outletID, OutletControlAction action) {
        try {
            OID oid = getOutletControlOID(outletID);
            Integer32 actionID = action.getActionInt32();
            setPackage(new VariableBinding(oid, actionID));
            logger.info(LoggerPreFix+"OutletAction --> " + action + " --> " + outletID);
        } catch (IOException e) {
        }
    }

    @Override
    public void doOutletAction(int[] outletID, OutletControlAction action) {
        try {
            ArrayList<VariableBinding> bindings = new ArrayList<>();
            for(int i : outletID) {
                bindings.add(new VariableBinding(getOutletControlOID(i), action.getActionInt32()));
            }

            setPackage(bindings);
            logger.info(LoggerPreFix+"OutletAction --> " + action + " --> " + Arrays.toString(outletID));
        } catch (IOException e) {
        }
    }

    // OUTLET STATUS
    String OutletStatusBaseOID = ".1.3.6.1.4.1.10418.17.2.5.5.1.5.1.1.";

    private OID getOutletStatusOID(int outletID) {
        return new OID(OutletStatusBaseOID + outletID);
    }

    @Override
    public OutletStatus getOutletStatus(int outletID) {
        OID oid = getOutletStatusOID(outletID);
        OutletStatus status;
        try {
            PDU data = getPackage(new VariableBinding(oid));
            if(data == null) return OutletStatus.notSet;
            status = OutletStatus.fromInt(data.getVariable(oid).toInt());
            logger.info(LoggerPreFix+"OutletStatus  --> " + outletID + " --> " + status);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get outletStatus!");
        }
        return status;
    }

    @Override
    public HashMap<Integer, OutletStatus> getOutletStatus(int[] outletID) {
        HashMap<Integer, OutletStatus> ret = new HashMap<>();
        HashMap<Integer,OID> oids = new HashMap<>();
        ArrayList<VariableBinding> bindings = new ArrayList<>();
        for(int id : outletID) {
            OID oid = getOutletStatusOID(id);
            oids.put(id,oid);
            bindings.add(new VariableBinding(oid));
        }

        try {
            PDU data = getPackage(bindings);
            if(data == null) {
                for(Integer pair : oids.keySet()) {
                    ret.put(pair, OutletStatus.notSet);
                }
                return ret;
            }

            for(Map.Entry<Integer, OID> pair : oids.entrySet()) {
                int statusVal = data.getVariable(pair.getValue()).toInt();
                ret.put(pair.getKey(), OutletStatus.fromInt(statusVal));
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to get outletStatus!");
        }

        return ret;
    }

    // OUTLET ON Delay
    String OutletOnDelayBaseOID = ".1.3.6.1.4.1.10418.17.2.5.5.1.9.1.1.";

    private OID getOutletOnDelayOID(int outletID) {
        return new OID(OutletOnDelayBaseOID + outletID);
    }

    @Override
    public void setOutletOnDelay(int outletID, int seconds) {
        OID oid = getOutletOnDelayOID(outletID);
        try {
            setPackage(new VariableBinding(oid, new Integer32(seconds)));
            logger.info(LoggerPreFix+"OutletOnDelay --> " + seconds + " --> " + outletID);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get outletOnDelay!");
        }
    }

    @Override
    public void setOutletOnDelay(int[] outletID, int seconds) {
        try {
            ArrayList<VariableBinding> bindings = new ArrayList<>();
            for(int i : outletID) {
                bindings.add(new VariableBinding(
                        getOutletOnDelayOID(i),
                        new Integer32(seconds)
                ));
            }

            setPackage(bindings);
            logger.info(LoggerPreFix+"OutletOnDelay --> " + seconds + " --> " + Arrays.toString(outletID));
        } catch (IOException e) {
            throw new RuntimeException("Failed to get outletOnDelay!");
        }
    }

    @Override
    public int getOutletOnDelay(int outletID) {
        OID oid = getOutletOnDelayOID(outletID);
        int status;
        try {
            PDU data = getPackage(new VariableBinding(oid));
            status = data.getVariable(oid).toInt();
            logger.info(LoggerPreFix+"OutletOnDelay --> " + outletID + " --> " + data);
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

    @Override
    public void setOutletOffDelay(int outletID, int seconds) {
        OID oid = getOutletOffDelayOID(outletID);
        try {
            setPackage(new VariableBinding(oid, new Integer32(seconds)));
            logger.info(LoggerPreFix+"OutletOffDelay --> " + seconds + " --> " + outletID);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get outletOnDelay!");
        }
    }

    @Override
    public void setOutletOffDelay(int[] outletID, int seconds) {
        try {
            ArrayList<VariableBinding> bindings = new ArrayList<>();
            for(int i : outletID) {
                bindings.add(new VariableBinding(
                        getOutletOffDelayOID(i),
                        new Integer32(seconds)
                ));
            }

            setPackage(bindings);
            logger.info(LoggerPreFix+"OutletOffDelay --> " + seconds + " --> " + Arrays.toString(outletID));
        } catch (IOException e) {
            throw new RuntimeException("Failed to get outletOnDelay!");
        }
    }

    @Override
    public int getOutletOffDelay(int outletID) {
        OID oid = getOutletOffDelayOID(outletID);
        int status;
        try {
            PDU data = getPackage(new VariableBinding(oid));
            status = data.getVariable(oid).toInt();
            logger.info(LoggerPreFix+"OutletOffDelay --> " + outletID + " --> " + data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get outletOnDelay!");
        }
        return status;
    }


}
