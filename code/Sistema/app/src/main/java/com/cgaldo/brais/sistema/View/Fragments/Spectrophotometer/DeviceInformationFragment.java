package com.cgaldo.brais.sistema.View.Fragments.Spectrophotometer;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cgaldo.brais.sistema.Controller.Bluetooth.BluetoothController;
import com.cgaldo.brais.sistema.Controller.ConnectionsController;
import com.cgaldo.brais.sistema.Controller.USB.USBController;
import com.cgaldo.brais.sistema.Model.InformationFragment.DeviceInformationViewInformation;
import com.cgaldo.brais.sistema.Model.InformationFragment.ViewInformationInterface;
import com.cgaldo.brais.sistema.Model.StaticData.Actions;
import com.cgaldo.brais.sistema.Model.StaticData.Broadcasts;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;
import com.cgaldo.brais.sistema.R;
import com.cgaldo.brais.sistema.View.FragmentTemplate;
import com.cgaldo.brais.sistema.View.Dialogs.Dialogs;
import com.cgaldo.brais.sistema.View.MainActivity;

import java.util.HashMap;

public class DeviceInformationFragment extends Fragment implements FragmentTemplate {

    //Bluetooth
    private static ConnectionsController connectionsController;

    // Information
    ViewInformationInterface informationInterface = DeviceInformationViewInformation.getInstance();

    // Button
    private ImageButton infoButton;

    //Map to hold TextViews
    private HashMap<String, View> placeToShow;

    // Broadcast Receiver
    private BroadcastReceiver broadcastReceiver;

    // View
    private View rootView;

    //Context
    private AppCompatActivity activity;

    public DeviceInformationFragment(){
        // Empty constructor required
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setLastView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_device_information, container, false);

        //Bluetooth
        //connectionsController = BluetoothController.getInstance();
        connectionsController = USBController.getInstance();
        //TextViews
        getViews();

        // Information
        execute();

        // Broadcast
        broadcast();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity =(AppCompatActivity) context;
    }

    private void getViews(){
        placeToShow = new HashMap<>();
        placeToShow.put("manufacture", rootView.findViewById(R.id.manufactureDataView));
        placeToShow.put("modelNumber", rootView.findViewById(R.id.modelNumberDataView));
        placeToShow.put("serialNumber", rootView.findViewById(R.id.serialNumberDataView));
        placeToShow.put("hardwareRevision", rootView.findViewById(R.id.hardwareRevisionDataView));
        placeToShow.put("tivaFirmwareRevision", rootView.findViewById(R.id.tivaFirmwaeVersionDataView));
        placeToShow.put("spectrumLibraryRevision", rootView.findViewById(R.id.spectrumLibraryRevisionDataView));
        infoButton = rootView.findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.getInstance().createInformationDialog(getContext(), "Device Information Information", getString(informationInterface.getInformation()));
            }
        });
    }

    @Override
    public void execute() {
        //Information
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        DeviceInformationFragment.DeviceInfo deviceInfo = new DeviceInformationFragment.DeviceInfo(activity);
        deviceInfo.execute();
    }

    @Override
    public void verticalLayout() {

    }

    @Override
    public void horizontalLayout() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(getContext(), "Change configuration", Toast.LENGTH_SHORT).show();
            connectionsController.unRegister(activity);
            getActivity().getSupportFragmentManager().popBackStackImmediate();

        }
    }

    private void broadcast(){
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Broadcasts.FINISH_INFORMATION)){
                    DeviceInfo.progressDialog.dismiss();
                    connectionsController.getInformation().handleRequest(Requests.DEVICE_INFORMATION, placeToShow);
                }
                if (intent.getAction().equals(Broadcasts.ERROR_COMM)){
                    DeviceInfo.progressDialog.dismiss();
                    activity.getSupportFragmentManager().popBackStack();
                }

                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

            }
        };

        IntentFilter intentFilter = new IntentFilter(Broadcasts.FINISH_INFORMATION);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, intentFilter);
        intentFilter = new IntentFilter(Broadcasts.ERROR_COMM);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    private static class DeviceInfo extends AsyncTask<Object, Object, Boolean>{

        static ProgressDialog progressDialog;
        private Context context;

        private DeviceInfo( Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading device information");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {
                connectionsController.performAction(Actions.RETRIEVE_DEVICE_INFORMATION);
                // Wait 10 secs if necessary
                long time = SystemClock.currentThreadTimeMillis();
                while (SystemClock.currentThreadTimeMillis() - time < 10000 && progressDialog.isShowing()){

                }

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Broadcasts.ERROR_COMM));
                    Toast.makeText(context, "Bad time", Toast.LENGTH_LONG);
                    return false;
                }

            }catch (Exception e){
                return false;
            }
            return true;

        }

        @Override
        protected void onPostExecute(Boolean result) {


        }
    }

}

