package net.nussi.jFrameClient.outlet;

import nussi.net.pduControl.pdu.OutletControlAction;
import nussi.net.pduControl.pdu.OutletStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static net.nussi.jFrameClient.Main.manager;
import static net.nussi.jFrameClient.Util.*;

public class LockButton extends JButton implements Runnable {

    public static final String ASCII_LOCK_CLOSED = "\uD83D\uDD12";
    public static final String ASCII_LOCK_OPEN = "\uD83D\uDD13";

    private Thread thread;
    private Outlet outlet;

    public LockButton(Outlet outlet) {
        this.outlet = outlet;

//        this.setFont(new Font("Courier New", Font.PLAIN, 14));
        this.setVisible(true);
        this.addActionListener(new MyActionListener(this));

        updateText();
        updateColor();

    }

    public void updateText() {
        StringBuilder builder = new StringBuilder();
//        builder.append(ASCII_LOCK_CLOSED);
//        builder.append(" --> ");
//        builder.append(ASCII_LOCK_OPEN);
        switch (outlet.getStatus()) {
            case onLocked:
            case offLocked:
                builder.append(ASCII_LOCK_CLOSED);
                break;
//            case notSet:
//                builder.append("?");
//                break;
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

    private class MyActionListener implements ActionListener {
        LockButton button;

        MyActionListener(LockButton button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            this.button.thread = new Thread(this.button);
            this.button.thread.start();
        }
    }




}
