package net.nussi.jFrameClient.outlet;

import net.nussi.pduControl.pdu.OutletControlAction;

import java.awt.*;

import static net.nussi.jFrameClient.Main.manager;
import static net.nussi.jFrameClient.Util.*;

public class SwitchButton extends MyButton {
    private Outlet outlet;

    public SwitchButton(Outlet outlet) {
        super();
        this.outlet = outlet;
    }

    public void updateText() {
        StringBuilder builder = new StringBuilder();
        builder.append("ID: ");
        if(outlet.outletID<10) builder.append(" ");
        if(outlet.outletID<100) builder.append(" ");
        builder.append(outlet.outletID);

        this.setText(builder.toString());
    }

    public void updateColor() {
        this.setForeground(Color.WHITE);
        switch (outlet.getStatus()) {
            case off:
            case offLocked:
                this.setBackground(dark_red);
                break;
            case on:
            case onLocked:
                this.setBackground(dark_green);
                break;
            default:
                this.setBackground(dark_gray);
                break;
        }
        this.updateUI();
    }


    @Override
    public void run() {
        outlet.updateOutletStatus();

        switch (outlet.getStatus())  {
            case off:
                manager.doOutletAction(outlet.outletID, OutletControlAction.powerOn);
                break;
            case on:
                manager.doOutletAction(outlet.outletID, OutletControlAction.powerOff);
                break;
        }
    }
}
