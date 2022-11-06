package net.nussi.pduControl.pdu;

import java.util.HashMap;

public interface PowerDistributionUnitInterface {


    int[] validOutletIDS();

    // CONTROL
    void doOutletAction(int outletID, OutletControlAction action);
    void doOutletAction(int[] outletID, OutletControlAction action);

    // STATUS
    OutletStatus getOutletStatus(int outletID);
    HashMap<Integer, OutletStatus> getOutletStatus(int[] outletID);

    // DELAYS
    void setOutletOnDelay(int outletID, int seconds);
    void setOutletOnDelay(int[] outletID, int seconds);
    int getOutletOnDelay(int outletID);

    void setOutletOffDelay(int outletID, int seconds);
    void setOutletOffDelay(int[] outletID, int seconds);
    int getOutletOffDelay(int outletID);


    // Name
    void setOutletName(int outletID, String name);
    String getOutletName(int outletID);

}
