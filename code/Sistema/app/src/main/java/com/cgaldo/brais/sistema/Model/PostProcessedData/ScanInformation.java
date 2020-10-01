package com.cgaldo.brais.sistema.Model.PostProcessedData;

import android.util.Log;
import android.view.View;

import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScanInformation extends Handler {

    private List<Float> intensity;
    private List<Float> reflectance;
    private List<Float> absorbance;
    private List<Double> wavelengthPoints;

    // Singleton
    private static ScanInformation scanInformation;

    private ScanInformation(){

    }

    private ScanInformation(Handler handler){
        super(handler);
    }

    public static ScanInformation getInstance(){
        if (scanInformation == null){
            scanInformation = new ScanInformation();
        }

        return scanInformation;
    }

    public static ScanInformation getInstance(Handler handler){
        if (scanInformation == null){
            scanInformation = new ScanInformation(handler);
        }

        return scanInformation;
    }

    // Convert information
    public void convertScanData(int scanSize, double[] wavelengthPoints, int[] resultIntensity, int[] uncalibIntensity){
        this.intensity = new ArrayList<>();
        this.reflectance = new ArrayList<>();
        this.absorbance = new ArrayList<>();
        this.wavelengthPoints = new ArrayList<>();

        // We compute intensity
        for(int i : uncalibIntensity){
            this.intensity.add((float) i);
        }

        // We compute wavelength points
        for(double wp: wavelengthPoints){
            this.wavelengthPoints.add(wp);
        }

        for(int i = 0; i < scanSize; i++){
            // We compute reflectance
            this.reflectance.add((float)uncalibIntensity[i] / (float) resultIntensity[i]);

            // We compute absorbance
            this.absorbance.add((float) -Math.log10((double)this.reflectance.get(i)));

        }

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
                case 8001:
                    return intensity;
                case 8002:
                    return reflectance;
                case 8003:
                    return absorbance;
                case 8004:
                    return wavelengthPoints;
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
                case 8001:
                    this.intensity = (List<Float>) data;
                    break;
                case 8002:
                    this.reflectance = (List<Float>) data;
                    break;
                case 8003:
                    this.absorbance = (List<Float>) data;
                    break;
                case 8004:
                    this.wavelengthPoints = (List<Double>) data;
                    break;
            }

        }else {

            super.handleSetRequest(requiredData, requiredObject, data);
        }

    }

    @Override
    public boolean canHandleRequest(Integer requiredData){
        if (requiredData == Requests.DEVICE_SCAN_INFORMATION)
            return true;

        return false;
    }



}
