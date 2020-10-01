package com.cgaldo.brais.sistema.Controller.Bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.cgaldo.brais.sistema.Controller.Commands.CalibrateDeviceCommand;
import com.cgaldo.brais.sistema.Model.StaticData.ServicesCharacteristicsBLE;

public class CalibrateDeviceBluetoothCommand implements CalibrateDeviceCommand {

    // Singleton
    private static CalibrateDeviceBluetoothCommand calibrateDeviceBluetoothCommand;

    private CalibrateDeviceBluetoothCommand(){

    }

    public static CalibrateDeviceBluetoothCommand getInstance(){
        if (calibrateDeviceBluetoothCommand == null){
            calibrateDeviceBluetoothCommand = new CalibrateDeviceBluetoothCommand();
        }

        return calibrateDeviceBluetoothCommand;
    }

    @Override
    public void execute() {
        BluetoothGattService calibrationService = BluetoothController.getInstance().getmGatt().getService(ServicesCharacteristicsBLE.SERVICE_GCIS);

        if (calibrationService != null) {

            BluetoothGattCharacteristic spectrumCalibCoeffCharacteristic = calibrationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_REQUEST_SPEC_CALIB_COEF);
            spectrumCalibCoeffCharacteristic.setValue(new byte[]{0x00});
            BluetoothController.getInstance().getmGatt().writeCharacteristic(spectrumCalibCoeffCharacteristic);

            Log.i("requestSpecCalib", "spectrum calibration coefficients requested");
        }
    }
}
