package com.cgaldo.brais.sistema.Controller;

import android.content.Context;

import com.cgaldo.brais.sistema.Controller.Bluetooth.BluetoothController;
import com.cgaldo.brais.sistema.Model.Handler;

public interface ConnectionsController {

    BluetoothController connect(Context context);

    void unRegister(Context context);

    Boolean getConnected();

    void sendThresholds(String temperature, String humidity);

    // Put information
    Handler getInformation();

    // Perform actions
    void performAction(Integer action);

    // Set configuration to activate
    void configurationToActive(byte[] configuration);

}
