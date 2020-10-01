package com.cgaldo.brais.sistema.Model.InformationFragment;

import com.cgaldo.brais.sistema.R;

public class ConfigurationViewInformation implements ViewInformationInterface {
    private Integer info = R.string.info_configuration;

    //Singleton
    private static ConfigurationViewInformation infoConfiguration;

    private ConfigurationViewInformation(){

    }

    public static ConfigurationViewInformation getInstance(){
        if (infoConfiguration == null){
            infoConfiguration = new ConfigurationViewInformation();
        }

        return infoConfiguration;
    }

    @Override
    public Integer getInformation() {
        return info;
    }
}
