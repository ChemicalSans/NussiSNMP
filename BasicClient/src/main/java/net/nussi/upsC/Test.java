package net.nussi.upsC;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

public class Test {
    public static final Logger logger = LoggerFactory.getLogger("net.nussi.upsC.Test");


    private static String  ipAddress  = "192.168.0.119";

    private static String  port    = "161";

    // OID of MIB RFC 1213; Scalar Object = .iso.org.dod.internet.mgmt.mib-2.system.sysDescr.0
    private static String  oidValue  = ".1.3.6.1.4.1.10418.17.2.5.5.1.6.1.1.";  // ends with 0 for scalar object

    private static int    snmpVersion  = SnmpConstants.version2c;

    private static String  community  = "public_read";


    public static void main(String[] args) throws Exception {
        AvocentPDU pdu = new AvocentPDU("public_read",SnmpConstants.version2c,"192.168.0.119",161);

//        pdu.controlOutlet(1, AvocentPDU.OutletControl.powerOn);

        for(int i = 1; i <= 10; i++) {
            pdu.controlOutlet(i, AvocentPDU.OutletControl.powerOff);
            Thread.sleep(100);
        }

        Thread.sleep(1000);

        for(int i = 1; i <= 10; i++) {
            pdu.controlOutlet(i, AvocentPDU.OutletControl.powerOn);
            Thread.sleep(100);
        }

        Thread.sleep(1000);

        pdu.controlOutlet(new int[]{1,2,3,4,5}, AvocentPDU.OutletControl.powerOff);

        Thread.sleep(500);

        pdu.controlOutlet(new int[]{1,2,3,4,5}, AvocentPDU.OutletControl.powerOn);
        pdu.controlOutlet(new int[]{6,7,8,9,10}, AvocentPDU.OutletControl.powerOff);

        Thread.sleep(500);

        pdu.controlOutlet(new int[]{6,7,8,9,10}, AvocentPDU.OutletControl.powerOn);

//        System.out.println("SNMP SET Demo");
//
//        // Create TransportMapping and Listen
//        TransportMapping transport = new DefaultUdpTransportMapping();
//        transport.listen();
//
//
//        // Create Target Address object
//        CommunityTarget comtarget = new CommunityTarget();
//        comtarget.setCommunity(new OctetString(community));
//        comtarget.setVersion(snmpVersion);
//        comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
//        comtarget.setRetries(2);
//        comtarget.setTimeout(1000);
//
//        // Create the PDU object
//        PDU pdu = new PDU();
//        pdu.add(new VariableBinding(new OID(oidValue+1), new Integer32(2)));
//        pdu.setType(PDU.SET);
//
//
////        pdu.setRequestID(new Integer32(1));
//
//        // Create Snmp object for sending data to Agent
//        Snmp snmp = new Snmp(transport);
//
//
////        pdu.setVariableBindings((List<? extends VariableBinding>) new VariableBinding(new OID(oidValue), "2"));
//        System.out.println("Sending Request to Agent...");
//        ResponseEvent response = snmp.set(pdu, comtarget);
//
//        snmp.set(pdu, comtarget);
//        // Process Agent Response
//        if (response != null)
//        {
//            System.out.println("Got Response from Agent");
//            PDU responsePDU = response.getResponse();
//
//            if (responsePDU != null)
//            {
//                int errorStatus = responsePDU.getErrorStatus();
//                int errorIndex = responsePDU.getErrorIndex();
//                String errorStatusText = responsePDU.getErrorStatusText();
//
//                if (errorStatus == PDU.noError)
//                {
//                    System.out.println("Snmp Get Response = " + responsePDU.getVariableBindings());
//                    System.out.println(responsePDU.getVariable(new OID(oidValue)));
//                }
//                else
//                {
//                    System.out.println("Error: Request Failed");
//                    System.out.println("Error Status = " + errorStatus);
//                    System.out.println("Error Index = " + errorIndex);
//                    System.out.println("Error Status Text = " + errorStatusText);
//                }
//            }
//            else
//            {
//                System.out.println("Error: Response PDU is null");
//            }
//        }
//        else
//        {
//            System.out.println("Error: Agent Timeout... ");
//        }
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
//        snmp.close();
    }


    public static class AvocentPDU {
        CommunityTarget comtarget;
        TransportMapping transport;
        Snmp snmp;

        public AvocentPDU(String community, int snmpVersion, String ipAddress, int port) throws Exception {
            comtarget = new CommunityTarget();
            comtarget.setCommunity(new OctetString(community));
            comtarget.setVersion(snmpVersion);
            comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
            comtarget.setRetries(2);
            comtarget.setTimeout(1000);

            transport = new DefaultUdpTransportMapping();
            transport.listen();

            snmp = new Snmp(transport);
        }



        public PDU controlOutlet(int number, OutletControl outletControl) throws IOException {
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.10418.17.2.5.5.1.6.1.1." + number), new Integer32(outletControl.getId())));
            System.out.println("Sendig: " + ".1.3.6.1.4.1.10418.17.2.5.5.1.6.1.1." + number + " --> " + outletControl.getId());
            pdu.setType(PDU.SET);

            ResponseEvent response = snmp.set(pdu, comtarget);
            return response.getResponse();
        }

        public PDU controlOutlet(int[] numbers, OutletControl outletControl) throws IOException {
            PDU pdu = new PDU();
            for(int i : numbers) {
                pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.10418.17.2.5.5.1.6.1.1." + i), new Integer32(outletControl.getId())));
                System.out.println("Sendig: " + ".1.3.6.1.4.1.10418.17.2.5.5.1.6.1.1." + i + " --> " + outletControl.getId());
            }
            pdu.setType(PDU.SET);

            ResponseEvent response = snmp.set(pdu, comtarget);
            return response.getResponse();
        }

        enum OutletControl {
            powerOn(2),
            powerOff(3),
            powerCycle(4),
            powerLock(5),
            powerUnlock(6);

            int id;
            OutletControl(int id) {
             this.id = id;
            }
            public int getId() {
                return id;
            }
        }

    }


}



