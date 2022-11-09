package net.nussi.snmp.pdu;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.ArrayList;

public abstract class PowerDistributionUnit implements PowerDistributionUnitInterface {
    protected int SNMP_VERSION = SnmpConstants.version2c;
    protected String HOSTNAME;
    protected int PORT;
    protected String COMMUNITY;

    CommunityTarget COMMUNITYTARGET;
    TransportMapping TRANSPORT;
    Snmp SNMP;

    public PowerDistributionUnit(String HOSTNAME, int PORT, String COMMUNITY) throws IOException {
        this.HOSTNAME = HOSTNAME;
        this.PORT = PORT;
        this.COMMUNITY = COMMUNITY;

        this.COMMUNITYTARGET = new CommunityTarget();
        this.COMMUNITYTARGET.setCommunity(new OctetString(COMMUNITY));
        this.COMMUNITYTARGET.setVersion(this.SNMP_VERSION);
        this.COMMUNITYTARGET.setAddress(new UdpAddress(HOSTNAME + "/" + PORT));
        this.COMMUNITYTARGET.setRetries(2);
        this.COMMUNITYTARGET.setTimeout(1000);

        this.TRANSPORT = new DefaultUdpTransportMapping();
        this.TRANSPORT.listen();

        this.SNMP = new Snmp(this.TRANSPORT);
    }


    // Packed Util

    public PDU setPackage(ArrayList<VariableBinding> bindings) throws IOException {
        PDU data = new PDU();
        for(VariableBinding binding : bindings) {
            data.add(binding);
        }
        data.setType(PDU.SET);

        ResponseEvent response = this.SNMP.set(data, COMMUNITYTARGET);
        PDU resp = response.getResponse();
        return resp;
    }

    public PDU setPackage(VariableBinding binding) throws IOException {
        PDU data = new PDU();
        data.add(binding);
        data.setType(PDU.SET);


        ResponseEvent response = this.SNMP.set(data, COMMUNITYTARGET);
        PDU resp = response.getResponse();
        return resp;
    }

    public PDU getPackage(VariableBinding[] bindings) throws IOException {
        ArrayList<VariableBinding> b = new ArrayList<>();
        for(VariableBinding binding : bindings) {
            b.add(binding);
        }
        return getPackage(b);
    }
    public PDU getPackage(ArrayList<VariableBinding> bindings) throws IOException {
        PDU data = new PDU();
        for(VariableBinding binding : bindings) {
            data.add(binding);
        }
        data.setType(PDU.GET);

        ResponseEvent response = this.SNMP.get(data, COMMUNITYTARGET);
        PDU resp = response.getResponse();
        return resp;
    }

    public PDU getPackage(VariableBinding binding) throws IOException {
        VariableBinding[] bindings = new VariableBinding[]{binding};
        return getPackage(bindings);
    }

    public PDU getBulkPackage(VariableBinding[] bindings) throws IOException {
        PDU data = new PDU();
        for(VariableBinding binding : bindings) {
            data.add(binding);
        }
        data.setType(PDU.GETBULK);

        ResponseEvent response = this.SNMP.getBulk(data, COMMUNITYTARGET);
        PDU resp = response.getResponse();
        return resp;
    }

    public PDU getBulkPackage(VariableBinding binding) throws IOException {
        VariableBinding[] bindings = new VariableBinding[]{binding};
        return getPackage(bindings);
    }




}
