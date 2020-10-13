package com.cgaldo.brais.sistema.Controller.USB;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.cgaldo.brais.sistema.Controller.Bluetooth.BluetoothController;
import com.cgaldo.brais.sistema.Controller.ConnectionsController;
import com.cgaldo.brais.sistema.Controller.Invoker;
import com.cgaldo.brais.sistema.Model.Handler;

public class USBController implements ConnectionsController {

    //Singleton
    private static USBController usbController;

    //Methods Singleton
    private USBController() {

    }

    public static USBController getInstance() {
        if (usbController == null) {
            usbController = new USBController();
        }
        return usbController;
    }

    // Atributtes
    private UsbManager usbManager;
    private Invoker invoker = Invoker.getInstance(RetrieveDeviceInformationUSBCommand.getInstance(), RetrieveDeviceStatusUSBCommand.getInstance(),
            CalibrateDeviceUSBCommand.getInstance(), ScanSampleUSBCommand.getInstance(), ConfigurateDeviceUSBCommand.getInstance(),
            ResetDeviceUSBCommand.getInstance(), ActivateConfigurationDeviceUSBCommand.getInstance());
    private byte[] configurationToActive;

    @Override
    public BluetoothController connect(Context context) {
        return null;
    }

    @Override
    public USBController connectUSB(Context context) {
        this.usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        Log.i("USB", this.usbManager.getDeviceList().toString());
        return this.usbController;
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
        // We select the action required
        switch (action) {
            case 1001:
                invoker.retrieveDeviceInformation();
                break;
            case 1002:
                invoker.retrieveDeviceStatus();
                break;
            case 1003:
                invoker.calibrateDevice();
                break;
            case 1004:
                invoker.scanSample();
                break;
            case 1005:
                invoker.configurateDevice();
                break;
            case 1006:
                invoker.resetDevice();
                break;
            case 1007:
                ActivateConfigurationDeviceUSBCommand.getInstance().setConfigurationToActive(configurationToActive);
                invoker.activateConfiguration();
                break;
            default:
                Log.i("Not supported", "action not supported");
                break;
        }
    }

    @Override
    public void configurationToActive(byte[] configuration) {

    }

}
