package com.cgaldo.brais.sistema.Model.StaticData;

public class ObjectsRequired {
    // From device information info
    public static Integer MANUFACTURE;
    public static Integer MODEL_NUMBER;
    public static Integer SERIAL_NUMBER;
    public static Integer HARDWARE_REVISION;
    public static Integer TIVA_FIRMWARE_REVISION;
    public static Integer SPECTRUM_LIBRARY_REVISION;

    // From device status info
    public static Integer BATTERY_LEVEL;
    public static Integer TEMPERATURE_LEVEL;
    public static Integer TEMPERATURE_THRESHOLD;
    public static Integer HUMIDITY_LEVEL;
    public static Integer HUMIDITY_THRESHOLD;

    // From PreScanConfiguration info
    public static Integer NUMBER_STORED_CONFIGURATION;
    public static Integer STORED_CONF_LIST;
    public static Integer STORED_CONF_LIST_SIZE;
    public static Integer SCAN_CONF_DATA;
    public static Integer SCAN_CONF_DATA_SIZE;

    // From PreScanDataInformation
    public static Integer NAME_SCAN;
    public static Integer DATA_SCAN;
    public static Integer DATE_TIME_SCAN;
    public static Integer TYPE_SCAN;
    public static Integer PACKET_FORMAT_SCAN;
    public static Integer SCAN_INDEX;
    public static Integer DATA_SCAN_SIZE;

    // From ScanCalibrationData
    public static Integer SPECTRUM_CALIB_COEFF;
    public static Integer SPECTRUM_CALIB_COEFF_SIZE;
    public static Integer REFERENCE_CALIB_COEFF;
    public static Integer REFERENCE_CALIB_COEFF_SIZE;
    public static Integer REFERENCE_CALIB_MATRIX;
    public static Integer REFERENCE_CALIB_MATRIX_SIZE;

    // From ScanData
    public static Integer SCAN_SCAN_SIZE;
    public static Integer SCAN_WAVELENGTH_POINTS;
    public static Integer SCAN_RESULT_INTENSITY;
    public static Integer SCAN_UNCALIB_INTENSITY;

    // From ScanConfigurationData
    public static Integer CONF_SCAN_TYPE;
    public static Integer CONF_SCAN_CONF_INDEX;
    public static Integer CONF_SERIAL_NUMBER;
    public static Integer CONF_CONF_NAME;
    public static Integer CONF_NUM_SECTIONS;
    public static Integer CONF_SECTION_SCAN_TYPES;
    public static Integer CONF_SECTION_WAVELENGTH_START_NM;
    public static Integer CONF_SECTION_WAVELENGTH_END_NM;
    public static Integer CONF_SECTION_NUM_PATTERNS;
    public static Integer CONF_SECTION_NUM_REPEATS;
    public static Integer CONF_SECTION_EXPOSURE_TIME;
    public static Integer CONF_WAVELENGTH_START_NM;
    public static Integer CONF_WAVELENGTH_END_NM;
    public static Integer CONF_NUM_PATTERNS;
    public static Integer CONF_NUM_REPEATS;
    public static Integer CONF_SLEW_TYPE;
    public static Integer CONF_SECTION_WIDTHS;
    public static Integer CONF_WIDTH_PX;

    // From ScanInformation
    public static Integer SCAN_INTENSITY;
    public static Integer SCAN_ABSORBANCE;
    public static Integer SCAN_REFLECTANCE;
    public static Integer SCAN_WAVELENGTH;


    static {
        // Device information info
        MANUFACTURE = 1001;
        MODEL_NUMBER = 1002;
        SERIAL_NUMBER = 1003;
        HARDWARE_REVISION = 1004;
        TIVA_FIRMWARE_REVISION = 1005;
        SPECTRUM_LIBRARY_REVISION = 1006;

        // Device status info
        BATTERY_LEVEL = 2001;
        TEMPERATURE_LEVEL = 2002;
        TEMPERATURE_THRESHOLD = 2003;
        HUMIDITY_LEVEL = 2004;
        HUMIDITY_THRESHOLD = 2005;

        // PreScanConfiguration Info
        NUMBER_STORED_CONFIGURATION = 3001;
        STORED_CONF_LIST = 3002;
        STORED_CONF_LIST_SIZE = 3003;
        SCAN_CONF_DATA = 3004;
        SCAN_CONF_DATA_SIZE = 3005;

        // From PreScanDataInformation
        NAME_SCAN = 4001;
        DATA_SCAN = 4002;
        DATE_TIME_SCAN = 4003;
        TYPE_SCAN = 4004;
        PACKET_FORMAT_SCAN = 4005;
        SCAN_INDEX = 4006;
        DATA_SCAN_SIZE = 4007;

        // From ReferenceCalibMatrix
        SPECTRUM_CALIB_COEFF = 5001;
        SPECTRUM_CALIB_COEFF_SIZE = 5002;
        REFERENCE_CALIB_COEFF = 5003;
        REFERENCE_CALIB_COEFF_SIZE = 5004;
        REFERENCE_CALIB_MATRIX = 5005;
        REFERENCE_CALIB_MATRIX_SIZE = 5006;

        // From ScanData
        SCAN_SCAN_SIZE = 6001;
        SCAN_WAVELENGTH_POINTS = 6002;
        SCAN_RESULT_INTENSITY = 6003;
        SCAN_UNCALIB_INTENSITY = 6004;

        // From ScanConfigurationData
        CONF_SCAN_TYPE = 7001;
        CONF_SCAN_CONF_INDEX = 7002;
        CONF_SERIAL_NUMBER = 7003;
        CONF_CONF_NAME = 7004;
        CONF_NUM_SECTIONS = 7005;
        CONF_SECTION_SCAN_TYPES = 7006;
        CONF_SECTION_WIDTHS = 7007;
        CONF_SECTION_WAVELENGTH_START_NM = 7008;
        CONF_SECTION_WAVELENGTH_END_NM = 7009;
        CONF_SECTION_NUM_PATTERNS = 7010;
        CONF_SECTION_NUM_REPEATS = 7011;
        CONF_SECTION_EXPOSURE_TIME = 7012;
        CONF_WAVELENGTH_START_NM = 7013;
        CONF_WAVELENGTH_END_NM = 7014;
        CONF_WIDTH_PX = 7015;
        CONF_NUM_PATTERNS = 7016;
        CONF_NUM_REPEATS = 7017;
        CONF_SLEW_TYPE = 7018;

        // From ScanInformation
        SCAN_INTENSITY = 8001;
        SCAN_REFLECTANCE = 8002;
        SCAN_ABSORBANCE = 8003;
        SCAN_WAVELENGTH = 8004;
    }
}
