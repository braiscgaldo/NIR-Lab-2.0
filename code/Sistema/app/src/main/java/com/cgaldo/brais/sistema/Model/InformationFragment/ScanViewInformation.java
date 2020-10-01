package com.cgaldo.brais.sistema.Model.InformationFragment;

import com.cgaldo.brais.sistema.R;

public class ScanViewInformation implements ViewInformationInterface {

    private Integer info = R.string.info_scan;

    //Singleton
    private static ScanViewInformation infoScan;

    private ScanViewInformation(){

    }

    public static ScanViewInformation getInstance(){
        if (infoScan == null){
            infoScan = new ScanViewInformation();
        }

        return infoScan;
    }

    @Override
    public Integer getInformation() {
        return info;
    }
}
