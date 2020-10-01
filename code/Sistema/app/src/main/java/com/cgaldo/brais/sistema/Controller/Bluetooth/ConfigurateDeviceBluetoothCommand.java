package com.cgaldo.brais.sistema.Controller.Bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.cgaldo.brais.sistema.Controller.Commands.ConfigurateDeviceCommand;
import com.cgaldo.brais.sistema.Model.StaticData.ServicesCharacteristicsBLE;

public class ConfigurateDeviceBluetoothCommand implements ConfigurateDeviceCommand {

    // Singleton
    private static ConfigurateDeviceBluetoothCommand configurateDeviceBluetoothCommand;

    private ConfigurateDeviceBluetoothCommand(){

    }

    public static ConfigurateDeviceBluetoothCommand getInstance(){
        if (configurateDeviceBluetoothCommand == null){
            configurateDeviceBluetoothCommand = new ConfigurateDeviceBluetoothCommand();
        }

        return configurateDeviceBluetoothCommand;
    }

    @Override
    public void execute() {
        Log.i("configurationStarted", "configuration started");
        BluetoothGattService configurationService = BluetoothController.getInstance().getmGatt().getService(ServicesCharacteristicsBLE.SERVICE_GSCIS);

        if (configurationService != null) {

            BluetoothGattCharacteristic numberStoredConfCharacteristic = configurationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_NUMBER_STORED_CONF);
            BluetoothController.getInstance().getmGatt().readCharacteristic(numberStoredConfCharacteristic);

            Log.i("numberStoredConf", "number of stored configuration requested");
        }
    }
}
