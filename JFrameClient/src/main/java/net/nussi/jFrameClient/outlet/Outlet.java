package net.nussi.jFrameClient.outlet;

import net.nussi.jFrameClient.Main;
import nussi.net.pduControl.pdu.OutletControlAction;
import nussi.net.pduControl.pdu.OutletStatus;
import nussi.net.pduControl.pdu.PowerDistributionUnitManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static net.nussi.jFrameClient.Util.*;

public class Outlet extends JPanel {
    private static final PowerDistributionUnitManager manager = Main.manager;
    public final int outletID;

    private OutletStatus status;

    private final JButton switchButton;
    private final LockButton lockButton;

    public Outlet(int outletID) {
        this.outletID = outletID;
        this.status = OutletStatus.notSet;
        this.setVisible(true);

        this.switchButton = new JButton();
        this.switchButton.setText(calcTextSwitchButton());
        this.switchButton.setFont(new Font("Courier New", Font.PLAIN, 14));
        this.switchButton.setVisible(true);
        this.switchButton.addActionListener(new SwitchButtonActionListener(this));




        this.add(switchButton);
        this.lockButton = new LockButton(this);
        this.add(this.lockButton);


    }

    public void switchButtonActionPerformed(ActionEvent e) {
        updateOutletStatus();
        if(status == OutletStatus.off) {
            manager.doOutletAction(outletID, OutletControlAction.powerOn);
            return;
        }
        if(status == OutletStatus.on) {
            manager.doOutletAction(outletID, OutletControlAction.powerOff);
        }
//        updateOutletStatus();
//        updateColor();
    }


    public void updateOutletStatus() {
        this.status = manager.getOutletStatus(outletID);
    }

    private void updateColor() {
        this.lockButton.updateText();
        this.lockButton.updateColor();

        this.switchButton.setForeground(Color.WHITE);
        switch (status) {
            case off:
            case offLocked:
                this.switchButton.setBackground(dark_red);
                break;
            case on:
            case onLocked:
                this.switchButton.setBackground(dark_green);
                break;
            default:
                this.switchButton.setBackground(dark_gray);
                break;
        }
        this.switchButton.setText(calcTextSwitchButton());
        this.updateUI();
    }

    private void updateText() {
        this.lockButton.updateText();
    }

    public OutletStatus getStatus() {
        return status;
    }

    public void setStatus(OutletStatus status) {
        this.status = status;
        updateText();
        updateColor();
    }

    private String calcTextSwitchButton(){
        StringBuilder builder = new StringBuilder();
        builder.append("ID: ");
        if(outletID<10) builder.append(" ");
        if(outletID<100) builder.append(" ");
        builder.append(outletID);

        return builder.toString();
    }
    private class SwitchButtonActionListener implements ActionListener {


        Outlet outlet;

        public SwitchButtonActionListener(Outlet outlet) {
            this.outlet = outlet;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            outlet.switchButtonActionPerformed(e);
        }

    }


}
