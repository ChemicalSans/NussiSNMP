package nussi.net.pduControl.pdu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public class BlankPDU extends PowerDistributionUnit {
    private int[] ValidOutletIDs;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String LoggerPreFix = "BlankPDU ";

    public BlankPDU(int[] ValidOutletIDs) throws IOException {
        super("0.0.0.0", 0, "");
        this.ValidOutletIDs = ValidOutletIDs;
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
}