package com.cgaldo.brais.sistema.Controller.Bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.cgaldo.brais.sistema.Controller.Commands.ResetDeviceCommand;
import com.cgaldo.brais.sistema.Model.StaticData.CommandSpectrometer;
import com.cgaldo.brais.sistema.Model.StaticData.ServicesCharacteristicsBLE;

import java.util.Arrays;

public class ResetDeviceBluetoothCommand implements ResetDeviceCommand {

    // Singleton
    private static ResetDeviceBluetoothCommand resetDeviceBluetoothCommand;

    private ResetDeviceBluetoothCommand(){

    }

    public static ResetDeviceBluetoothCommand getInstance(){
        if (resetDeviceBluetoothCommand == null){
            resetDeviceBluetoothCommand = new ResetDeviceBluetoothCommand();
        }

        return resetDeviceBluetoothCommand;
    }

    @Override
    public void execute() {
        byte resetCommand[] = new byte[4];
        byte dataSize[] = {0x00};
        System.arraycopy(CommandSpectrometer.RESET, 0, resetCommand, 0, 3);
        System.arraycopy(dataSize, 0, resetCommand, 3, 1);


        //Send command
        BluetoothGattService serviceSend = BluetoothController.getInstance().getmGatt().getService(ServicesCharacteristicsBLE.SERVICE_COMMAND);
        if (serviceSend != null) {
            BluetoothGattCharacteristic commandChar = serviceSend.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_COMMAND_SEND);
            commandChar.setValue(resetCommand);
            BluetoothController.getInstance().getmGatt().writeCharacteristic(commandChar);

            Log.i("sendReset", "Reset send" + Arrays.toString(commandChar.getValue()));
        }
    }
}
