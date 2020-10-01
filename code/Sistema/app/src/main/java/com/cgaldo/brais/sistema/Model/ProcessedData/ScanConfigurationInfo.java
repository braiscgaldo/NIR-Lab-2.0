package com.cgaldo.brais.sistema.Model.ProcessedData;

//Class for set the measure parameters
public class ScanConfigurationInfo {

    //Attributes
    String name = "default";
    float rangeMin = 900;
    float rangeMax = 1700;
    boolean repeatScan = false;
    int wavelengthPoints = 100;


    //Singleton
    private static ScanConfigurationInfo scanConfigurationInfo;

    private ScanConfigurationInfo() {

    }

    public static ScanConfigurationInfo getInstance(){
        if(scanConfigurationInfo == null){
            scanConfigurationInfo = new ScanConfigurationInfo();
        }

        return scanConfigurationInfo;
    }

    // Methods

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRangeMin() {
        return rangeMin;
    }

    public void setRangeMin(float rangeMin) {
        this.rangeMin = rangeMin;
    }

    public float getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(float rangeMax) {
        this.rangeMax = rangeMax;
    }

    public boolean isRepeatScan() {
        return repeatScan;
    }

    public void setRepeatScan(boolean repeatScan) {
        this.repeatScan = repeatScan;
    }

    public int getWavelengthPoints() {
        return wavelengthPoints;
    }

    public void setWavelengthPoints(int wavelengthPoints) {
        this.wavelengthPoints = wavelengthPoints;
    }
}
