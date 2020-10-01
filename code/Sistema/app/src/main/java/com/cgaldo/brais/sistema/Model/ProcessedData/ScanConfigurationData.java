package com.cgaldo.brais.sistema.Model.ProcessedData;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;

import java.util.Map;

//This class is used in Java JNI
public class ScanConfigurationData extends Handler {

    //Configuration data

    //Same data in both types
    private int scanType;
    private int scanConfigurationIndex;
    private byte serialNumber[];
    private byte configurationName[];

    //Data in Slew Type
    private byte numSections; //In Slew Scan
    private byte sectionScanTypes[];
    private byte sectionWidths[];
    private int sectionWavelengthStartNm[];
    private int sectionWavelengthEndNm[];
    private int sectionNumPatterns[];
    private int sectionNumRepeats[];
    private int sectionExposureTime[];

    //Data in NOT Slew Type
    private int wavelengthStartNm;
    private int wavelengthEndNm;
    private int widthPx;
    private int numPatterns;
    private int numRepeats;

    //Know who type of scan is
    private boolean slewType;

    // Singleton
    private static ScanConfigurationData scanConfigurationData;

    //Constructor handler
    private ScanConfigurationData(){

    }

    private ScanConfigurationData(Handler handler){
        super(handler);
    }

    //Constructor for Slew Type
    private ScanConfigurationData(int scanType, int scanConfigurationIndex, byte[] serialNumber, byte[] configurationName, byte numSections, byte[] sectionScanTypes, byte[] sectionWidths, int[] sectionWavelengthStartNm, int[] sectionWavelengthEndNm, int[] sectionNumPatterns, int[] sectionNumRepeats, int[] sectionExposureTime) {
        this.scanType = scanType;
        this.scanConfigurationIndex = scanConfigurationIndex;
        this.serialNumber = serialNumber;
        this.configurationName = configurationName;
        this.numSections = numSections;
        this.sectionScanTypes = sectionScanTypes;
        this.sectionWidths = sectionWidths;
        this.sectionWavelengthStartNm = sectionWavelengthStartNm;
        this.sectionWavelengthEndNm = sectionWavelengthEndNm;
        this.sectionNumPatterns = sectionNumPatterns;
        this.sectionNumRepeats = sectionNumRepeats;
        this.sectionExposureTime = sectionExposureTime;
        this.slewType = true;
    }

    //Constructor for NOT Slew Type

    private ScanConfigurationData(int scanType, int scanConfigurationIndex, byte[] serialNumber, byte[] configurationName, int wavelengthStartNm, int wavelengthEndNm, int widthPx, int numPatterns, int numRepeats) {
        this.scanType = scanType;
        this.scanConfigurationIndex = scanConfigurationIndex;
        this.serialNumber = serialNumber;
        this.configurationName = configurationName;
        this.wavelengthStartNm = wavelengthStartNm;
        this.wavelengthEndNm = wavelengthEndNm;
        this.widthPx = widthPx;
        this.numPatterns = numPatterns;
        this.numRepeats = numRepeats;
        this.slewType = false;
    }

    public static ScanConfigurationData getInstance(int scanType, int scanConfigurationIndex, byte[] serialNumber, byte[] configurationName, byte numSections, byte[] sectionScanTypes, byte[] sectionWidths, int[] sectionWavelengthStartNm, int[] sectionWavelengthEndNm, int[] sectionNumPatterns, int[] sectionNumRepeats, int[] sectionExposureTime){
        if (scanConfigurationData == null){
            scanConfigurationData = new ScanConfigurationData(scanType, scanConfigurationIndex, serialNumber, configurationName, numSections,sectionScanTypes, sectionWidths, sectionWavelengthStartNm, sectionWavelengthEndNm, sectionNumPatterns, sectionNumRepeats, sectionExposureTime);
        }
        scanConfigurationData.reset();
        scanConfigurationData.scanType = scanType;
        scanConfigurationData.scanConfigurationIndex = scanConfigurationIndex;
        scanConfigurationData.serialNumber = serialNumber;
        scanConfigurationData.configurationName = configurationName;
        scanConfigurationData.numSections = numSections;
        scanConfigurationData.sectionScanTypes = sectionScanTypes;
        scanConfigurationData.sectionWidths = sectionWidths;
        scanConfigurationData.sectionWavelengthStartNm = sectionWavelengthStartNm;
        scanConfigurationData.sectionWavelengthEndNm = sectionWavelengthEndNm;
        scanConfigurationData.sectionNumPatterns = sectionNumPatterns;
        scanConfigurationData.sectionNumRepeats = sectionNumRepeats;
        scanConfigurationData.sectionExposureTime = sectionExposureTime;
        scanConfigurationData.slewType = true;

        return scanConfigurationData;

    }

    public static ScanConfigurationData getInstance(int scanType, int scanConfigurationIndex, byte[] serialNumber, byte[] configurationName, int wavelengthStartNm, int wavelengthEndNm, int widthPx, int numPatterns, int numRepeats){
        if (scanConfigurationData == null){
            scanConfigurationData = new ScanConfigurationData(scanType, scanConfigurationIndex, serialNumber, configurationName, wavelengthStartNm, wavelengthEndNm, widthPx, numPatterns, numRepeats);
        }
        scanConfigurationData.reset();
        scanConfigurationData.scanType = scanType;
        scanConfigurationData.scanConfigurationIndex = scanConfigurationIndex;
        scanConfigurationData.serialNumber = serialNumber;
        scanConfigurationData.configurationName = configurationName;
        scanConfigurationData.wavelengthStartNm = wavelengthStartNm;
        scanConfigurationData.wavelengthEndNm = wavelengthEndNm;
        scanConfigurationData.widthPx = widthPx;
        scanConfigurationData.numPatterns = numPatterns;
        scanConfigurationData.numRepeats = numRepeats;
        scanConfigurationData.slewType = false;

        return scanConfigurationData;

    }

    public static ScanConfigurationData getInstance(){
        if (scanConfigurationData == null){
            scanConfigurationData = new ScanConfigurationData();
        }

        return scanConfigurationData;
    }

    public static ScanConfigurationData getInstance(Handler handler){
        if (scanConfigurationData == null){
            scanConfigurationData = new ScanConfigurationData(handler);
        }

        return scanConfigurationData;
    }

    private void reset(){
        this.configurationName = null;
        this.numPatterns = 0;
        this.numRepeats = 0;
        this.numSections = 0;
        this.scanConfigurationIndex = 0;
        this.scanType = 0;
        this.sectionExposureTime = null;
        this.sectionNumPatterns = null;
        this.sectionNumRepeats = null;
        this.sectionScanTypes = null;
        this.sectionWavelengthEndNm = null;
        this.sectionWavelengthStartNm = null;
        this.sectionWidths = null;
        this.serialNumber = null;
        this.slewType = false;
        this.wavelengthEndNm = 0;
        this.wavelengthStartNm = 0;
        this.widthPx = 0;
    }

    // Chain of Responsibility
    @Override
    public void handleRequest(Integer requiredData, Map<String, View> placeToShow){
        if (this.canHandleRequest(requiredData)){
            // We put the information into the required views
            Log.i("data", "DATA");
        } else {
            // If can not handle request, must call next handler
            super.handleRequest(requiredData, placeToShow);
        }
    }

    @Override
    public Object handleGetRequest(Integer requiredData, Integer requiredObject){
        if (canHandleRequest(requiredData)) {
            switch (requiredObject) {
                case 7001:
                    return scanType;
                case 7002:
                    return scanConfigurationIndex;
                case 7003:
                    return serialNumber;
                case 7004:
                    return configurationName;
                case 7005:
                    return numSections;
                case 7006:
                    return sectionScanTypes;
                case 7007:
                    return sectionWidths;
                case 7008:
                    return sectionWavelengthStartNm;
                case 7009:
                    return sectionWavelengthEndNm;
                case 7010:
                    return sectionNumPatterns;
                case 7011:
                    return sectionNumRepeats;
                case 7012:
                    return sectionExposureTime;
                case 7013:
                    return wavelengthStartNm;
                case 7014:
                    return wavelengthEndNm;
                case 7015:
                    return widthPx;
                case 7016:
                    return numPatterns;
                case 7017:
                    return numRepeats;
                case 7018:
                    return slewType;
                default:
                    return null;
            }

        }else {

            return super.handleGetRequest(requiredData, requiredObject);
        }


    }

    @Override
    public void handleSetRequest(Integer requiredData, Integer requiredObject, Object data){
        if (this.canHandleRequest(requiredData)) {
            switch (requiredObject) {
                case 7001:
                    this.scanType = (int) data;
                    break;
                case 7002:
                    this.scanConfigurationIndex = (int) data;
                    break;
                case 7003:
                    this.serialNumber = (byte[]) data;
                    break;
                case 7004:
                    this.configurationName = (byte[]) data;
                    break;
                case 7005:
                    this.numSections = (byte) data;
                    break;
                case 7006:
                    this.sectionScanTypes = (byte[]) data;
                    break;
                case 7007:
                    this.sectionWidths = (byte[]) data;
                    break;
                case 7008:
                    this.sectionWavelengthStartNm = (int[]) data;
                    break;
                case 7009:
                    this.sectionWavelengthEndNm = (int[]) data;
                    break;
                case 7010:
                    this.sectionNumPatterns = (int[]) data;
                    break;
                case 7011:
                    this.sectionNumRepeats = (int[]) data;
                    break;
                case 7012:
                    this.sectionExposureTime = (int[]) data;
                    break;
                case 7013:
                    this.wavelengthStartNm = (int) data;
                    break;
                case 7014:
                    this.wavelengthEndNm = (int) data;
                    break;
                case 7015:
                    this.widthPx = (int) data;
                    break;
                case 7016:
                    this.numPatterns = (int) data;
                    break;
                case 7017:
                    this.numRepeats = (int) data;
                    break;
                case 7018:
                    this.slewType = (boolean) data;
                    break;
                default:
                    break;
            }

        }else {

            super.handleSetRequest(requiredData, requiredObject, data);
        }

    }

    @Override
    public boolean canHandleRequest(Integer requiredData){
        return requiredData.equals(Requests.DEVICE_SCAN_CONFIGURATION);

    }
}
