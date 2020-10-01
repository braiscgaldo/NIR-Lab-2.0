package com.cgaldo.brais.sistema.Model.PreProcessed;

import android.view.View;

import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class PreScanDataInformation extends Handler {
    //Data bytes obtained from NIRScan
    private String nameScan = new String();
    private ByteArrayOutputStream dataScan = new ByteArrayOutputStream();
    private ByteArrayOutputStream dateTimeScan = new ByteArrayOutputStream();
    private ByteArrayOutputStream typeScan = new ByteArrayOutputStream();
    private ByteArrayOutputStream packetFormatScan = new ByteArrayOutputStream();

    private ByteArrayOutputStream scanIndex = new ByteArrayOutputStream();

    //BufferSize
    private Integer dataScanSize;


    // Singleton
    private static PreScanDataInformation preScanDataInformation;

    private PreScanDataInformation(){

    }

    private PreScanDataInformation(Handler handler){
        super(handler);
    }

    public static PreScanDataInformation getInstance(){
        if (preScanDataInformation == null){
            preScanDataInformation = new PreScanDataInformation();
        }

        return preScanDataInformation;
    }

    public static PreScanDataInformation getInstance(Handler handler){
        if (preScanDataInformation == null){
            preScanDataInformation = new PreScanDataInformation(handler);
        }

        return preScanDataInformation;
    }
    @Override
    public void handleRequest(Integer requiredData, Map<String, View> placeToShow){
        if (this.canHandleRequest(requiredData)){
            /*
            Log.i("dataScan", String.valueOf(this.dataScan));
            Log.i("dateTimeScan", String.valueOf(this.dateTimeScan));
            Log.i("nameScan", String.valueOf(this.nameScan));
            */
        } else {
            // If can not handle request, must call next handler
            super.handleRequest(requiredData, placeToShow);
        }
    }

    @Override
    public Object handleGetRequest(Integer requiredData, Integer requiredObject){
        if (canHandleRequest(requiredData)) {
            switch (requiredObject) {
                case 4001:
                    return nameScan;
                case 4002:
                    return dataScan;
                case 4003:
                    return dateTimeScan;
                case 4004:
                    return typeScan;
                case 4005:
                    return packetFormatScan;
                case 4006:
                    return scanIndex;
                case 4007:
                    return dataScanSize;
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
                case 4001:
                    this.nameScan = (String) data;
                    break;
                case 4002:
                    this.dataScan = (ByteArrayOutputStream) data;
                    break;
                case 4003:
                    this.dateTimeScan = (ByteArrayOutputStream) data;
                    break;
                case 4004:
                    this.typeScan = (ByteArrayOutputStream) data;
                    break;
                case 4005:
                    this.packetFormatScan = (ByteArrayOutputStream) data;
                    break;
                case 4006:
                    this.scanIndex = (ByteArrayOutputStream) data;
                    break;
                case 4007:
                    this.dataScanSize = (Integer) data;
                    break;
            }

        }else {

            super.handleSetRequest(requiredData, requiredObject, data);
        }

    }

    @Override
    public boolean canHandleRequest(Integer requiredData){
        return requiredData.equals(Requests.DEVICE_PRE_SCAN);

    }
}
