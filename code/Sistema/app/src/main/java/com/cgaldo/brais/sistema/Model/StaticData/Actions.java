package com.cgaldo.brais.sistema.Model.StaticData;

// Actions that can do the spectrometer
public class Actions {

    public static Integer RETRIEVE_DEVICE_INFORMATION;
    public static Integer RETRIEVE_DEVICE_STATUS;
    public static Integer CALIBRATE_DEVICE;
    public static Integer SCAN_SAMPLE;
    public static Integer CONFIGURATE_DEVICE;
    public static Integer RESET_DEVICE;
    public static Integer ACTIVATE_CONFIGURATION;

    static{
        RETRIEVE_DEVICE_INFORMATION = 1001;
        RETRIEVE_DEVICE_STATUS = 1002;
        CALIBRATE_DEVICE = 1003;
        SCAN_SAMPLE = 1004;
        CONFIGURATE_DEVICE = 1005;
        RESET_DEVICE = 1006;
        ACTIVATE_CONFIGURATION = 1007;
    }
}
