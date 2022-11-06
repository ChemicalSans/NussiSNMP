package nussi.net.pduControl.pdu;

import nussi.net.pduControl.pdu.products.Avocent3009h;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PowerDistributionUnitManager implements PowerDistributionUnitInterface {
    private final ArrayList<Integer> OutletIDs = new ArrayList<>();
    private final HashMap<Integer, PowerDistributionUnit> idToPdu = new HashMap<>();
    private final HashMap<Integer, Integer> idToID = new HashMap<>();

    private final ArrayList<PowerDistributionUnit> PowerDistributionUnits;

    public PowerDistributionUnitManager(ArrayList<PowerDistributionUnit> arrayList) {
        this.PowerDistributionUnits = arrayList;

        int idCounter = 1;
        for(PowerDistributionUnit pdu : PowerDistributionUnits) {
            for(int id : pdu.validOutletIDS()) {
                OutletIDs.add(idCounter);
                idToPdu.put(idCounter, pdu);
                idToID.put(idCounter, id);
                idCounter++;
            }
        }

    }


    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList<PowerDistributionUnit> pdus = new ArrayList<>();
        pdus.add(new Avocent3009h("192.168.0.119", 161, "public_read"));
        pdus.add(new Avocent3009h("192.168.0.120", 161, "public_read"));
//        pdus.add(new BlankPDU(new int[]{1,2,3,4,5,6,7,8,9,10}));

        PowerDistributionUnitManager manager = new PowerDistributionUnitManager(pdus);

        System.out.println(manager.getOutletStatus(manager.validOutletIDS()).toString());

//        manager.setAllOutletOnDelay(0);
//        manager.setAllOutletOffDelay(0);
//        manager.doAllOutletAction(OutletControlAction.powerOff);
//        manager.doAllOutletAction(OutletControlAction.powerOn);
//
//        manager.setAllOutletOnDelay(1);
//        manager.setAllOutletOffDelay(1);
//        manager.doAllOutletAction(OutletControlAction.powerOff);
//        manager.doAllOutletAction(OutletControlAction.powerOn);
//
//
//        manager.doAllOutletAction(OutletControlAction.powerOff);
//        for(int id : manager.validOutletIDS()) {
//            manager.doOutletAction(id, OutletControlAction.powerOn);
//            Thread.sleep(100);
//            manager.doOutletAction(id, OutletControlAction.powerOff);
//        }
//
//
//        manager.doOutletAction(new int[]{1,3,5,7,9,11,13,15,17,19}, OutletControlAction.powerOn);
//        Thread.sleep(1000);
//        manager.doOutletAction(new int[]{1,3,5,7,9,11,13,15,17,19}, OutletControlAction.powerOff);
//        manager.doOutletAction(new int[]{2,4,6,8,10,12,14,16,18,20}, OutletControlAction.powerOn);
//        Thread.sleep(1000);
//        manager.doOutletAction(new int[]{2,4,6,8,10,12,14,16,18,20}, OutletControlAction.powerOff);


//        while (true) {
//
//            for(int i = 0; i < 10; i++) {
//                Random random = new Random();
//                manager.doOutletAction(random.nextInt(1,20),OutletControlAction.powerOn);
//            }
//
//            Thread.sleep(1000);
//            manager.doAllOutletAction(OutletControlAction.powerOff);
//
//
//
//        }

//        Thread.sleep(100);
//        manager.doAllOutletAction(OutletControlAction.powerOn);
//        System.out.print(Arrays.toString(manager.validOutletIDS()));

    }

    public void doAllOutletAction(OutletControlAction action) {
        for(PowerDistributionUnit pdu : PowerDistributionUnits) {
            pdu.doOutletAction(pdu.validOutletIDS(), action);
        }
    }

    public void setAllOutletOnDelay(int seconds) {
        for(PowerDistributionUnit pdu : PowerDistributionUnits) {
            pdu.setOutletOnDelay(pdu.validOutletIDS(), seconds);
        }
    }

    public void setAllOutletOffDelay(int seconds) {
        for(PowerDistributionUnit pdu : PowerDistributionUnits) {
            pdu.setOutletOffDelay(pdu.validOutletIDS(), seconds);
        }
    }


    // PowerDistributionUnitInterface

    @Override
    public int[] validOutletIDS() {
        int[] data = new int[OutletIDs.size()];
        for(int i = 0; i < data.length; i++) {
            data[i] = OutletIDs.get(i);
        }
        return data;
    }

    @Override
    public void doOutletAction(int outletID, OutletControlAction action) {
        idToPdu.get(outletID).doOutletAction(idToID.get(outletID), action);
    }

    @Override
    public void doOutletAction(int[] outletID, OutletControlAction action) {
//        int[] data = new int[outletID.length];
//        for(int i = 0; i < data.length; i++) {
//            data[i] = idToID.get(outletID[i]);
//        }
//
//        return idToPdu.get(outletID[1]).doOutletAction(data, action);
        for(int id : outletID) {
            doOutletAction(id, action);
        }
    }

    @Override
    public OutletStatus getOutletStatus(int outletID) {
        return idToPdu.get(outletID).getOutletStatus(idToID.get(outletID));
    }

    @Override
    public HashMap<Integer, OutletStatus> getOutletStatus(int[] outletID) {
        HashMap<PowerDistributionUnit, HashMap<Integer, Integer>> maps = new HashMap<>();
        HashMap<Integer, OutletStatus> data = new HashMap<>();
        for(int id : outletID) {
            PowerDistributionUnit pdu = idToPdu.get(id);
            int localID = idToID.get(id);
            try {
                maps.get(pdu).put(localID, id);
            } catch (Exception e) {
                HashMap<Integer, Integer> list = new HashMap<>();
                list.put(localID, id);
                maps.put(pdu, list);
            }
        }

        for(Map.Entry<PowerDistributionUnit, HashMap<Integer, Integer>> pair : maps.entrySet()) {
            int[] localIDs = pair
                    .getValue()
                    .keySet()
                    .stream()
                    .mapToInt(Integer::intValue)
                    .toArray();;

            HashMap<Integer, OutletStatus> response = pair.getKey().getOutletStatus(localIDs);
            for(Map.Entry<Integer, OutletStatus> p : response.entrySet()) {
                data.put(pair.getValue().get(p.getKey()),p.getValue());
            }
        }

        return data;
    }

    @Override
    public void setOutletOnDelay(int outletID, int seconds) {
        idToPdu.get(outletID).setOutletOnDelay(idToID.get(outletID), seconds);
    }

    @Override
    public void setOutletOnDelay(int[] outletID, int seconds) {
//        int[] data = new int[outletID.length];
//        for(int i = 0; i < data.length; i++) {
//            data[i] = idToID.get(outletID[i]);
//        }
//
//        idToPdu.get(outletID[1]).setOutletOnDelay(data, seconds);
        for(int id : outletID) {
            setOutletOnDelay(id, seconds);
        }
    }

    @Override
    public int getOutletOnDelay(int outletID) {
        return idToPdu.get(outletID).getOutletOnDelay(idToID.get(outletID));
    }

    @Override
    public void setOutletOffDelay(int outletID, int seconds) {
        idToPdu.get(outletID).setOutletOffDelay(idToID.get(outletID), seconds);
    }

    @Override
    public void setOutletOffDelay(int[] outletID, int seconds) {
//        int[] data = new int[outletID.length];
//        for(int i = 0; i < data.length; i++) {
//            data[i] = idToID.get(outletID[i]);
//        }
//
//        idToPdu.get(outletID[1]).setOutletOffDelay(data, seconds);
        for(int id : outletID) {
            setOutletOffDelay(id, seconds);
        }
    }

    @Override
    public int getOutletOffDelay(int outletID) {
        return idToPdu.get(outletID).getOutletOffDelay(idToID.get(outletID));
    }
}
