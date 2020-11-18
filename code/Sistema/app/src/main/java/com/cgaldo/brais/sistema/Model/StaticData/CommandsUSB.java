package com.cgaldo.brais.sistema.Model.StaticData;

import java.nio.ByteBuffer;

public class CommandsUSB {

    // Group bytes
    public static final String SPECTROMETER_NAME_DIRECTION;
    public static final Integer INPUT_ENDPOINT;
    public static final Integer OUTPUT_ENDPOINT;


    // Information
    public static final byte[] READ_MODEL_NUMBER;
    public static final byte[] READ_SERIAL_NUMBER;
    public static final byte[] READ_REVISIONS;

    // Reset device
    public static final byte[] RESET_DEVICE;


    static {
        SPECTROMETER_NAME_DIRECTION = "/dev/bus/usb/001/003";
        INPUT_ENDPOINT = 0;
        OUTPUT_ENDPOINT = 1;

        // Information
        READ_MODEL_NUMBER = new byte[]{0x3C};
        READ_SERIAL_NUMBER = new byte[]{0x33};
        READ_REVISIONS = new UsbPacket()
                .setGroupByte((byte) 0x02)
                .setCommandByte((byte) 0x16)
                .setFlagRW(UsbPacket.RW.READ)
                .setFlagReady(UsbPacket.READY.READY)
                .toByteArray(); // 4 bits each one TIVA SW (0 - 4) SPECTRUM Lib (16 - 20)

        // Reset device
        RESET_DEVICE =  new UsbPacket()
                .setGroupByte((byte) 0x02)
                .setCommandByte((byte) 0x1A)
                .setFlagRW(UsbPacket.RW.WRITE)
                .setFlagReady(UsbPacket.READY.READY)
                .toByteArray();
    }

}

class UsbPacket {
    private byte flags;
    private int sequence = 0;
    private byte commandByte;
    private byte groupByte;
    private byte[] data;

    enum RW {
        WRITE,
        READ
    }

    enum READY {
        BUSY,
        READY
    }

    enum ERROR {
        SUCCESS,
        ERROR,
        BUSY
    }

    UsbPacket setFlagRW(RW flag) {
        if (flag == RW.READ) {
            this.flags = (byte) (this.flags | 0x80);
        }
        return this;
    }

    UsbPacket setFlagReady(READY flag) {
        if (flag == READY.READY) {
            this.flags = (byte) (this.flags | 0x40);
        }
        return this;
    }

    UsbPacket setFlagError(ERROR flag) {
        if (flag == ERROR.ERROR) {
            this.flags = (byte) (this.flags | 0x20);
        }
        if (flag == ERROR.BUSY) {
            this.flags = (byte) (this.flags | 0x10);
        }
        return this;
    }

    UsbPacket setSequence(int sequence) {
        if (0 > sequence || sequence > 255) {
            throw new IllegalArgumentException("Only values from 0 to 255 are allowed");
        }
        this.sequence = sequence;
        return this;
    }

    UsbPacket setCommandByte(byte commandByte) {
        this.commandByte = commandByte;
        return this;
    }

    UsbPacket setGroupByte(byte groupByte) {
        this.groupByte = groupByte;
        return this;
    }

    public UsbPacket setData(byte[] data) {
        this.data = data;
        return this;
    }

    byte[] toByteArray() {
        byte[] dataLength = new byte[2];
        int lengthOfCommandBytes = 2;
        if (data != null) {
            dataLength = ByteBuffer.allocate(2).putInt(lengthOfCommandBytes + data.length).array();
        }
        else {
            dataLength[0] = (byte) (lengthOfCommandBytes & 0xFF);
            dataLength[1] = (byte) ((byte) ((lengthOfCommandBytes >> 8)) & 0xFF);
        }
        byte[] header = new byte[] {
                0x00,
                flags,
                (byte) sequence,
                dataLength[0],
                dataLength[1],
                commandByte,
                groupByte
        };

        byte[] packet;
        if (data != null) {
            packet = new byte[7 + data.length];
            System.arraycopy(header, 0, packet, 0, header.length);
            System.arraycopy(data, 0, packet, 7, data.length);
        }
        else {
            packet = header;
        }
        return packet;
    }
}