package net.nussi.upsC;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.fluent.SnmpBuilder;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.net.SocketException;

public class Test {
    public static final Logger logger = LoggerFactory.getLogger("net.nussi.upsC.Test");


    private static String  ipAddress  = "192.168.0.115";

    private static String  port    = "161";

    // OID of MIB RFC 1213; Scalar Object = .iso.org.dod.internet.mgmt.mib-2.system.sysDescr.0
    private static String  oidValue  = "1.3.6.1.2.1.1.3.0";  // ends with 0 for scalar object

    private static int    snmpVersion  = SnmpConstants.version2c;

    private static String  community  = "public";


    public static void main(String[] args) throws Exception {

//        UserTarget target = new UserTarget<>();
//
//        target.setAddress(new IpAddress("192.168.0.115"));
//        target.setVersion(SnmpConstants.version3);
//        target.setRetries(1);
//        target.setTimeout(500);
//        target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
//        target.setSecurityName(new OctetString("MD5DES"));
//
//
//        Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
//        snmp.listen();
//
//
//        PDU requestPDU = new PDU();
//        requestPDU.setType(PDU.INFORM);
//
//        ResponseEvent response = snmp.send(requestPDU, target);
//        if (response.getResponse() == null) {
//            // request timed out
//            logger.error("Failed!");
//        }
//
//        for(var res : response.getResponse().getAll()) {
//            logger.info(res.toString());
//
//        }



        System.out.println("SNMP GET Demo");

        // Create TransportMapping and Listen
        TransportMapping transport = new DefaultUdpTransportMapping();
        transport.listen();

        // Create Target Address object
        CommunityTarget comtarget = new CommunityTarget();
        comtarget.setCommunity(new OctetString(community));
        comtarget.setVersion(snmpVersion);
        comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
        comtarget.setRetries(2);
        comtarget.setTimeout(1000);

        // Create the PDU object
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oidValue)));
        pdu.setType(PDU.GET);
//        pdu.setRequestID(new Integer32(1));

        // Create Snmp object for sending data to Agent
        Snmp snmp = new Snmp(transport);

        for(int i = 0; i < 10; i++) {
            System.out.println("Sending Request to Agent...");
            ResponseEvent response = snmp.get(pdu, comtarget);

            // Process Agent Response
            if (response != null)
            {
                System.out.println("Got Response from Agent");
                PDU responsePDU = response.getResponse();

                if (responsePDU != null)
                {
                    int errorStatus = responsePDU.getErrorStatus();
                    int errorIndex = responsePDU.getErrorIndex();
                    String errorStatusText = responsePDU.getErrorStatusText();

                    if (errorStatus == PDU.noError)
                    {
                        System.out.println("Snmp Get Response = " + responsePDU.getVariableBindings());
                        System.out.println(responsePDU.getVariable(new OID(oidValue)));
                    }
                    else
                    {
                        System.out.println("Error: Request Failed");
                        System.out.println("Error Status = " + errorStatus);
                        System.out.println("Error Index = " + errorIndex);
                        System.out.println("Error Status Text = " + errorStatusText);
                    }
                }
                else
                {
                    System.out.println("Error: Response PDU is null");
                }
            }
            else
            {
                System.out.println("Error: Agent Timeout... ");
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        snmp.close();
    }



}



