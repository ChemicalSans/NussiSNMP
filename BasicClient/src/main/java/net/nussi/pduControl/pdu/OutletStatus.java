package net.nussi.pduControl.pdu;

import org.snmp4j.smi.Integer32;

public enum OutletStatus {
    off ( 1 ) ,
    on ( 2 ) ,
    offLocked ( 3 ) ,
    onLocked ( 4 ) ,
    offCycle ( 5 ) ,
    onPendingOff ( 6 ) ,
    offPendingOn ( 7 ) ,
    onPendingCycle ( 8 ) ,
    notSet ( 9 ) ,
    onFixed ( 10 ) ,
    offShutdown ( 11 ) ,
    tripped ( 12 );

    int status;
    OutletStatus(int id) {
        this.status = id;
    }
    public int getStatus() {
        return status;
    }
    public Integer32 getActionInt32() {
        return new Integer32(status);
    }

    public static OutletStatus fromInt(int i) {
        switch (i) {
            case 1: return off;
            case 2: return on;
            case 3: return offLocked;
            case 4: return onLocked;
            case 5: return offCycle;
            case 6: return onPendingOff;
            case 7: return offPendingOn;
            case 8: return onPendingCycle;
            case 9: return notSet;
            case 10: return onFixed;
            case 11: return offShutdown;
            case 12: return tripped;
        }
        return null;
    }
}
