package com.cgaldo.brais.sistema.Controller.USB;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.cgaldo.brais.sistema.Controller.Bluetooth.BluetoothController;
import com.cgaldo.brais.sistema.Controller.ConnectionsController;
import com.cgaldo.brais.sistema.Controller.Invoker;
import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.StaticData.CommandsUSB;

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

    private LocalBroadcastManager localBroadcastManager;

    private UsbManager usbManager;
    private Invoker invoker = Invoker.getInstance(RetrieveDeviceInformationUSBCommand.getInstance(), RetrieveDeviceStatusUSBCommand.getInstance(),
            CalibrateDeviceUSBCommand.getInstance(), ScanSampleUSBCommand.getInstance(), ConfigurateDeviceUSBCommand.getInstance(),
            ResetDeviceUSBCommand.getInstance(), ActivateConfigurationDeviceUSBCommand.getInstance());
    private byte[] configurationToActive;

    private UsbDevice spectrophotometer;

    private UsbInterface usbInterface;

    public UsbInterface getUsbInterface() {
        return usbInterface;
    }

    public UsbManager getUsbManager() {
        return usbManager;
    }

    public UsbDevice getSpectrophotometer() {
        return spectrophotometer;
    }

    @Override
    public BluetoothController connect(Context context) {
        return null;
    }

    @Override
    public USBController connectUSB(Activity activity) {
        // We set up the local broadcast manager
        this.localBroadcastManager = LocalBroadcastManager.getInstance(activity);
        this.usbManager = (UsbManager) activity.getSystemService(Context.USB_SERVICE);
        Log.i("USB", this.usbManager.getDeviceList().toString());
        this.spectrophotometer = this.usbManager.getDeviceList().get(CommandsUSB.SPECTROMETER_NAME_DIRECTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i("USB", "information\n manufacture name: " + this.spectrophotometer.getManufacturerName() + "\n product name: " + this.spectrophotometer.getProductName() + "\n serial number: " + this.spectrophotometer.getSerialNumber() + "\n version: " + this.spectrophotometer.getVersion());

        }
        this.usbInterface = this.spectrophotometer.getInterface(0); // It only has one interface
        Log.i("USB", "interface: " + this.usbInterface.toString());
        return this.usbController;
    }

    @Override
    public void unRegister(Context context) {

    }

    @Override
    public Boolean getConnected() {
        return true;
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

    public int sendCommand(byte [] command, UsbDeviceConnection connection){

        return connection.controlTransfer(0xA1, 0x01, 0x00, 0x01, command, command.length, 0);
        //return connection.bulkTransfer(usbInterface.getEndpoint(1), command, command.length, 0);
    }

    @Override
    public void configurationToActive(byte[] configuration) {

    }

    public void sendBroadcast(String action){
        Intent intent = new Intent();
        intent.setAction(action);
        localBroadcastManager.sendBroadcast(intent);
    }

}
