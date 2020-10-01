package com.cgaldo.brais.sistema.Controller.ViewsController;

import android.content.Context;
import android.widget.Toast;

import com.cgaldo.brais.sistema.Controller.Bluetooth.BluetoothController;
import com.cgaldo.brais.sistema.Controller.ConnectionsController;
import com.cgaldo.brais.sistema.Controller.JNICalls.JNICall;
import com.cgaldo.brais.sistema.Model.ProcessedData.ScanConfigurationInfo;
import com.cgaldo.brais.sistema.Model.ProcessedData.ScanData;
import com.cgaldo.brais.sistema.Model.StaticData.ObjectsRequired;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;

import java.io.ByteArrayOutputStream;

public class ScanConfigurationController implements ScanConfigurationControllerInterface {

    // Singleton
    private static ScanConfigurationController scanConfigurationController;

    private static ConnectionsController connectionsController = BluetoothController.getInstance();

    private ScanConfigurationController(){

    }

    public static ScanConfigurationController getInstance(){
        if (scanConfigurationController == null){
            scanConfigurationController = new ScanConfigurationController();
        }

        return scanConfigurationController;
    }
    @Override
    public boolean setConfigurationScanData(Context context, String name, String minRange, String maxRange, String wavePoints, Boolean repeatScan) {
        ScanConfigurationInfo scanConfigurationInfo = ScanConfigurationInfo.getInstance();


        if ((!minRange.equals("")) && (!maxRange.equals(""))){
            if(Float.valueOf(minRange) <= Float.valueOf(maxRange)){
                scanConfigurationInfo.setRangeMin(Float.valueOf(minRange));
                scanConfigurationInfo.setRangeMin(Float.valueOf(maxRange));
            }else{
                Toast.makeText(context,"Error ranges",Toast.LENGTH_LONG).show();
                return false;
            }

        }else {

            if (!minRange.equals("")){
                if ((Float.valueOf(minRange) <= scanConfigurationInfo.getRangeMax()) && (Float.valueOf(minRange) >= 900)) {
                    scanConfigurationInfo.setRangeMin(Float.valueOf(minRange));
                }else{
                    Toast.makeText(context,"Error minRange",Toast.LENGTH_LONG).show();
                    return false;
                }
            }

            if (!maxRange.equals("")){
                if ((Float.valueOf(maxRange) >= scanConfigurationInfo.getRangeMin()) && (Float.valueOf(maxRange) <= 1700)) {
                    scanConfigurationInfo.setRangeMin(Float.valueOf(maxRange));
                }else{
                    Toast.makeText(context,"Error maxRange",Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }

        if(!name.equals("")) {
            scanConfigurationInfo.setName(name);
        }

        if(!wavePoints.equals("")){
            scanConfigurationInfo.setRangeMin(Float.valueOf(wavePoints));
        }

        if(repeatScan){
            scanConfigurationInfo.setRepeatScan(true);
        }else{
            scanConfigurationInfo.setRepeatScan(false);
        }

        return true;
    }

    @Override
    public void activeConfiguration(){
        ByteArrayOutputStream dataArray = (ByteArrayOutputStream) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_PRE_SCAN_CONFIGURATIONS, ObjectsRequired.SCAN_CONF_DATA);
        byte[] dataJNI = dataArray.toByteArray();
        new JNICall().dlpSpecScanReadConfiguration(dataJNI);
    }

    @Override
    public void treatConfiguration() {
        ByteArrayOutputStream dataArray = (ByteArrayOutputStream) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_PRE_SCAN_CONFIGURATIONS, ObjectsRequired.SCAN_CONF_DATA);
        byte[] dataJNI = dataArray.toByteArray();
        new JNICall().dlpSpecScanReadConfiguration(dataJNI);

    }
}
