package com.cgaldo.brais.sistema.Model.StaticData;

public class Requests {
    // Request to make to the spectrometer
    public static Integer DEVICE_INFORMATION;
    public static Integer DEVICE_STATUS;
    public static Integer DEVICE_PRE_SCAN_CONFIGURATIONS;
    public static Integer DEVICE_PRE_SCAN;
    public static Integer DEVICE_CALIBRATION;
    public static Integer DEVICE_SCAN;
    public static Integer DEVICE_SCAN_CONFIGURATION;
    public static Integer DEVICE_SCAN_INFORMATION;

    static {
        DEVICE_INFORMATION = 1001;
        DEVICE_STATUS = 1002;
        DEVICE_PRE_SCAN_CONFIGURATIONS = 1003;
        DEVICE_PRE_SCAN = 1004;
        DEVICE_CALIBRATION = 1005;
        DEVICE_SCAN = 1006;
        DEVICE_SCAN_CONFIGURATION = 1007;
        DEVICE_SCAN_INFORMATION = 1008;
    }
}
