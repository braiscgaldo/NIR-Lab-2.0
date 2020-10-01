package com.cgaldo.brais.sistema.Controller.Bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.cgaldo.brais.sistema.Controller.ConnectionsController;
import com.cgaldo.brais.sistema.Controller.Invoker;
import com.cgaldo.brais.sistema.Model.DeviceInformation.DeviceInformationInfo;
import com.cgaldo.brais.sistema.Model.DeviceInformation.DeviceStatusInfo;
import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.PostProcessedData.ScanInformation;
import com.cgaldo.brais.sistema.Model.PreProcessed.PreScanConfigurationInformationData;
import com.cgaldo.brais.sistema.Model.PreProcessed.PreScanDataInformation;
import com.cgaldo.brais.sistema.Model.PreProcessed.ScanCalibrationData;
import com.cgaldo.brais.sistema.Model.ProcessedData.ScanConfigurationData;
import com.cgaldo.brais.sistema.Model.ProcessedData.ScanData;
import com.cgaldo.brais.sistema.Model.StaticData.Broadcasts;
import com.cgaldo.brais.sistema.Model.StaticData.ObjectsRequired;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;
import com.cgaldo.brais.sistema.Model.StaticData.ServicesCharacteristicsBLE;
import com.cgaldo.brais.sistema.R;
import com.cgaldo.brais.sistema.View.Fragments.MainFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_SINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE;
import static android.bluetooth.BluetoothGattDescriptor.PERMISSION_READ;
import static android.bluetooth.BluetoothGattDescriptor.PERMISSION_WRITE;
import static android.bluetooth.BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED;
import static android.bluetooth.BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED_MITM;
import static android.bluetooth.BluetoothGattService.SERVICE_TYPE_PRIMARY;
import static android.content.Context.BLUETOOTH_SERVICE;

public class BluetoothController implements ConnectionsController {

    //Attributes
    private final static int REQUEST_ENABLE_BT = 1001; // used to identify adding bluetooth names

    //UUIDs
    private final static UUID SERVICE_UUID = UUID.fromString("795090c7-420d-4048-a24e-18e60180e23c");
    private final static UUID CHARACTERISTIC_COUNTER_UUID = UUID.fromString("31517c58-66bf-470c-b662-e352a6c80cba");
    private final static UUID CHARACTERISTIC_INTERACTOR_UUID = UUID.fromString("0b89d2d4-0ea6-4141-86bb-0c5fb91ab14a");
    private final static UUID DESCRIPTOR_CONFIG_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    //Bluetooth
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> addressDevices;
    private List<String> btDevices;
    private BluetoothAdapter bluetoothAdapter;


    private BluetoothDevice bluetoothDevice;

    private Boolean connected = false;
    private Boolean attemptsToConnect = true;

    private AdvertiseSettings settings = new AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
            .setConnectable(true)
            .setTimeout(0)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
            .build();

    // Defines which service to advertise.
    private AdvertiseData data = new AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .setIncludeTxPowerLevel(false)
            .addServiceUuid(new ParcelUuid(SERVICE_UUID))
            .build();


    private BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
        }
    };


    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {


        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {

                connected = true;

                // Attempts to discover services after successful connection.
                Log.i("BLE", "Connected to GATT server");


            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                connected = false;
                Log.i("BLE", "Disconnected from GATT server.");
            }
        }


        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BLE", "Services discovered:SUCCESS");

                //Once we discover services, we must enable the notifications for the characteristics that we need
                //For doing that, first we call to one of them and we write her descriptor
                //Then, we must following a sequence for setting all of them
                //It is important to know that the order is not important
                //Scan service
                BluetoothGattService scanService = BluetoothController.getInstance().mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GSDIS);


                if (scanService != null) {
                    //Start scan characteristic
                    BluetoothGattCharacteristic startScanCharacteristic = scanService.getCharacteristics().get(5);
                    mGatt.setCharacteristicNotification(startScanCharacteristic, true);

                    //We use the descriptor to set the value
                    BluetoothGattDescriptor startScanDescriptor = startScanCharacteristic.getDescriptor(DESCRIPTOR_CONFIG_UUID);
                    startScanDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

                    mGatt.writeDescriptor(startScanDescriptor);
                    Log.i("notification", "scan notification enabled");


                }


            } else {
                Log.e("BLE", "onServicesDiscovered: NOT DISCOVERED ");
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor descriptor,
                                      int status) {
            //Into this function we must start a sequence for set all the characteristic notification

            //First, we must declare all services
            BluetoothGattService scanService = BluetoothController.getInstance().mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GSDIS);
            BluetoothGattService calibrationService = BluetoothController.getInstance().mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GCIS);
            BluetoothGattService configurationService = BluetoothController.getInstance().mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GSCIS);

            //First descriptor is the startScanDescriptor
            if (descriptor.equals(scanService.getCharacteristics().get(5).getDescriptor(DESCRIPTOR_CONFIG_UUID))) {
                Log.i("onDescriptorWrite", "enable notification for startScanDescriptor");

                //Then, we must call another descriptor to be written
                //Return scan characteristic
                BluetoothGattCharacteristic returnScanCharacteristic = scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_DATA);
                mGatt.setCharacteristicNotification(returnScanCharacteristic, true);

                //We use the descriptor to set the value
                BluetoothGattDescriptor returnScanDescriptor = returnScanCharacteristic.getDescriptor(DESCRIPTOR_CONFIG_UUID);
                returnScanDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mGatt.writeDescriptor(returnScanDescriptor);


            } else if (descriptor.equals(scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_DATA).getDescriptor(DESCRIPTOR_CONFIG_UUID))) {
                Log.i("onDescriptorWrite", "enable notification for returnScanDescriptor");

                //Then, we must call another descriptor
                //Return spectrum calibration coefficients characteristic
                BluetoothGattCharacteristic returnSpecCalibCoefCharacteristic = calibrationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_SPEC_CALIB_COEF);
                mGatt.setCharacteristicNotification(returnSpecCalibCoefCharacteristic, true);

                //We use the descriptor to set the value
                BluetoothGattDescriptor returnSpecCalibCoefDescriptor = returnSpecCalibCoefCharacteristic.getDescriptor(DESCRIPTOR_CONFIG_UUID);
                returnSpecCalibCoefDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mGatt.writeDescriptor(returnSpecCalibCoefDescriptor);

            } else if (descriptor.equals(calibrationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_SPEC_CALIB_COEF).getDescriptor(DESCRIPTOR_CONFIG_UUID))) {
                Log.i("onDescriptorWrite", "enable notification for returnSpecCalibCoefDescriptor");

                //Then, we must call another descriptor
                //Return reference calibration coefficients characteristic
                BluetoothGattCharacteristic returnRefCalibCoefCharacteristic = calibrationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_REF_CALIB_COEF);
                mGatt.setCharacteristicNotification(returnRefCalibCoefCharacteristic, true);

                //Then, we must call another descriptor
                //We use the descriptor to set the value
                BluetoothGattDescriptor returnRefCalibCoefDescriptor = returnRefCalibCoefCharacteristic.getDescriptor(DESCRIPTOR_CONFIG_UUID);
                returnRefCalibCoefDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mGatt.writeDescriptor(returnRefCalibCoefDescriptor);

            } else if (descriptor.equals(calibrationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_REF_CALIB_COEF).getDescriptor(DESCRIPTOR_CONFIG_UUID))) {
                Log.i("onDescriptorWrite", "enable notification for returnRefCalibCoefDescriptor");

                //Then, we must call another descriptor
                //Return reference calibration matrix characteristic
                BluetoothGattCharacteristic returnRefCalibMatrixCharacteristic = calibrationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_REF_CALIB_MATRIX);
                mGatt.setCharacteristicNotification(returnRefCalibMatrixCharacteristic, true);

                //We use the descriptor to set the value
                BluetoothGattDescriptor returnRefCalibMatrixDescriptor = returnRefCalibMatrixCharacteristic.getDescriptor(DESCRIPTOR_CONFIG_UUID);
                returnRefCalibMatrixDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mGatt.writeDescriptor(returnRefCalibMatrixDescriptor);

            } else if (descriptor.equals(calibrationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_REF_CALIB_MATRIX).getDescriptor(DESCRIPTOR_CONFIG_UUID))) {
                Log.i("onDescriptorWrite", "enable notification for returnRefCalibMatrixDescriptor");

                //Then, we must call another descriptor
                //Return stored configuration list characteristic
                BluetoothGattCharacteristic returnStoredConfigurationListCharacteristic = configurationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_STORED_CONF_LIST);
                mGatt.setCharacteristicNotification(returnStoredConfigurationListCharacteristic, true);

                //We use the descriptor to set the value
                BluetoothGattDescriptor returnStoredConfigurationListDescriptor = returnStoredConfigurationListCharacteristic.getDescriptor(DESCRIPTOR_CONFIG_UUID);
                returnStoredConfigurationListDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mGatt.writeDescriptor(returnStoredConfigurationListDescriptor);

            } else if (descriptor.equals(configurationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_STORED_CONF_LIST).getDescriptor(DESCRIPTOR_CONFIG_UUID))){
                Log.i("onDescriptorWrite", "enable notification for returnStoredConfigurationListDescriptor");

                //Then, we must call another descriptor
                //Return scan configuration data characteristic
                BluetoothGattCharacteristic returnScanConfigurationDataCharacteristic = configurationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_SCAN_CONF_DATA);
                mGatt.setCharacteristicNotification(returnScanConfigurationDataCharacteristic, true);

                //We use the descriptor to set the value
                BluetoothGattDescriptor returnScanConfigurationDataDescriptor = returnScanConfigurationDataCharacteristic.getDescriptor(DESCRIPTOR_CONFIG_UUID);
                returnScanConfigurationDataDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mGatt.writeDescriptor(returnScanConfigurationDataDescriptor);
            } else if (descriptor.equals(configurationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_SCAN_CONF_DATA).getDescriptor(DESCRIPTOR_CONFIG_UUID))){
                Log.i("onDescriptorWrite", "enable notification for returnScanConfigurationDataDescriptor");
                //Then, we must call another descriptor to be written
                BluetoothGattCharacteristic returnNameScanCharacteristic = scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_SCAN_NAME);
                mGatt.setCharacteristicNotification(returnNameScanCharacteristic, true);

                //We use the descriptor to set the value
                BluetoothGattDescriptor returnScanDescriptor = returnNameScanCharacteristic.getDescriptor(DESCRIPTOR_CONFIG_UUID);
                returnScanDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mGatt.writeDescriptor(returnScanDescriptor);

            } else if (descriptor.equals(scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_SCAN_NAME).getDescriptor(DESCRIPTOR_CONFIG_UUID))){
                Log.i("onDescriptorWrite", "enable notification for returnNameScanDescriptor");
                //Then, we must call another descriptor to be written
                BluetoothGattCharacteristic returnScanTypeCharacteristic = scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_SCAN_TYPE);
                mGatt.setCharacteristicNotification(returnScanTypeCharacteristic, true);

                //We use the descriptor to set the value
                BluetoothGattDescriptor returnScanDescriptor = returnScanTypeCharacteristic.getDescriptor(DESCRIPTOR_CONFIG_UUID);
                returnScanDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mGatt.writeDescriptor(returnScanDescriptor);

            } else if (descriptor.equals(scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_SCAN_TYPE).getDescriptor(DESCRIPTOR_CONFIG_UUID))) {
                Log.i("onDescriptorWrite", "enable notification for returnScanTypeDescriptor");
                //Then, we must call another descriptor to be written
                BluetoothGattCharacteristic returnScanDateTimeCharacteristic = scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_SCAN_DATE_TIME);
                mGatt.setCharacteristicNotification(returnScanDateTimeCharacteristic, true);

                //We use the descriptor to set the value
                BluetoothGattDescriptor returnScanDescriptor = returnScanDateTimeCharacteristic.getDescriptor(DESCRIPTOR_CONFIG_UUID);
                returnScanDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mGatt.writeDescriptor(returnScanDescriptor);

            } else if (descriptor.equals(scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_SCAN_DATE_TIME).getDescriptor(DESCRIPTOR_CONFIG_UUID))) {
                Log.i("onDescriptorWrite", "enable notification for returnScanDateTimeDescriptor");
                //Then, we must call another descriptor to be written
                BluetoothGattCharacteristic returnPacketFormatCharacteristic = scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_PACKET_FORMAT_VERSION);
                mGatt.setCharacteristicNotification(returnPacketFormatCharacteristic, true);

                //We use the descriptor to set the value
                BluetoothGattDescriptor returnScanDescriptor = returnPacketFormatCharacteristic.getDescriptor(DESCRIPTOR_CONFIG_UUID);
                returnScanDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mGatt.writeDescriptor(returnScanDescriptor);
            } else if (descriptor.equals(scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_PACKET_FORMAT_VERSION).getDescriptor(DESCRIPTOR_CONFIG_UUID))) {
                Log.i("onDescriptorWrite", "enable notification for returnPacketFormatDescriptor");
                //Then, we must call another descriptor to be written
                BluetoothGattCharacteristic clearScanCharacteristic = scanService.getCharacteristics().get(7);
                mGatt.setCharacteristicNotification(clearScanCharacteristic, true);

                //We use the descriptor to set the value
                BluetoothGattDescriptor returnClearDescriptor = clearScanCharacteristic.getDescriptor(DESCRIPTOR_CONFIG_UUID);
                returnClearDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mGatt.writeDescriptor(returnClearDescriptor);

            } else if (descriptor.equals((scanService.getCharacteristics().get(7).getDescriptor(DESCRIPTOR_CONFIG_UUID)))){
                Log.i("onDescriptorWrite", "enable notification for clearScanCharacteristic");

                //We notify that we are successfully connected
                sendBroadcast(Broadcasts.FINISH_CONNECTION);

            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {

            BluetoothGattService deviceInformationService = mGatt.getService(ServicesCharacteristicsBLE.SERVICE_DIS);
            BluetoothGattService generalInformationService = mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GGIS);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i("onCharRead", "status OK");

                if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_MANUF_NUMBER)) {
                    Log.i("charRead", "manufacture number");
                    information.handleSetRequest(Requests.DEVICE_INFORMATION, ObjectsRequired.MANUFACTURE, characteristic.getStringValue(0));

                    //We read from another characteristic
                    BluetoothGattCharacteristic modelNumberCharacteristic = deviceInformationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_MODEL_NUMBER);
                    mGatt.readCharacteristic(modelNumberCharacteristic);

                } else if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_MODEL_NUMBER)) {
                    Log.i("charRead", "model number");
                    information.handleSetRequest(Requests.DEVICE_INFORMATION, ObjectsRequired.MODEL_NUMBER, characteristic.getStringValue(0));

                    //We read from another characteristic
                    BluetoothGattCharacteristic serialNumberCharacteristic = deviceInformationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_SERIAL_NUMBER);
                    mGatt.readCharacteristic(serialNumberCharacteristic);

                } else if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_SERIAL_NUMBER)) {
                    Log.i("charRead", "serial number");
                    information.handleSetRequest(Requests.DEVICE_INFORMATION, ObjectsRequired.SERIAL_NUMBER, characteristic.getStringValue(0));

                    //We read from another characteristic
                    BluetoothGattCharacteristic hardwareRevCharacteristic = deviceInformationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_HARDWARE_REVISION);
                    mGatt.readCharacteristic(hardwareRevCharacteristic);

                } else if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_HARDWARE_REVISION)) {
                    Log.i("charRead", "hardware revision");
                    information.handleSetRequest(Requests.DEVICE_INFORMATION, ObjectsRequired.HARDWARE_REVISION, characteristic.getStringValue(0));

                    //We read from another characteristic
                    BluetoothGattCharacteristic tivaFirmwareCharacteristic = deviceInformationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_TIVA_FIRMWARE_REVISION);
                    mGatt.readCharacteristic(tivaFirmwareCharacteristic);

                } else if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_TIVA_FIRMWARE_REVISION)) {
                    Log.i("charRead", "tiva firmware");
                    information.handleSetRequest(Requests.DEVICE_INFORMATION, ObjectsRequired.TIVA_FIRMWARE_REVISION, characteristic.getStringValue(0));

                    //We read from another characteristic
                    BluetoothGattCharacteristic specLibraryCharacteristic = deviceInformationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_SPECTRUM_LIBRARY_REVISION);
                    mGatt.readCharacteristic(specLibraryCharacteristic);

                } else if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_SPECTRUM_LIBRARY_REVISION)) {
                    Log.i("charRead", "spectrum library");
                    information.handleSetRequest(Requests.DEVICE_INFORMATION, ObjectsRequired.SPECTRUM_LIBRARY_REVISION, characteristic.getStringValue(0));

                    //We advertise that device information is available
                    sendBroadcast(Broadcasts.FINISH_INFORMATION);


                } else if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_BATTERY_LEVEL)) {
                    Log.i("charRead", "battery level");
                    information.handleSetRequest(Requests.DEVICE_STATUS, ObjectsRequired.BATTERY_LEVEL, characteristic.getIntValue(FORMAT_UINT8, 0));

                    //We read from another characteristic
                    BluetoothGattCharacteristic temperatureCharacteristic = generalInformationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_TEMPERATURE_MEASUREMENT_RECEIVE);
                    mGatt.readCharacteristic(temperatureCharacteristic);

                } else if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_TEMPERATURE_MEASUREMENT_RECEIVE)) {
                    Log.i("charRead", "temperature level");
                    information.handleSetRequest(Requests.DEVICE_STATUS, ObjectsRequired.TEMPERATURE_LEVEL, characteristic.getIntValue(FORMAT_SINT16, 0));

                    //We read from another characteristic
                    BluetoothGattCharacteristic humidityCharacteristic = generalInformationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_HUMIDITY_MEASUREMENT_RECEIVE);
                    mGatt.readCharacteristic(humidityCharacteristic);

                } else if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_HUMIDITY_MEASUREMENT_RECEIVE)) {
                    Log.i("charRead", "humidity level");
                    information.handleSetRequest(Requests.DEVICE_STATUS, ObjectsRequired.HUMIDITY_LEVEL, characteristic.getIntValue(FORMAT_UINT16, 0));

                    // We advertise that device status is available
                    sendBroadcast(Broadcasts.FINISH_STATUS);

                } else if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_NUMBER_STORED_CONF)){
                    Log.i("charRead", "number stored configurations");
                    information.handleSetRequest(Requests.DEVICE_PRE_SCAN_CONFIGURATIONS, ObjectsRequired.NUMBER_STORED_CONFIGURATION, characteristic.getIntValue(FORMAT_UINT16, 0));
                    //Log.i("storedConfig", "number stored config: " + preScanConfigurationInformationData.getNumberStoredConfiguration().toString());

                    //We ask for the stored configurations
                    getStoredConfList();

                } else if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_ACTIVE_SCAN_CONF)){
                    Log.i("activeScan", "active scan readed");

                    //We obtained the correspondent data
                    getScanConfData(characteristic.getValue());
                }

                Log.i("onCharacteristicRead", "readCharacteristic");
            }


        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic,
                                          int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i("onCharWrite", "status OK");
                if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_REQUEST_REF_CALIB_COEF)) {
                    Log.i("calibCoefReq", "calibration requested with " + characteristic.getIntValue(FORMAT_UINT8, 0));
                }else if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_ACTIVE_SCAN_CONF)){
                    Log.i("scanConf", "scanConfigurationActivated");
                    sendBroadcast(Broadcasts.FINISH_ACTIVATE_CONFIGURATION);
                }
            }

            Log.i("onCharacteristicWrite", "char writed " + status);


        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            Log.i("onCharacteristicChanged", "change characteristic");

            //Scan
            if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_START_SCAN)) {
                Log.i("startScan", "start scan requested");
                //We reset the previous scan index
                ByteArrayOutputStream buffer = (ByteArrayOutputStream) information.handleGetRequest(Requests.DEVICE_PRE_SCAN, ObjectsRequired.SCAN_INDEX);
                buffer.reset();

                byte[] valueCharacteristic = characteristic.getValue();

                if (finishScan(valueCharacteristic)) {
                    for (int i = 1; i < valueCharacteristic.length; i++) {
                        buffer.write(valueCharacteristic[i]);
                    }

                    getNameScan(buffer.toByteArray());
                }
            }

            if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_SCAN_NAME)){
                Log.i("nameScan", "name scan requested");
                information.handleSetRequest(Requests.DEVICE_PRE_SCAN, ObjectsRequired.NAME_SCAN, characteristic.getStringValue(0));

                ByteArrayOutputStream buffer = (ByteArrayOutputStream) information.handleGetRequest(Requests.DEVICE_PRE_SCAN, ObjectsRequired.SCAN_INDEX);
                getScanType(buffer.toByteArray());

            }

            if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_SCAN_TYPE)){
                Log.i("typeScan", "type scan requested");
                //We reset the previous scan index
                ByteArrayOutputStream buffer = (ByteArrayOutputStream) information.handleGetRequest(Requests.DEVICE_PRE_SCAN, ObjectsRequired.TYPE_SCAN);
                buffer.reset();

                byte[] valueCharacteristic = characteristic.getValue();

                for (byte aValueCharacteristic : valueCharacteristic) {
                    buffer.write(aValueCharacteristic);
                }

                buffer = (ByteArrayOutputStream) information.handleGetRequest(Requests.DEVICE_PRE_SCAN, ObjectsRequired.SCAN_INDEX);

                getScanDateTime(buffer.toByteArray());

            }

            if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_SCAN_DATE_TIME)){
                Log.i("dateTime", "date time requested");
                //We reset the previous scan buffer
                ByteArrayOutputStream buffer = (ByteArrayOutputStream) information.handleGetRequest(Requests.DEVICE_PRE_SCAN, ObjectsRequired.DATE_TIME_SCAN);
                buffer.reset();

                byte[] valueCharacteristic = characteristic.getValue();

                for (byte aValueCharacteristic : valueCharacteristic) {
                    buffer.write(aValueCharacteristic);
                }

                buffer = (ByteArrayOutputStream) information.handleGetRequest(Requests.DEVICE_PRE_SCAN, ObjectsRequired.SCAN_INDEX);

                getPacketFormat(buffer.toByteArray());

            }

            if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_PACKET_FORMAT_VERSION)){
                Log.i("packetFormat", "packet format requested");
                //We reset the buffer
                ByteArrayOutputStream buffer = (ByteArrayOutputStream) information.handleGetRequest(Requests.DEVICE_PRE_SCAN, ObjectsRequired.PACKET_FORMAT_SCAN);

                byte[] valueCharacteristic = characteristic.getValue();

                for (byte aValueCharacteristic : valueCharacteristic) {
                    buffer.write(aValueCharacteristic);
                }

                buffer = (ByteArrayOutputStream) information.handleGetRequest(Requests.DEVICE_PRE_SCAN, ObjectsRequired.SCAN_INDEX);

                getSerializedData(buffer.toByteArray());

            }

            if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_DATA)) {
                Log.i("scanData", "Return serialized data");
                requestData(characteristic, Requests.DEVICE_PRE_SCAN, ObjectsRequired.DATA_SCAN_SIZE, ObjectsRequired.DATA_SCAN);


            }

            if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_CLEAR_SCAN)){
                Log.i("clearScan", "clear scan");

                for (byte b : characteristic.getValue()){
                    Log.i("scanClear", "scan cleared: " + b);
                }

            }

            //Calibration
            if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_SPEC_CALIB_COEF)) {
                Log.i("scanCalibration", "Return spectrum calibration coefficients");
                requestData(characteristic, Requests.DEVICE_CALIBRATION, ObjectsRequired.SPECTRUM_CALIB_COEFF_SIZE, ObjectsRequired.SPECTRUM_CALIB_COEFF);

            }

            if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_REF_CALIB_COEF)) {
                Log.i("scanCalibration", "Return reference calibration coefficients");
                requestData(characteristic, Requests.DEVICE_CALIBRATION, ObjectsRequired.REFERENCE_CALIB_COEFF_SIZE, ObjectsRequired.REFERENCE_CALIB_COEFF);

            }

            if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_REF_CALIB_MATRIX)) {
                Log.i("scanCalibration", "Return reference calibration matrix");
                requestData(characteristic, Requests.DEVICE_CALIBRATION, ObjectsRequired.REFERENCE_CALIB_MATRIX_SIZE, ObjectsRequired.REFERENCE_CALIB_MATRIX);


            }

            //Configuration
            if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_STORED_CONF_LIST)) {
                Log.i("scanConfiguration", "Return stored configuration list");
                requestData(characteristic, Requests.DEVICE_PRE_SCAN_CONFIGURATIONS, ObjectsRequired.STORED_CONF_LIST_SIZE, ObjectsRequired.STORED_CONF_LIST);


            }

            if (characteristic.getUuid().equals(ServicesCharacteristicsBLE.CHARACTERISTIC_RETURN_SCAN_CONF_DATA)) {
                Log.i("scanConfiguration", "Return scan configuration data");
                requestData(characteristic, Requests.DEVICE_PRE_SCAN_CONFIGURATIONS, ObjectsRequired.SCAN_CONF_DATA_SIZE, ObjectsRequired.SCAN_CONF_DATA);


            }


        }

    };


    private AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            Log.i("BLE", "LE Advertise Started.");
            BluetoothController.getInstance().connected = true;
        }

        @Override
        public void onStartFailure(int errorCode) {
            Log.w("BLE", "LE Advertise Failed: " + errorCode);
            BluetoothController.getInstance().connected = false;
        }
    };

    private BluetoothGatt mGatt;

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                if (device.getName() != null)
                    if (!btDevices.contains(device.getName() + "\n" + device.getAddress()) && device.getName().equals("NIRScanNano")) {
                        arrayAdapter.add(device.getName() + "\n" + device.getAddress());
                        addressDevices.add(device.getAddress());
                    }
            }
        }
    };

    // Handler
    private Handler information = DeviceInformationInfo.getInstance(DeviceStatusInfo.getInstance(ScanCalibrationData.getInstance(PreScanDataInformation.getInstance(PreScanConfigurationInformationData.getInstance(ScanData.getInstance(ScanConfigurationData.getInstance(ScanInformation.getInstance())))))));

    //Singleton
    private static BluetoothController bluetoothController;

    // Commands
    private Invoker invoker = Invoker.getInstance(RetrieveDeviceInformationBluetoothCommand.getInstance(), RetrieveDeviceStatusBluetoothCommand.getInstance(),
            CalibrateDeviceBluetoothCommand.getInstance(), ScanSampleBluetoothCommand.getInstance(), ConfigurateDeviceBluetoothCommand.getInstance(),
            ResetDeviceBluetoothCommand.getInstance(), ActivateConfigurationDeviceBluetoothCommand.getInstance());

    private byte[] configurationToActive;

    // Broadcast Manager
    private LocalBroadcastManager localBroadcastManager;

    //Methods Singleton
    private BluetoothController() {

    }

    public static BluetoothController getInstance() {
        if (bluetoothController == null) {
            bluetoothController = new BluetoothController();
        }
        return bluetoothController;
    }

    public void clearScan(byte[] index) {
        Log.i("scanFinished", "Scan finished");
        BluetoothGattService scanService = BluetoothController.getInstance().mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GSDIS);

        if (scanService != null) {

            BluetoothGattCharacteristic clearScanCharacteristic = scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_CLEAR_SCAN);
            clearScanCharacteristic.setValue(index);
            mGatt.writeCharacteristic(clearScanCharacteristic);

            Log.i("clear Scan", "clear scan requested");
        }
    }

    @Override
    public BluetoothController connect(Context context) {

        disconnect();

        // We set up the local broadcast manager
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context.getApplicationContext());

        //Bluetooth
        BluetoothManager mBluetoothManager = (BluetoothManager) context.getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast toast = Toast.makeText(context.getApplicationContext(), context.getString(R.string.toast_error_bluetooth_not_supported), Toast.LENGTH_SHORT);

            toast.show();
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            this.connected = false;
            return this;
        }

        btDevices = new ArrayList<>();
        addressDevices = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, btDevices);

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                if (device.getName().equals("NIRScanNano")) {
                    arrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    addressDevices.add(device.getAddress());
                }
            }
        }


        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        try {
            context.unregisterReceiver(mReceiver);
            Log.i("notReg", "Receiver registered");

        }catch (Exception e){
            Log.i("notReg", "Receiver not registered");
        }
        context.registerReceiver(mReceiver, filter);


        BluetoothLeAdvertiser mBluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
        mBluetoothLeAdvertiser.startAdvertising(settings, data, mAdvertiseCallback);

        BluetoothGattServer mGattServer = mBluetoothManager.openGattServer(context, mGattServerCallback);
        mGattServer.addService(createService());

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        bluetoothAdapter.startDiscovery();

        // The devices are showing and connected to the device selected
        AlertDialog.Builder devicesDialog = showDialog(context);

        devicesDialog.create();

        devicesDialog.show();

        return this;
    }

    private void connectTo(Context context, int which){
        BluetoothController.getInstance().bluetoothDevice = BluetoothController.getInstance().bluetoothAdapter.getRemoteDevice(addressDevices.get(which));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mGatt = BluetoothController.getInstance().bluetoothDevice.connectGatt(context, false, mGattCallback, BluetoothDevice.TRANSPORT_LE);
        } else {
            mGatt = BluetoothController.getInstance().bluetoothDevice.connectGatt(context, false, mGattCallback);
        }
        new MainFragment.DeviceConnection(context).setUpProgressDialog();

        Toast.makeText(context, context.getString(R.string.toast_selected_device) + btDevices.get(which), Toast.LENGTH_LONG).show();

        // We have to wait while stablishing connection
        SystemClock.sleep(700);
        mGatt.discoverServices();
        Log.i("Discover:", "Discovering " + mGatt.getServices().size());
    }


    private AlertDialog.Builder showDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(context.getString(R.string.choose_device));

        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                connectTo(context, which);


            }

        });

        return builder;

    }

    BluetoothGatt getmGatt() {
        return mGatt;
    }

    @Override
    public Boolean getConnected() {

        return connected;
    }

    public void disconnect() {
        connected = false;
        if (bluetoothAdapter == null || mGatt == null) {
            Log.i("disconnect", "already disconnected");
            return;
        }

        mGatt.disconnect();
        mGatt.close();
    }



    @Override
    public void sendThresholds(String temperature, String humidity) {
        BluetoothGattService serviceGattSendThresholds = BluetoothController.getInstance().mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GGIS);
        while (serviceGattSendThresholds == null) {
            serviceGattSendThresholds = mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GGIS);
        }

        if (!temperature.equals("")) {
            BluetoothGattCharacteristic temperatureThresholdCharacteristic = serviceGattSendThresholds.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_TEMPERATURE_THRESHOLD_WRITE);
            temperature = String.valueOf(Math.round(Float.valueOf(temperature) * 100));
            temperatureThresholdCharacteristic.setValue(temperature);
            mGatt.writeCharacteristic(temperatureThresholdCharacteristic);


            Log.i("Temperature write", "temperature: " + temperatureThresholdCharacteristic.getStringValue(0));
        }

        if (!humidity.equals("")) {
            BluetoothGattCharacteristic humidityThresholdCharacteristic = serviceGattSendThresholds.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_HUMIDITY_THRESHOLD_WRITE);
            humidity = String.valueOf(Math.round(Float.valueOf(humidity) * 100));
            humidityThresholdCharacteristic.setValue(humidity);
            mGatt.writeCharacteristic(humidityThresholdCharacteristic);


            Log.i("Humidity write", "humidity: " + humidityThresholdCharacteristic.getStringValue(0));
        }
    }

    @Override
    public Handler getInformation() {
        return information;
    }

    @Override
    public void performAction(Integer action) {
        // We select the action required
        switch (action){
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
                ActivateConfigurationDeviceBluetoothCommand.getInstance().setConfigurationToActive(configurationToActive);
                invoker.activateConfiguration();
                break;
            default:
                Log.i("Not supported", "action not supported");
                break;

        }


    }

    @Override
    public void configurationToActive(byte[] configuration) {
        this.configurationToActive = configuration;
    }

    private BluetoothGattService createService() {
        BluetoothGattService service = new BluetoothGattService(SERVICE_UUID, SERVICE_TYPE_PRIMARY);

        // Counter characteristic (read-only, supports subscriptions)
        BluetoothGattCharacteristic counter = new BluetoothGattCharacteristic(CHARACTERISTIC_COUNTER_UUID, PROPERTY_READ | PROPERTY_NOTIFY, PERMISSION_READ | PERMISSION_WRITE | PERMISSION_WRITE_ENCRYPTED | PERMISSION_WRITE_ENCRYPTED_MITM);
        BluetoothGattDescriptor counterConfig = new BluetoothGattDescriptor(DESCRIPTOR_CONFIG_UUID, PERMISSION_READ | PERMISSION_WRITE);
        counter.addDescriptor(counterConfig);

        // Interactor characteristic
        BluetoothGattCharacteristic interactor = new BluetoothGattCharacteristic(CHARACTERISTIC_INTERACTOR_UUID, PROPERTY_WRITE_NO_RESPONSE, PERMISSION_WRITE);

        service.addCharacteristic(counter);
        service.addCharacteristic(interactor);
        return service;
    }

    private void getRefCalibCoef() {
        BluetoothGattService calibrationService = BluetoothController.getInstance().mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GCIS);

        if (calibrationService != null) {

            BluetoothGattCharacteristic referenceCalibCoeffCharacteristic = calibrationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_REQUEST_REF_CALIB_COEF);
            referenceCalibCoeffCharacteristic.setValue(new byte[]{0x00});
            mGatt.writeCharacteristic(referenceCalibCoeffCharacteristic);

            Log.i("requestRefCalib", "reference calibration coefficients requested");
        }
    }

    private void getRefCalibMatrix() {
        Log.i("calibrationContinued", "calibration started");
        BluetoothGattService calibrationService = BluetoothController.getInstance().mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GCIS);

        if (calibrationService != null) {

            BluetoothGattCharacteristic referenceCalibMatrixCharacteristic = calibrationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_REQUEST_REF_CALIB_MATRIX);
            referenceCalibMatrixCharacteristic.setValue(new byte[]{0x00});
            mGatt.writeCharacteristic(referenceCalibMatrixCharacteristic);

            Log.i("requestRefCalib", "reference calibration coefficients requested");
        }
    }

    // Configuration
    private void getStoredConfList() {
        Log.i("configurationContinued", "configuration continued");
        BluetoothGattService configurationService = BluetoothController.getInstance().mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GSCIS);

        if (configurationService != null) {

            BluetoothGattCharacteristic storedConfListCharacteristic = configurationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_REQUEST_STORED_CONF_LIST);
            storedConfListCharacteristic.setValue(new byte[]{0x00});
            mGatt.writeCharacteristic(storedConfListCharacteristic);

            Log.i("storedConfList", "stored configuration list requested");
        }
    }

    private void getScanConfData(byte [] data) {
        Log.i("configurationContinued", "configuration continued");
        BluetoothGattService configurationService = BluetoothController.getInstance().mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GSCIS);

        if (configurationService != null) {

            BluetoothGattCharacteristic scanConfDataCharacteristic = configurationService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_REQUEST_SCAN_CONF_DATA);
            scanConfDataCharacteristic.setValue(data);
            mGatt.writeCharacteristic(scanConfDataCharacteristic);

            Log.i("storedConfData", "stored configuration data requested");
        }
    }

    // Scan
    private void getNameScan(byte[] index){
        Log.i("scanContinued", "scan continue");
        BluetoothGattService scanService = BluetoothController.getInstance().mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GSDIS);

        if (scanService != null) {

            BluetoothGattCharacteristic nameScanCharacteristic = scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_REQUEST_SCAN_NAME);
            nameScanCharacteristic.setValue(index);
            mGatt.writeCharacteristic(nameScanCharacteristic);

            Log.i("name Scan", "name scan requested");
        }
    }

    private void getScanType(byte[] index){
        Log.i("scanContinued", "scan continue");
        BluetoothGattService scanService = BluetoothController.getInstance().mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GSDIS);

        if (scanService != null) {

            BluetoothGattCharacteristic scanTypeCharacteristic = scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_REQUEST_SCAN_TYPE);
            scanTypeCharacteristic.setValue(index);
            mGatt.writeCharacteristic(scanTypeCharacteristic);

            Log.i("type Scan", "type scan requested");
        }
    }

    private void getScanDateTime(byte[] index){
        Log.i("scanContinued", "scan continue");
        BluetoothGattService scanService = BluetoothController.getInstance().mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GSDIS);

        if (scanService != null) {

            BluetoothGattCharacteristic scanDateTimeCharacteristic = scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_REQUEST_SCAN_DATE_TIME);
            scanDateTimeCharacteristic.setValue(index);
            mGatt.writeCharacteristic(scanDateTimeCharacteristic);

            Log.i("scan Date Time", "scan date time requested");
        }
    }

    private void getPacketFormat(byte[] index){
        Log.i("scanContinued", "scan continue");
        BluetoothGattService scanService = BluetoothController.getInstance().mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GSDIS);

        if (scanService != null) {

            BluetoothGattCharacteristic packetFormatCharacteristic = scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_REQUEST_PACKET_FORMAT_VERSION);
            packetFormatCharacteristic.setValue(index);
            mGatt.writeCharacteristic(packetFormatCharacteristic);

            Log.i("packet format Scan", "packet format scan requested");
        }
    }

    private void getSerializedData(byte[] index){
        Log.i("scanContinued", "scan continue");
        BluetoothGattService scanService = BluetoothController.getInstance().mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GSDIS);

        if (scanService != null) {

            BluetoothGattCharacteristic serializedDataCharacteristic = scanService.getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_REQUEST_DATA);
            serializedDataCharacteristic.setValue(index);
            mGatt.writeCharacteristic(serializedDataCharacteristic);

            Log.i("serialized Scan", "serialized data scan requested");
        }
    }

    //Function for know if a scan was succesful
    private boolean finishScan(byte[] scan){
        if (scan.length != 5){
            return true;
        }

        return !((scan[0] == 1) & (scan[1] == -125) & (scan[2] == 10) & (scan[3] == 0) & (scan[4] == 0));
    }

    //Function for obtain the size parting from the 4 bytes sent by NIRScan
    private int fromByteArray(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    private void requestData(BluetoothGattCharacteristic characteristic, Integer requiredData, Integer requiredSize, Integer requiredBuffer){
        byte valueCharacteristic[] = characteristic.getValue();

        if (valueCharacteristic[0] == 0) {
            //We obtain the buffer size
            byte size[] = new byte[valueCharacteristic.length - 1];
            for (int i = 1; i < valueCharacteristic.length; i++) {
                size[size.length - i] = valueCharacteristic[i];
            }

            ByteArrayOutputStream buffer = (ByteArrayOutputStream) information.handleGetRequest(requiredData, requiredBuffer);
            buffer.reset();
            information.handleSetRequest(requiredData, requiredSize, fromByteArray(size));

        } else{
            //We use the Size as reference for the buffer
            ByteArrayOutputStream buffer = (ByteArrayOutputStream) information.handleGetRequest(requiredData, requiredBuffer);
            for (int i = 1; i < valueCharacteristic.length; i++) {
                buffer.write(valueCharacteristic[i]);
            }

            //We test if the size has been completed
            if (buffer.size() == (Integer) information.handleGetRequest(requiredData, requiredSize)) {
                Log.i("characteristic", "characteristic data received succesfully");
                // We test if we have to make another call
                switch (requiredBuffer){
                    case 5001:
                        getRefCalibCoef();
                        break;
                    case 5003:
                        getRefCalibMatrix();
                        break;
                    case 5005:
                        // We advertise that calibration data is obtained
                        sendBroadcast(Broadcasts.FINISH_CALIBRATION);
                        break;
                    case 3002:
                        BluetoothGattCharacteristic activeScanCharacteristic = mGatt.getService(ServicesCharacteristicsBLE.SERVICE_GSCIS).getCharacteristic(ServicesCharacteristicsBLE.CHARACTERISTIC_ACTIVE_SCAN_CONF);
                        mGatt.readCharacteristic(activeScanCharacteristic);
                        break;
                    case 3004:
                        // We advertise that configuration data is obtained
                        sendBroadcast(Broadcasts.FINISH_CONFIGURATION);
                        break;
                    case 4002:
                        // We advertise that data scan is obtained
                        sendBroadcast(Broadcasts.FINISH_SCAN);
                        break;
                    default:
                        Log.i("error receiving", "error");

                }
            }

        }
    }

    private void sendBroadcast(String action){
        Intent intent = new Intent();
        intent.setAction(action);
        localBroadcastManager.sendBroadcast(intent);
    }

    public void unRegister(Context context){
        disconnect();
        try {
            context.unregisterReceiver(mReceiver);
        }catch (Exception e){
            Log.i("unreg", "Unregister failed");
        }
    }

}





