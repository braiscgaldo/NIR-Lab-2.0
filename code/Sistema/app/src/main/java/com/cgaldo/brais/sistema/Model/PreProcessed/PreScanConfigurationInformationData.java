package com.cgaldo.brais.sistema.Model.PreProcessed;

import android.util.Log;
import android.view.View;

import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class PreScanConfigurationInformationData extends Handler {
    //Configuration
    private Integer numberStoredConfiguration;
    private ByteArrayOutputStream storedConfigurationList = new ByteArrayOutputStream();
    private ByteArrayOutputStream scanConfigurationData = new ByteArrayOutputStream();

    //BufferSize
    private Integer storedConfigurationListSize;
    private Integer scanConfigurationDataSize;

    // Singleton
    private static PreScanConfigurationInformationData preScanConfigurationInformationData;

    private PreScanConfigurationInformationData(){

    }

    private PreScanConfigurationInformationData(Handler handler){
        super(handler);
    }

    public static PreScanConfigurationInformationData getInstance(){
        if (preScanConfigurationInformationData == null){
            preScanConfigurationInformationData = new PreScanConfigurationInformationData();
        }

        return preScanConfigurationInformationData;
    }

    public static PreScanConfigurationInformationData getInstance(Handler handler){
        if (preScanConfigurationInformationData == null){
            preScanConfigurationInformationData = new PreScanConfigurationInformationData(handler);
        }

        return preScanConfigurationInformationData;
    }

    @Override
    public void handleRequest(Integer requiredData, Map<String, View> placeToShow){
        if (this.canHandleRequest(requiredData)){

            Log.i("scanConfDataSize", String.valueOf(this.scanConfigurationDataSize));
            Log.i("numberStoredConf", String.valueOf(this.numberStoredConfiguration));
            Log.i("StoredConfList", String.valueOf(this.storedConfigurationList));

        } else {
            // If can not handle request, must call next handler
            super.handleRequest(requiredData, placeToShow);
        }
    }

    @Override
    public Object handleGetRequest(Integer requiredData, Integer requiredObject){
        if (canHandleRequest(requiredData)) {
            switch (requiredObject) {
                case 3001:
                    return numberStoredConfiguration;
                case 3002:
                    return storedConfigurationList;
                case 3003:
                    return storedConfigurationListSize;
                case 3004:
                    return scanConfigurationData;
                case 3005:
                    return scanConfigurationDataSize;
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
                case 3001:
                    this.numberStoredConfiguration = (Integer) data;
                    break;
                case 3002:
                    this.storedConfigurationList = (ByteArrayOutputStream) data;
                    break;
                case 3003:
                    this.storedConfigurationListSize = (Integer) data;
                    break;
                case 3004:
                    this.scanConfigurationData = (ByteArrayOutputStream) data;
                    break;
                case 3005:
                    this.scanConfigurationDataSize = (Integer) data;
                    break;
            }

        }else {

            super.handleSetRequest(requiredData, requiredObject, data);
        }

    }

    @Override
    public boolean canHandleRequest(Integer requiredData){
        return requiredData.equals(Requests.DEVICE_PRE_SCAN_CONFIGURATIONS);

    }
}
