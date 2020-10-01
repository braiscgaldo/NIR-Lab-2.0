package com.cgaldo.brais.sistema.Model.InformationFragment;

import com.cgaldo.brais.sistema.R;

public class SeeScanSavedViewInformation implements ViewInformationInterface {
    private Integer info = R.string.info_see_scan_saved;

    //Singleton
    private static SeeScanSavedViewInformation infoSeeSavedScan;

    private SeeScanSavedViewInformation(){

    }

    public static SeeScanSavedViewInformation getInstance(){
        if (infoSeeSavedScan == null){
            infoSeeSavedScan = new SeeScanSavedViewInformation();
        }

        return infoSeeSavedScan;
    }

    @Override
    public Integer getInformation() {
        return info;
    }
}
