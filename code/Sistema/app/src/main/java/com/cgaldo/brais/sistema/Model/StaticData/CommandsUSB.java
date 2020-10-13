package com.cgaldo.brais.sistema.Model.StaticData;

public class CommandsUSB {

    // Group bytes


    // Information
    public static final byte[] READ_MODEL_NUMBER;
    public static final byte[] READ_SERIAL_NUMBER;
    public static final byte[] READ_REVISIONS;

    // Reset device
    public static final byte[] RESET_DEVICE;


    static {
        // Information
        READ_MODEL_NUMBER = new byte[]{0x3C};
        READ_SERIAL_NUMBER = new byte[]{0x33};
        READ_REVISIONS = new byte[]{0x16}; // 4 bits each one TIVA SW (0 - 4) SPECTRUM Lib (16 - 20)

        // Reset device
        RESET_DEVICE = new byte[]{0x00, 0x02, 0x1A, 0x03};
    }

}
