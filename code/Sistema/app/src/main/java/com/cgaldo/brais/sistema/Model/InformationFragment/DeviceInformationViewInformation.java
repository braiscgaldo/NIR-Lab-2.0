package com.cgaldo.brais.sistema.Model.InformationFragment;

import com.cgaldo.brais.sistema.R;

public class DeviceInformationViewInformation implements ViewInformationInterface {
    private Integer info = R.string.info_device_information;

    //Singleton
    private static DeviceInformationViewInformation infoDeviceInformation;

    private DeviceInformationViewInformation(){

    }

    public static DeviceInformationViewInformation getInstance(){
        if (infoDeviceInformation == null){
            infoDeviceInformation = new DeviceInformationViewInformation();
        }

        return infoDeviceInformation;
    }

    @Override
    public Integer getInformation() {
        return info;
    }
}
