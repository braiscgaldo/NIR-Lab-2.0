package com.cgaldo.brais.sistema.Controller.USB;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbRequest;
import android.os.Build;
import android.util.Log;

import com.cgaldo.brais.sistema.Controller.Commands.ResetDeviceCommand;
import com.cgaldo.brais.sistema.Model.StaticData.CommandsUSB;

import java.nio.ByteBuffer;

public class ResetDeviceUSBCommand implements ResetDeviceCommand {

    // Singleton
    private static ResetDeviceUSBCommand resetDeviceUSBCommand;

    private ResetDeviceUSBCommand(){

    }

    public static ResetDeviceUSBCommand getInstance(){
        if (resetDeviceUSBCommand == null){
            resetDeviceUSBCommand = new ResetDeviceUSBCommand();
        }

        return resetDeviceUSBCommand;
    }

    @Override
    public void execute() {
        // Obtaining correspondent interface, endpoint and connection
        UsbInterface usbInterface = USBController.getInstance().getUsbInterface();
        UsbDeviceConnection connection = USBController.getInstance().getUsbManager().openDevice(USBController.getInstance().getSpectrophotometer());
        UsbEndpoint outputEndpoint = usbInterface.getEndpoint(1);

        // obtain interface
        connection.claimInterface(usbInterface, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            Log.i("USB", "reset device....");
            /*
            int r;
            r = connection.bulkTransfer(outputEndpoint, CommandsUSB.RESET_DEVICE, CommandsUSB.RESET_DEVICE.length, 0);
            if (r > 0){
                Log.i("USB", "reset performed " + r);
            }else{
                Log.i("USB", "reset not performed " + r);
            }

             */
            //connection.controlTransfer(0xA1, 0x01, 0x00, 0x01, CommandsUSB.RESET_DEVICE, CommandsUSB.RESET_DEVICE.length, 0);
            String s = "";
            for(byte b : CommandsUSB.RESET_DEVICE){
                s += b;
            }
            Log.i("USB", "command sent: " + s);
            UsbRequest request = new UsbRequest();
            request.initialize(connection, outputEndpoint);
            request.queue(ByteBuffer.wrap(CommandsUSB.RESET_DEVICE));
            UsbRequest r = connection.requestWait();
            request.close();
            Log.i("USB", "requested data");
            Log.i("USB", "request: " + r.toString());

        }

        // realease interface
        connection.releaseInterface(usbInterface);
        connection.close();

    }
}
