package net.nussi.jFrameClient;

import net.nussi.jFrameClient.outlet.OutletList;
import net.nussi.snmp.pdu.PowerDistributionUnit;
import net.nussi.snmp.pdu.PowerDistributionUnitManager;
import net.nussi.snmp.pdu.products.Avocent3009h;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static JFrame mainFrame;
    public static PowerDistributionUnitManager manager;

    public static void main(String[] args) throws IOException {
        InitPDUManager();
        InitMainFrame();



        mainFrame.add(new OutletList(manager.validOutletIDS()));




    }

    private static void InitPDUManager() throws IOException {
        ArrayList<PowerDistributionUnit> pdus = new ArrayList<>();
        pdus.add(new Avocent3009h("192.168.0.119", 161, "public_read"));
        pdus.add(new Avocent3009h("192.168.0.120", 161, "public_read"));

        manager = new PowerDistributionUnitManager(pdus);

    }

    private static void InitMainFrame() {
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1920,1080);
        mainFrame.setVisible(true);
    }
}