package net.nussi.jFrameClient.outlet;

import net.nussi.jFrameClient.Main;
import net.nussi.jFrameClient.Util;
import net.nussi.snmp.pdu.PowerDistributionUnitManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class NameTextPanel extends JTextField {
    private static final PowerDistributionUnitManager manager = Main.manager;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    String LoggerPrefix;

    Outlet outlet;
    public boolean invalid = false;

    public NameTextPanel(Outlet outlet) {
        super(manager.getOutletName(outlet.outletID), 16);
        this.outlet = outlet;
        this.LoggerPrefix = "TextField["+outlet.outletID+"] ";


        this.addActionListener(new MyActionListener(this));
        this.getDocument().addDocumentListener(new MyDocumentListener(this));


        this.setForeground(Color.WHITE);
        this.setBackground(Util.dark_green);
    }

    private class MyDocumentListener implements DocumentListener {
        private NameTextPanel textPanel;

        public MyDocumentListener(NameTextPanel textPanel) {
            this.textPanel = textPanel;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            this.update(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            this.update(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            this.update(e);
        }

        public void update(DocumentEvent e) {
            try {
                String text = e.getDocument().getText(0, e.getDocument().getLength());
                logger.info(LoggerPrefix + "TextUpdate --> " + text);
                if(!text.matches("[a-zA-Z_0-9]+") || text.length() > 16) {
                    textPanel.invalid = true;
                    textPanel.setBackground(Util.dark_red);
                } else {
                    textPanel.invalid = false;
                    textPanel.setBackground(Util.dark_gray);
                }
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    private class MyActionListener implements ActionListener {
        NameTextPanel textPanel;

        MyActionListener(NameTextPanel textPanel) {
            this.textPanel = textPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
//            if(invalid) return;
            manager.setOutletName(outlet.outletID, e.getActionCommand());
            textPanel.setBackground(Util.dark_green);
            logger.info(LoggerPrefix + "TextFinish --> " + textPanel.getText());
//            logger.info("Outlet --> TextPanel --> " + e.getActionCommand());
        }
    }
}
