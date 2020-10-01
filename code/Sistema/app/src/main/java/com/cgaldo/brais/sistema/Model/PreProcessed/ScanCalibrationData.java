package com.cgaldo.brais.sistema.Model.PreProcessed;

import android.view.View;

import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;

import java.io.ByteArrayOutputStream;
import java.util.Map;

//Class for calibration data
public class ScanCalibrationData extends Handler {

    //Bytes obtained from calibration
    private ByteArrayOutputStream spectrumCalibCoeffs = new ByteArrayOutputStream();
    private ByteArrayOutputStream referenceCalibCoeffs = new ByteArrayOutputStream();
    private ByteArrayOutputStream referenceCalibMatrix = new ByteArrayOutputStream();

    //BufferSize
    private Integer spectrumCalibCoeffsSize;
    private Integer referenceCalibCoeffsSize;
    private Integer referenceCalibMatrixSize;

    // Singleton
    private static ScanCalibrationData scanCalibrationData;

    private ScanCalibrationData(){

    }

    private ScanCalibrationData(Handler handler){
        super(handler);
    }

    public static ScanCalibrationData getInstance(){
        if (scanCalibrationData == null){
            scanCalibrationData = new ScanCalibrationData();
        }

        return  scanCalibrationData;
    }

    public static ScanCalibrationData getInstance(Handler handler){
        if (scanCalibrationData == null){
            scanCalibrationData = new ScanCalibrationData(handler);
        }

        return  scanCalibrationData;
    }

    @Override
    public void handleRequest(Integer requiredData, Map<String, View> placeToShow){
        if (this.canHandleRequest(requiredData)){
            /*
            Log.i("spectrumCalibCoeffs", String.valueOf(this.spectrumCalibCoeffsSize));
            Log.i("referenceCalibCoeffs", String.valueOf(this.referenceCalibCoeffsSize));
            Log.i("referenceCalibMatrix", String.valueOf(this.referenceCalibMatrixSize));
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
                case 5001:
                    return spectrumCalibCoeffs;
                case 5002:
                    return spectrumCalibCoeffsSize;
                case 5003:
                    return referenceCalibCoeffs;
                case 5004:
                    return referenceCalibCoeffsSize;
                case 5005:
                    return referenceCalibMatrix;
                case 5006:
                    return referenceCalibMatrixSize;
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
                case 5001:
                    this.spectrumCalibCoeffs = (ByteArrayOutputStream) data;
                    break;
                case 5002:
                    this.spectrumCalibCoeffsSize = (Integer) data;
                    break;
                case 5003:
                    this.referenceCalibCoeffs = (ByteArrayOutputStream) data;
                    break;
                case 5004:
                    this.referenceCalibCoeffsSize = (Integer) data;
                    break;
                case 5005:
                    this.referenceCalibMatrix = (ByteArrayOutputStream) data;
                    break;
                case 5006:
                    this.referenceCalibMatrixSize = (Integer) data;
                    break;
            }

        }else {

            super.handleSetRequest(requiredData, requiredObject, data);
        }

    }

    @Override
    public boolean canHandleRequest(Integer requiredData){
        return requiredData.equals(Requests.DEVICE_CALIBRATION);

    }
}
