package com.cgaldo.brais.sistema.Model.InformationFragment;

import com.cgaldo.brais.sistema.R;

public class SpectrometerViewInformation implements ViewInformationInterface{

    private Integer info = R.string.info_spectrophotometer;

    // Singleton
    private static SpectrometerViewInformation infoSpectrometer;

    private SpectrometerViewInformation(){

    }

    public static SpectrometerViewInformation getInstance(){
        if (infoSpectrometer == null){
            infoSpectrometer = new SpectrometerViewInformation();
        }

        return infoSpectrometer;
    }

    @Override
    public Integer getInformation(){
        return info;
    }
}
