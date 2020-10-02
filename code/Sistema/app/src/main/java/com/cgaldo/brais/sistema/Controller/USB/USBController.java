package com.cgaldo.brais.sistema.Controller.USB;

import android.content.Context;

import com.cgaldo.brais.sistema.Controller.Bluetooth.BluetoothController;
import com.cgaldo.brais.sistema.Controller.ConnectionsController;
import com.cgaldo.brais.sistema.Model.Handler;

public class USBController implements ConnectionsController {
    @Override
    public BluetoothController connect(Context context) {
        return null;
    }

    @Override
    public USBController connectUSB(Context context) {
        return null;
    }

    @Override
    public void unRegister(Context context) {

    }

    @Override
    public Boolean getConnected() {
        return null;
    }

    @Override
    public void sendThresholds(String temperature, String humidity) {

    }

    @Override
    public Handler getInformation() {
        return null;
    }

    @Override
    public void performAction(Integer action) {

    }

    @Override
    public void configurationToActive(byte[] configuration) {

    }
}
