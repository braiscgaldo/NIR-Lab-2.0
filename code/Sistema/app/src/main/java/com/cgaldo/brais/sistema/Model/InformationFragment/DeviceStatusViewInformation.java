package com.cgaldo.brais.sistema.Model.InformationFragment;

import com.cgaldo.brais.sistema.R;

public class DeviceStatusViewInformation implements ViewInformationInterface {
    private Integer info = R.string.info_device_status;

    //Singleton
    private static DeviceStatusViewInformation infoDeviceStatus;

    private DeviceStatusViewInformation(){

    }

    public static DeviceStatusViewInformation getInstance(){
        if (infoDeviceStatus == null){
            infoDeviceStatus = new DeviceStatusViewInformation();
        }

        return infoDeviceStatus;
    }

    @Override
    public Integer getInformation() {
        return info;
    }
}
