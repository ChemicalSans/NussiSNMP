package net.nussi.snmp.ups;

import java.util.ArrayList;

public interface UninterruptiblePowerSupplyInterface {

    float getCurrent();     //upsHighPrecOutputCurrent.0
    float getVoltage();     //upsHighPrecOutputVoltage.0
    float getFrequency();   //upsHighPrecOutputFrequency.0
    float getPower();        //
    float getLoad(); //upsHighPrecOutputLoad.0

    UninterruptiblePowerSupplyStatus getStatus();


}
