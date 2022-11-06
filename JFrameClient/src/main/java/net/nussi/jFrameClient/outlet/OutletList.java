package net.nussi.jFrameClient.outlet;

import nussi.net.pduControl.pdu.OutletStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static net.nussi.jFrameClient.Main.manager;

public class OutletList extends JPanel implements Runnable {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    HashMap<Integer, Outlet> outlets = new HashMap<>();
    int[] outletIDs;
    Thread thread;

    public OutletList(int[] outletIDs) {
        this.outletIDs = outletIDs;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setSize(200,80);
        this.setBackground(Color.YELLOW);
        this.setVisible(true);

        for(int id : outletIDs) {
            Outlet outlet = new Outlet(id);
            this.add(outlet);
            this.outlets.put(id, outlet);
        }

        this.thread = new Thread(this);
        this.thread.start();
    }


    @Override
    public void run() {
        try {
            while (true) {
                logger.info("Requesting outlet status!");
                HashMap<Integer, OutletStatus> data = manager.getOutletStatus(outletIDs);

                for(Map.Entry<Integer, OutletStatus> pair : data.entrySet()) {
                    outlets.get(pair.getKey()).setStatus(pair.getValue());
                }

                Thread.sleep(250);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
