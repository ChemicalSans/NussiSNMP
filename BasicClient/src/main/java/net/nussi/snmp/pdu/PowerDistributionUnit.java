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

public class PowerDistributionUnit {

    int SNMP_VERSION = SnmpConstants.version2c;
    String HOSTNAME;
    int PORT;
    String COMMUNITY;

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

    // OUTLET CONTROL

    public boolean outletControl(int outletID, OutletControlAction action) {
        throw new RuntimeException("Method controlOutlet(int outletID, OutletControlAction action) not Implemented jet!");
    }

    public enum OutletControlAction {
        powerOn(2),
        powerOff(3),
        powerCycle(4),
        powerLock(5),
        powerUnlock(6);

        int action;
        OutletControlAction(int id) {
            this.action = id;
        }
        public int getAction() {
            return action;
        }
        public Integer32 getActionInt32() {
            return new Integer32(action);
        }

    }

    // OUTLET STATUS

    public OutletStatus outletStatus(int outletID) {
        throw new RuntimeException("Method outletStatus(int outletID) not Implemented jet!");
    }

    public enum OutletStatus {
        ON,
        OFF,
        Unknown,
    }

    // Packed Util

    public PDU setPackage(VariableBinding[] bindings) throws IOException {
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
        VariableBinding[] bindings = new VariableBinding[]{binding};
        return setPackage(bindings);
    }

    public PDU getPackage(VariableBinding[] bindings) throws IOException {
        PDU data = new PDU();
        for(VariableBinding binding : bindings) {
            data.add(binding);
        }
        data.setType(PDU.GET);

        ResponseEvent response = this.SNMP.set(data, COMMUNITYTARGET);
        PDU resp = response.getResponse();
        return resp;
    }

    public PDU getPackage(VariableBinding binding) throws IOException {
        VariableBinding[] bindings = new VariableBinding[]{binding};
        return getPackage(bindings);
    }




}
