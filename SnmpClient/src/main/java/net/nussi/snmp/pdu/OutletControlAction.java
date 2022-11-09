package net.nussi.snmp.pdu;

import org.snmp4j.smi.Integer32;

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


    @Override
    public String toString() {
        switch (this.action) {
            case 2: return "powerOn";
            case 3: return "powerOff";
            case 4: return "powerCycle";
            case 5: return "powerLock";
            case 6: return "powerUnlock";
        }
        return super.toString();
    }
}
