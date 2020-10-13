package com.cgaldo.brais.sistema.Model.StaticData;

public class Broadcasts {
    // Messages for LocalBroadcastManager
    public static String FINISH_CONNECTION;
    public static String FINISH_INFORMATION;
    public static String FINISH_STATUS;
    public static String FINISH_CALIBRATION;
    public static String FINISH_SCAN;
    public static String FINISH_CONFIGURATION;
    public static String FINISH_ACTIVATE_CONFIGURATION;
    public static String FINISH_EDIT_SCAN;
    public static String ERROR_COMM;
    public static String USB_STATE_CHANGE;


    static {
        FINISH_CONFIGURATION = "finishedConfiguration";
        FINISH_CALIBRATION = "finishedCalibration";
        FINISH_CONNECTION = "finishedConnection";
        FINISH_INFORMATION = "finishedInformation";
        FINISH_SCAN = "finishedScan";
        FINISH_STATUS = "finishedStatus";
        FINISH_ACTIVATE_CONFIGURATION = "finishedActivateConfiguration";
        FINISH_EDIT_SCAN = "finishedEditScan";
        ERROR_COMM = "errorCommunications";
        USB_STATE_CHANGE = "usbStateChanged";
    }
}
