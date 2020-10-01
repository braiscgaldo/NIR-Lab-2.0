package com.cgaldo.brais.sistema.Controller.Bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.cgaldo.brais.sistema.Controller.Commands.RetrieveDeviceInformationCommand;
import com.cgaldo.brais.sistema.Model.StaticData.ServicesCharacteristicsBLE;

public class RetrieveDeviceInformationBluetoothCommand implements RetrieveDeviceInformationCommand {

    // Singleton
    private static RetrieveDeviceInformationBluetoothCommand retrieveDeviceInformationBluetoothCommand;

    private RetrieveDeviceInformationBluetoothCommand(){

    }

    public static RetrieveDeviceInformationBluetoothCommand getInstance(){
        if (retrieveDeviceInformationBluetoothCommand == null){
            retrieveDeviceInformationBluetoothCommand = new RetrieveDeviceInformationBluetoothCommand();
        }

        return retrieveDeviceInformationBluetoothCommand;
    }

    @Override
    public void execute() {
        BluetoothGattService serviceReceive = BluetoothController.getInstance().getmGatt().getService(ServicesCharacteristicsBLE.SERVICE_DIS);

        //Manufacture
        BluetoothGattCharacteristic manufCharacteristic = serviceReceive.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_MANUF_NUMBER);
        BluetoothController.getInstance().getmGatt().readCharacteristic(manufCharacteristic);
    }
}
