package com.cgaldo.brais.sistema.Controller.Storage;

import java.util.HashMap;
import java.util.Map;

public class ScanSelected implements ScanSelectedInterface {

    private String problemSelectedString = "";
    private String scanSelectedString = "";
    private Map<String, String> labelsMap = new HashMap<>();

    //Singleton
    private static ScanSelected scanSelected;

    private ScanSelected(){

    }

    public static ScanSelected getInstance(){
        if (scanSelected == null){
            scanSelected = new ScanSelected();
        }

        return scanSelected;
    }

    public String getProblemSelectedString() {
        return problemSelectedString;
    }

    public void setProblemSelectedString(String problemSelectedString) {
        this.problemSelectedString = problemSelectedString;
    }

    public String getScanSelectedString() {
        return scanSelectedString;
    }

    public void setScanSelectedString(String scanSelectedString) {
        this.scanSelectedString = scanSelectedString;
    }

    public Map<String, String> getLabelsMap() {
        return labelsMap;
    }

    public void setLabelsMap(Map<String, String> labelsMap) {
        this.labelsMap = labelsMap;
    }

    @Override
    public void reset(){
        this.labelsMap = new HashMap<>();
        this.problemSelectedString = "";
        this.scanSelectedString = "";
    }
}
