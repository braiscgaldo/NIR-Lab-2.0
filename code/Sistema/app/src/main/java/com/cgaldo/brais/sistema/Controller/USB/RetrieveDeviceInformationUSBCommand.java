package com.cgaldo.brais.sistema.Controller.USB;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbRequest;
import android.os.Build;
import android.util.Log;

import com.cgaldo.brais.sistema.Controller.Commands.Command;
import com.cgaldo.brais.sistema.Controller.Commands.RetrieveDeviceInformationCommand;
import com.cgaldo.brais.sistema.Model.StaticData.Actions;
import com.cgaldo.brais.sistema.Model.StaticData.Broadcasts;
import com.cgaldo.brais.sistema.Model.StaticData.CommandsUSB;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class RetrieveDeviceInformationUSBCommand implements RetrieveDeviceInformationCommand {

    // Singleton
    private static RetrieveDeviceInformationUSBCommand retrieveDeviceInformationUSBCommand;

    private RetrieveDeviceInformationUSBCommand(){

    }

    public static RetrieveDeviceInformationUSBCommand getInstance(){
        if (retrieveDeviceInformationUSBCommand == null){
            retrieveDeviceInformationUSBCommand = new RetrieveDeviceInformationUSBCommand();
        }

        return retrieveDeviceInformationUSBCommand;
    }


    @Override
    public void execute() {
        try {
            // Obtaining correspondent interface, endpoint and connection
            UsbInterface usbInterface = USBController.getInstance().getUsbInterface();
            UsbDeviceConnection connection = USBController.getInstance().getUsbManager().openDevice(USBController.getInstance().getSpectrophotometer());
            UsbEndpoint inputEndpoint = usbInterface.getEndpoint(0);
            UsbEndpoint outputEndpoint = usbInterface.getEndpoint(1);

            // obtain interface
            connection.claimInterface(usbInterface, true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                /*
                UsbRequest request = new UsbRequest();
                request.initialize(connection, outputEndpoint);
                //request.setClientData(ByteBuffer.wrap(CommandsUSB.READ_REVISIONS));
                request.queue(ByteBuffer.wrap(CommandsUSB.READ_REVISIONS));
                Log.i("USB", "Sended command" );
                byte[] result = new byte[28];
                UsbRequest resultRequest = new UsbRequest();
                resultRequest.initialize(connection, inputEndpoint);
                resultRequest.queue(ByteBuffer.wrap(result));
                Log.i("USB", "queuing result");
                UsbRequest res = connection.requestWait();
                int bufferLength = 7;
                byte[] resByte = new byte[28];
                ByteBuffer bb = (ByteBuffer)res.getClientData();
                for (int i = 0; i < bufferLength; i++){
                    resByte[i] = bb.array()[i];
                }*/
                UsbRequest request = new UsbRequest();
                request.initialize(connection, outputEndpoint);
                //request.setClientData(ByteBuffer.wrap(CommandsUSB.READ_REVISIONS));
                request.queue(ByteBuffer.wrap(CommandsUSB.READ_REVISIONS));
                connection.requestWait();
                Log.i("USB", "first transaction");
                connection.releaseInterface(usbInterface);
                connection.close();
                connection.claimInterface(usbInterface, true);
                byte[] resByte = new byte[28];
                connection.bulkTransfer(inputEndpoint, resByte, resByte.length, 0);

                Log.i("USB", "result: bytes " + Arrays.toString(resByte));
                //Log.i("USB", "result: " + res.getClientData().toString());
            }

            // realease interface
            connection.releaseInterface(usbInterface);
            connection.close();

            //USBController.getInstance().sendBroadcast(Broadcasts.FINISH_INFORMATION);

        } catch (Exception e){
            Log.i("USB", "error reading data " + e.toString());
        }
    }
}
