package com.cgaldo.brais.sistema.Model.DeviceInformation;

import android.view.View;
import android.widget.TextView;

import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;

import java.util.Map;

public class DeviceStatusInfo extends Handler {
    //Status data
    private Integer batteryLevel;
    private Integer temperatureLevel;
    private Integer temperatureThreshold;
    private Integer humidityLevel;
    private Integer humidityThreshold;

    // Singleton
    private static DeviceStatusInfo deviceStatusInfo;

    private DeviceStatusInfo() {
        super();
    }

    private DeviceStatusInfo(Handler handler){
        super(handler);
    }

    public static DeviceStatusInfo getInstance(){
        if (deviceStatusInfo == null){
            deviceStatusInfo = new DeviceStatusInfo();
        }

        return deviceStatusInfo;
    }

    public static DeviceStatusInfo getInstance(Handler handler){
        if (deviceStatusInfo == null){
            deviceStatusInfo = new DeviceStatusInfo(handler);
        }

        return deviceStatusInfo;
    }

    // Chain of Responsibility
    @Override
    public void handleRequest(Integer requiredData, Map<String, View> placeToShow){
        if (this.canHandleRequest(requiredData)){
            // We put the information into the required views
            TextView tv;
            tv = (TextView) placeToShow.get("batteryLevel");
            tv.setText(String.valueOf(this.batteryLevel));
            tv = (TextView) placeToShow.get("temperatureLevel");
            tv.setText(String.valueOf(this.temperatureLevel / 100));
            tv = (TextView) placeToShow.get("humidityLevel");
            tv.setText(String.valueOf(this.humidityLevel / 100));
        } else {
            // If can not handle request, must call next handler
            super.handleRequest(requiredData, placeToShow);
        }
    }

    @Override
    public Object handleGetRequest(Integer requiredData, Integer requiredObject){
        if (canHandleRequest(requiredData)) {
            switch (requiredObject) {
                case 2001:
                    return batteryLevel;
                case 2002:
                    return temperatureLevel;
                case 2003:
                    return temperatureThreshold;
                case 2004:
                    return humidityLevel;
                case 2005:
                    return humidityThreshold;
                default:
                    return null;
            }

        }else {

            return super.handleGetRequest(requiredData, requiredObject);
        }

    }

    @Override
    public void handleSetRequest(Integer requiredData, Integer requiredObject, Object data){
        if (canHandleRequest(requiredData)) {
            switch (requiredObject) {
                case 2001:
                    this.batteryLevel = (Integer) data;
                    break;
                case 2002:
                    this.temperatureLevel = (Integer) data;
                    break;
                case 2003:
                    this.temperatureThreshold = (Integer) data;
                    break;
                case 2004:
                    this.humidityLevel = (Integer) data;
                    break;
                case 2005:
                    this.humidityThreshold = (Integer) data;
                    break;
            }

        }else {

            super.handleSetRequest(requiredData, requiredObject, data);
        }

    }

    @Override
    public boolean canHandleRequest(Integer requiredData){
        return requiredData.equals(Requests.DEVICE_STATUS);

    }
}
