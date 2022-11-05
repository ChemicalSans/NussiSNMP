package net.nussi.jFrameClient;

import nussi.net.pduControl.pdu.OutletControlAction;
import nussi.net.pduControl.pdu.OutletStatus;
import nussi.net.pduControl.pdu.PowerDistributionUnitManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Outlet extends JPanel implements Runnable {
    private static final PowerDistributionUnitManager manager = Main.manager;
    private final int outletID;
    private final Thread thread;

    private OutletStatus status;

    private final JButton switchButton;
    private final JButton lockButton;

    public Outlet(int outletID) {
        this.outletID = outletID;

        this.switchButton = new JButton();
        this.switchButton.setText(calcTextSwitchButton());
        this.switchButton.setFont(new Font("Courier New", Font.PLAIN, 14));
//        this.switchButton.setBackground(new Color(0,51,204));
//        this.switchButton.setForeground(Color.WHITE);
//        this.switchButton.setFocusPainted(false);
//        this.switchButton.setBorderPainted(false);
        this.switchButton.setVisible(true);
        this.switchButton.addActionListener(new SwitchButtonActionListener(this));


        this.lockButton = new JButton();
        this.lockButton.setText(calcTextLockButton());
        this.lockButton.setFont(new Font("Courier New", Font.PLAIN, 14));
//        this.lockButton.setBackground(new Color(0,51,204));
//        this.lockButton.setForeground(Color.WHITE);
//        this.lockButton.setFocusPainted(false);
//        this.lockButton.setBorderPainted(false);
        this.lockButton.setVisible(true);
        this.lockButton.addActionListener(new LockButtonActionListener(this));



        this.add(switchButton);
        this.add(lockButton);
        this.setVisible(true);

        updateOutletStatus();
        updateColor();

        this.thread = new Thread(this);
        this.thread.start();
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

    public void lockButtonActionPerformed(ActionEvent e) {
        updateOutletStatus();
        if(status != OutletStatus.onLocked && status != OutletStatus.offLocked) {
            manager.doOutletAction(outletID, OutletControlAction.powerLock);
        } else {
            manager.doOutletAction(outletID, OutletControlAction.powerUnlock);
        }

//        updateOutletStatus();
//        updateColor();
    }

    private void updateOutletStatus() {
        this.status = manager.getOutletStatus(outletID);
    }

    private void updateColor() {
         Color dark_red = new Color(100, 0, 0, 255);
        Color dark_green = new Color(0, 100, 0,250);
        Color dark_gray = new Color(50,50,50,255);

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


        this.lockButton.setForeground(Color.WHITE);
        switch (status) {
            case onLocked:
            case offLocked:
                this.lockButton.setBackground(dark_red);
                break;
            case notSet:
                this.lockButton.setBackground(dark_gray);
                break;
            default:
                this.lockButton.setBackground(dark_green);
                break;
        }
        this.lockButton.setText(calcTextLockButton());
        this.updateUI();


    }




    @Override
    public void run() {
        try {
            while (true) {
                this.status = OutletStatus.notSet;
                updateColor();
                updateOutletStatus();
                updateColor();


                Thread.sleep(250);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private String calcTextSwitchButton(){
        StringBuilder builder = new StringBuilder();
        builder.append("ID: ");
        if(outletID<10) builder.append(" ");
        if(outletID<100) builder.append(" ");
        builder.append(outletID);

        return builder.toString();
    }

    private String calcTextLockButton(){
        String builder = "" + status;

        while (builder.length()<10) {
            builder = " " + builder;
        }

        builder = "Lock: " + builder;

        return builder;
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


    private class LockButtonActionListener implements ActionListener {
        Outlet outlet;

        public LockButtonActionListener(Outlet outlet) {
            this.outlet = outlet;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            outlet.lockButtonActionPerformed(e);
        }
    }

}
