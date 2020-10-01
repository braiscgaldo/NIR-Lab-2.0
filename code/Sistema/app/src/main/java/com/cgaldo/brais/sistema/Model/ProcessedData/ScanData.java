package com.cgaldo.brais.sistema.Model.ProcessedData;

import android.util.Log;
import android.view.View;

import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;

import java.util.Map;

//This class is used in Java JNI
public class ScanData extends Handler {

    //Data in serialized scan
    private int scanSize;
    private double wavelenghtPoints[];
    private int resultIntensity[];
    private int uncalibrationIntensity[];

    private static ScanData scanData;

    //Constructor
    private ScanData(int scanSize, double[] wavelenghtPoints, int[] resultIntensity, int[] uncalibrationIntensity) {
        this.scanSize = scanSize;
        this.wavelenghtPoints = wavelenghtPoints;
        this.resultIntensity = resultIntensity;
        this.uncalibrationIntensity = uncalibrationIntensity;
    }

    private ScanData(){

    }

    private ScanData(Handler handler){
        super(handler);
    }

    public static ScanData getInstance(int scanSize, double[] wavelenghtPoints, int[] resultIntensity, int[] uncalibrationIntensity){
        if (scanData == null){
            scanData = new ScanData(scanSize, wavelenghtPoints, resultIntensity, uncalibrationIntensity);
        }
        scanData.scanSize = scanSize;
        scanData.wavelenghtPoints = wavelenghtPoints;
        scanData.resultIntensity = resultIntensity;
        scanData.uncalibrationIntensity = uncalibrationIntensity;
        return scanData;
    }

    //Handler
    public static ScanData getInstance(){
        if (scanData == null){
            scanData = new ScanData();
        }

        return scanData;
    }

    public static ScanData getInstance(Handler handler){
        if (scanData == null){
            scanData = new ScanData(handler);
        }

        return scanData;
    }

    // Chain of Responsibility
    @Override
    public void handleRequest(Integer requiredData, Map<String, View> placeToShow){
        if (this.canHandleRequest(requiredData)){
            // We put the information into the required views
            Log.i("data", "data");
        } else {
            // If can not handle request, must call next handler
            super.handleRequest(requiredData, placeToShow);
        }
    }

    @Override
    public Object handleGetRequest(Integer requiredData, Integer requiredObject){
        if (canHandleRequest(requiredData)) {
            switch (requiredObject) {
                case 6001:
                    return scanSize;
                case 6002:
                    return wavelenghtPoints;
                case 6003:
                    return resultIntensity;
                case 6004:
                    return uncalibrationIntensity;
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
                case 6001:
                    this.scanSize = (Integer) data;
                    break;
                case 6002:
                    this.wavelenghtPoints = (double[]) data;
                    break;
                case 6003:
                    this.resultIntensity = (int[]) data;
                    break;
                case 6004:
                    this.uncalibrationIntensity = (int[]) data;
                    break;
            }

        }else {

            super.handleSetRequest(requiredData, requiredObject, data);
        }

    }

    @Override
    public boolean canHandleRequest(Integer requiredData){
        return requiredData.equals(Requests.DEVICE_SCAN);

    }
}
