package com.cgaldo.brais.sistema.Controller.Bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.cgaldo.brais.sistema.Controller.Commands.ScanSampleCommand;
import com.cgaldo.brais.sistema.Model.StaticData.ServicesCharacteristicsBLE;

public class ScanSampleBluetoothCommand implements ScanSampleCommand {

    // Singleton
    private static ScanSampleBluetoothCommand scanSampleBluetoothCommand;

    private ScanSampleBluetoothCommand(){

    }

    public static ScanSampleBluetoothCommand getInstance(){
        if (scanSampleBluetoothCommand == null){
            scanSampleBluetoothCommand = new ScanSampleBluetoothCommand();
        }

        return scanSampleBluetoothCommand;
    }

    @Override
    public void execute() {
        Log.i("scanStarted", "Scan started");
        BluetoothGattService scanService = BluetoothController.getInstance().getmGatt().getService(ServicesCharacteristicsBLE.SERVICE_GSDIS);

        if (scanService != null) {

            BluetoothGattCharacteristic startScanCharacteristic = scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_START_SCAN);
            startScanCharacteristic.setValue(new byte[]{0x00});
            BluetoothController.getInstance().getmGatt().writeCharacteristic(startScanCharacteristic);

            Log.i("start Scan", "start scan requested");
        }
    }
}
