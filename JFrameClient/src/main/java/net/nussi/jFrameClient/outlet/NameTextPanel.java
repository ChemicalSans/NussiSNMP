package net.nussi.jFrameClient.outlet;

import net.nussi.jFrameClient.Main;
import net.nussi.pduControl.pdu.PowerDistributionUnitManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class NameTextPanel extends JTextField {
    private static final PowerDistributionUnitManager manager = Main.manager;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    Outlet outlet;

    public NameTextPanel(Outlet outlet) {
        this.outlet = outlet;
        this.setText(manager.getOutletName(outlet.outletID));
        this.addActionListener(new MyActionListener(this));
    }

    private class MyActionListener implements ActionListener {
        NameTextPanel textPanel;

        MyActionListener(NameTextPanel textPanel) {
            this.textPanel = textPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            manager.setOutletName(outlet.outletID, e.getActionCommand());
//            logger.info("Outlet --> TextPanel --> " + e.getActionCommand());
        }
    }
}
