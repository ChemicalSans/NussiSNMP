package net.nussi.jFrameClient.outlet;

import net.nussi.snmp.pdu.OutletControlAction;

import java.awt.*;

import static net.nussi.jFrameClient.Main.manager;
import static net.nussi.jFrameClient.Util.*;

public class LockButton extends MyButton {
    private Outlet outlet;

    public LockButton(Outlet outlet) {
        super();
        this.outlet = outlet;
    }

    public void updateText() {
        StringBuilder builder = new StringBuilder();
        switch (outlet.getStatus()) {
            case onLocked:
            case offLocked:
                builder.append(ASCII_LOCK_CLOSED);
                break;
            default:
                builder.append(ASCII_LOCK_OPEN);
                break;
        }

        this.setText(builder.toString());
    }

    public void updateColor() {
        this.setForeground(Color.WHITE);
        switch (outlet.getStatus()) {
            case onLocked:
            case offLocked:
                this.setBackground(dark_red);
                break;
            case notSet:
                this.setBackground(dark_gray);
                break;
            default:
                this.setBackground(dark_green);
                break;
        }
        this.updateUI();
    }


    @Override
    public void run() {
        outlet.updateOutletStatus();

        switch (outlet.getStatus())  {
            case onLocked:
            case offLocked:
                manager.doOutletAction(outlet.outletID, OutletControlAction.powerUnlock);
                break;
            default:
                manager.doOutletAction(outlet.outletID, OutletControlAction.powerLock);
                break;
        }
    }





}
