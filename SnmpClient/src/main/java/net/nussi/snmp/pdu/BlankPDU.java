package net.nussi.snmp.pdu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class BlankPDU extends PowerDistributionUnit {
    private final int[] ValidOutletIDs;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String LoggerPreFix = "BlankPDU ";
    private final HashMap<Integer, String> names = new HashMap<>();

    public BlankPDU(int[] ValidOutletIDs) throws IOException {
        super("0.0.0.0", 0, "");
        this.ValidOutletIDs = ValidOutletIDs;

        for(int id : ValidOutletIDs) {
            names.put(id, "outlet_"+id);
        }
    }

    @Override
    public int[] validOutletIDS() {
        return ValidOutletIDs;
    }

    @Override
    public void doOutletAction(int outletID, OutletControlAction action) {
        logger.info(LoggerPreFix+"OutletAction --> " + action + " --> " + outletID);
    }

    @Override
    public void doOutletAction(int[] outletID, OutletControlAction action) {
        logger.info(LoggerPreFix+"OutletAction --> " + action + " --> " + Arrays.toString(outletID));
    }

    @Override
    public OutletStatus getOutletStatus(int outletID) {
        logger.info(LoggerPreFix+"OutletStatus  --> " + outletID + " --> " + OutletStatus.notSet);
        return OutletStatus.notSet;
    }

    @Override
    public HashMap<Integer, OutletStatus> getOutletStatus(int[] outletID) {
        HashMap<Integer,OutletStatus> data = new HashMap<>();
        for(int id : outletID) {
            data.put(id, OutletStatus.notSet);
        }
        logger.info(LoggerPreFix+"OutletStatus  --> " + outletID + " --> " + data);
        return data;
    }

    @Override
    public void setOutletOnDelay(int outletID, int seconds) {
        logger.info(LoggerPreFix+"OutletOnDelay --> " + seconds + " --> " + outletID);
    }

    @Override
    public void setOutletOnDelay(int[] outletID, int seconds) {
        logger.info(LoggerPreFix+"OutletOnDelay --> " + seconds + " --> " + Arrays.toString(outletID));
    }

    @Override
    public int getOutletOnDelay(int outletID) {
        logger.info(LoggerPreFix+"OutletOnDelay --> " + outletID + " --> " + 0);
        return 0;
    }

    @Override
    public void setOutletOffDelay(int outletID, int seconds) {
        logger.info(LoggerPreFix+"OutletOffDelay --> " + seconds + " --> " + outletID);
    }

    @Override
    public void setOutletOffDelay(int[] outletID, int seconds) {
        logger.info(LoggerPreFix+"OutletOffDelay --> " + seconds + " --> " + Arrays.toString(outletID));
    }

    @Override
    public int getOutletOffDelay(int outletID) {
        logger.info(LoggerPreFix+"OutletOffDelay --> " + outletID + " --> " + 0);
        return 0;
    }


    @Override
    public void setOutletName(int outletID, String name) {
        names.put(outletID, name);
    }

    @Override
    public String getOutletName(int outletID) {
        return names.get(outletID);
    }
}
