package com.cgaldo.brais.sistema.Model.StaticData;

public class CommandSpectrometer {

    //Commands BLE
    public static byte RESET[];

    static{
        RESET = new byte[]{0x02, 0x1A, 0x03};
    }
}
