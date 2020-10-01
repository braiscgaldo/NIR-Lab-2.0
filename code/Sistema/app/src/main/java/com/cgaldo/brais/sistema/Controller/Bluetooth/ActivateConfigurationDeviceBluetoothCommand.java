package com.cgaldo.brais.sistema.Controller.Bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.cgaldo.brais.sistema.Controller.Commands.ActivateConfigurationDeviceCommand;
import com.cgaldo.brais.sistema.Model.StaticData.ServicesCharacteristicsBLE;

public class ActivateConfigurationDeviceBluetoothCommand implements ActivateConfigurationDeviceCommand {

    private byte[] configurationToActive;

    // Singleton
    private static ActivateConfigurationDeviceBluetoothCommand activateConfigurationDeviceBluetoothCommand;

    private ActivateConfigurationDeviceBluetoothCommand(){

    }

    public static ActivateConfigurationDeviceBluetoothCommand getInstance(){
        if (activateConfigurationDeviceBluetoothCommand == null){
            activateConfigurationDeviceBluetoothCommand = new ActivateConfigurationDeviceBluetoothCommand();
        }

        return activateConfigurationDeviceBluetoothCommand;
    }

    public byte[] getConfigurationToActive() {
        return configurationToActive;
    }

    public void setConfigurationToActive(byte[] configurationToActive) {
        this.configurationToActive = configurationToActive;
    }

    @Override
    public void execute() {
        BluetoothGattService configurationService = BluetoothController.getInstance().getmGatt().getService(ServicesCharacteristicsBLE.SERVICE_GSCIS);

        if (configurationService != null){
            BluetoothGattCharacteristic activeConfigurationCharacteristic = configurationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_ACTIVE_SCAN_CONF);
            activeConfigurationCharacteristic.setValue(this.configurationToActive);
            BluetoothController.getInstance().getmGatt().writeCharacteristic(activeConfigurationCharacteristic);

            Log.i("requestSpecCalib", "spectrum calibration coefficients requested");
        }
    }
}
