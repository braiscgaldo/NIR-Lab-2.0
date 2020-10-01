package com.cgaldo.brais.sistema.Model.InformationFragment;

import com.cgaldo.brais.sistema.R;

public class ScansSavedViewInformation implements ViewInformationInterface {
    private Integer info = R.string.info_scans_saved;

    //Singleton
    private static ScansSavedViewInformation infoScanSaved;

    private ScansSavedViewInformation(){

    }

    public static ScansSavedViewInformation getInstance(){
        if (infoScanSaved == null){
            infoScanSaved = new ScansSavedViewInformation();
        }

        return infoScanSaved;
    }

    @Override
    public Integer getInformation() {
        return info;
    }
}
