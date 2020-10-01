package com.cgaldo.brais.sistema.Controller.Bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.cgaldo.brais.sistema.Controller.Commands.RetrieveDeviceStatusCommand;
import com.cgaldo.brais.sistema.Model.StaticData.ServicesCharacteristicsBLE;

public class RetrieveDeviceStatusBluetoothCommand implements RetrieveDeviceStatusCommand {

    //Singleton
    private static RetrieveDeviceStatusBluetoothCommand retrieveDeviceStatusBluetoothCommand;

    private RetrieveDeviceStatusBluetoothCommand(){

    }

    public static RetrieveDeviceStatusBluetoothCommand getInstance(){
        if (retrieveDeviceStatusBluetoothCommand == null){
            retrieveDeviceStatusBluetoothCommand = new RetrieveDeviceStatusBluetoothCommand();
        }

        return retrieveDeviceStatusBluetoothCommand;
    }

    @Override
    public void execute() {
        BluetoothGattService serviceBasReceive = BluetoothController.getInstance().getmGatt().getService(ServicesCharacteristicsBLE.SERVICE_BAS);

        //Battery
        BluetoothGattCharacteristic batteryCharacteristic = serviceBasReceive.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_BATTERY_LEVEL);
        BluetoothController.getInstance().getmGatt().readCharacteristic(batteryCharacteristic);
    }

}
