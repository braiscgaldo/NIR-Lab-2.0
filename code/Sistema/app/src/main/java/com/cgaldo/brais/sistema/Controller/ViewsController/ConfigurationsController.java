package com.cgaldo.brais.sistema.Controller.ViewsController;

import android.util.Log;

import com.cgaldo.brais.sistema.Controller.Bluetooth.BluetoothController;
import com.cgaldo.brais.sistema.Controller.JNICalls.JNICall;
import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.StaticData.ObjectsRequired;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationsController implements ConfigurationsControllerInterface {

    private Integer countConfigurations;
    private List<Byte[]> configurationIndexes;

    // Singleton
    private static ConfigurationsController configurationsController;

    private ConfigurationsController(Integer countConfigurations){
        this.countConfigurations = countConfigurations;
        this.configurationIndexes  = new ArrayList<>();
        ByteArrayOutputStream indexes = (ByteArrayOutputStream) BluetoothController.getInstance().getInformation().handleGetRequest(Requests.DEVICE_PRE_SCAN_CONFIGURATIONS, ObjectsRequired.STORED_CONF_LIST);

        for(int i = 0; i < countConfigurations*2; i+=2){
            Byte[] index = new Byte[2];
            index[i%2] = indexes.toByteArray()[i];
            index[(i+1)%2] = indexes.toByteArray()[i+1];

            Log.i("index", String.valueOf(indexes.toByteArray()[0]) + String.valueOf(indexes.toByteArray()[1]) +
                    String.valueOf(indexes.toByteArray()[2]) + String.valueOf(indexes.toByteArray()[3]));
            Log.i("index", String.valueOf(index[0]) + String.valueOf(index[1]));
            this.configurationIndexes.add(index);
        }

    }

    public static ConfigurationsController getInstance(Integer countConfigurations){
        if (configurationsController == null){
            configurationsController = new ConfigurationsController(countConfigurations);
        }

        return configurationsController;
    }

    public Integer getCountConfigurations() {
        return countConfigurations;
    }

    public void setCountConfigurations(Integer countConfigurations) {
        this.countConfigurations = countConfigurations;
    }

    public List<Byte[]> getConfigurationIndexes() {
        return configurationIndexes;
    }

    public void setConfigurationIndexes(List<Byte[]> configurationIndexes) {
        this.configurationIndexes = configurationIndexes;
    }

    @Override
    public List<Byte[]> getConfigurations() {

        return configurationIndexes;
    }

    @Override
    public void getConfigurationData(byte[] data){
        new JNICall().dlpSpecScanReadConfiguration(data);
    }
}
