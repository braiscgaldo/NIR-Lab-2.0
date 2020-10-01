package com.cgaldo.brais.sistema.Model.DeviceInformation;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;

import java.util.Map;

public class DeviceInformationInfo extends Handler {
    //Info data
    private String manufacture;
    private String modelNumber;
    private String serialNumber;
    private String hardwareRevision;
    private String tivaFirmwareVersion;
    private String spectrumLibraryRevision;

    //Singleton
    private static DeviceInformationInfo deviceInformationInfo;

    private DeviceInformationInfo() {
        super();
    }

    private DeviceInformationInfo(Handler handler){
        super(handler);
    }

    public static DeviceInformationInfo getInstance(){
        if (deviceInformationInfo == null){
            deviceInformationInfo = new DeviceInformationInfo();
        }

        return deviceInformationInfo;
    }

    public static DeviceInformationInfo getInstance(Handler handler){
        if (deviceInformationInfo == null){
            deviceInformationInfo = new DeviceInformationInfo(handler);
        }

        return deviceInformationInfo;
    }

    // Chain of Responsibility
    @Override
    public void handleRequest(Integer requiredData, Map<String, View> placeToShow){
        if (this.canHandleRequest(requiredData)){
            // We put the information into the required views
            TextView tv;
            tv = (TextView) placeToShow.get("manufacture");
            tv.setText(this.manufacture);
            tv = (TextView) placeToShow.get("modelNumber");
            tv.setText(this.modelNumber);
            tv = (TextView) placeToShow.get("serialNumber");
            tv.setText(this.serialNumber);
            tv = (TextView) placeToShow.get("hardwareRevision");
            tv.setText(this.hardwareRevision);
            tv = (TextView) placeToShow.get("tivaFirmwareRevision");
            tv.setText(this.tivaFirmwareVersion);
            tv = (TextView) placeToShow.get("spectrumLibraryRevision");
            tv.setText(this.spectrumLibraryRevision);
            Log.i("data", this.modelNumber);
        } else {
            // If can not handle request, must call next handler
            super.handleRequest(requiredData, placeToShow);
        }
    }

    @Override
    public Object handleGetRequest(Integer requiredData, Integer requiredObject){
        if (canHandleRequest(requiredData)) {
            switch (requiredObject) {
                case 1001:
                    return manufacture;
                case 1002:
                    return modelNumber;
                case 1003:
                    return serialNumber;
                case 1004:
                    return hardwareRevision;
                case 1005:
                    return tivaFirmwareVersion;
                case 1006:
                    return spectrumLibraryRevision;
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
                case 1001:
                    this.manufacture = (String) data;
                    break;
                case 1002:
                    this.modelNumber = (String) data;
                    break;
                case 1003:
                    this.serialNumber = (String) data;
                    break;
                case 1004:
                    this.hardwareRevision = (String) data;
                    break;
                case 1005:
                    this.tivaFirmwareVersion = (String) data;
                    break;
                case 1006:
                    this.spectrumLibraryRevision = (String) data;
                    break;
            }

        }else {

            super.handleSetRequest(requiredData, requiredObject, data);
        }

    }

    @Override
    public boolean canHandleRequest(Integer requiredData){
        return requiredData.equals(Requests.DEVICE_INFORMATION);

    }
}
