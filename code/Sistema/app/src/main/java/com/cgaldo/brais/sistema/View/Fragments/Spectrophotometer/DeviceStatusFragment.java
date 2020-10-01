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
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cgaldo.brais.sistema.Controller.Bluetooth.BluetoothController;
import com.cgaldo.brais.sistema.Controller.ConnectionsController;
import com.cgaldo.brais.sistema.Model.InformationFragment.DeviceInformationViewInformation;
import com.cgaldo.brais.sistema.Model.InformationFragment.ViewInformationInterface;
import com.cgaldo.brais.sistema.Model.StaticData.Actions;
import com.cgaldo.brais.sistema.Model.StaticData.Broadcasts;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;
import com.cgaldo.brais.sistema.R;
import com.cgaldo.brais.sistema.View.FragmentTemplate;
import com.cgaldo.brais.sistema.View.Dialogs.Dialogs;

import java.util.HashMap;
import java.util.Map;

public class DeviceStatusFragment extends Fragment implements FragmentTemplate {

    //Bluetooth
    private static ConnectionsController connectionsController;

    // Information
    private ViewInformationInterface informationInterface = DeviceInformationViewInformation.getInstance();

    //TextViews
    private ImageButton infoButton;
    private Button sendThresholdButton;

    // Map for holding TextViews
    private Map<String, View> placeToShow;

    // Broadcast Receiver
    private BroadcastReceiver broadcastReceiver;

    // View
    private View rootView;

    private AppCompatActivity activity;

    public DeviceStatusFragment(){
        // Empty constructor required
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_device_status, container, false);

        //Bluetooth
        connectionsController = BluetoothController.getInstance();

        //Button
        sendThresholdButton = rootView.findViewById(R.id.sendThreshold);

        //Views
        getViews();

        //Status
        execute();

        //Broadcast
        broadcast();

        //Actions for sendThresholdButton
        sendThresholdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView temperatureThresholdTV = (TextView) placeToShow.get("temperatureThreshold");
                TextView humidityThresholdTV = (TextView) placeToShow.get("humidityThreshold");
                //Send thresholds if almost 1 is defined
                if ((temperatureThresholdTV.getText().length() > 0) || (humidityThresholdTV.getText().length() > 0)){
                    connectionsController.sendThresholds(temperatureThresholdTV.getText().toString(), humidityThresholdTV.getText().toString());
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity =(AppCompatActivity) context;
    }

    private void getViews(){
        //TextViews
        placeToShow = new HashMap<>();
        placeToShow.put("batteryLevel", rootView.findViewById(R.id.batteryDataView));
        placeToShow.put("temperatureLevel", rootView.findViewById(R.id.temperatureDataView));
        placeToShow.put("humidityLevel", rootView.findViewById(R.id.humidityDataView));
        placeToShow.put("temperatureThreshold", rootView.findViewById(R.id.temperatureThresholdDataView));
        placeToShow.put("humidityThreshold", rootView.findViewById(R.id.humidityThresholdDataView));
        infoButton = rootView.findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.getInstance().createInformationDialog(getContext(), "Device Status Information", getString(informationInterface.getInformation()));
            }
        });
    }

    private void broadcast(){
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Broadcasts.FINISH_STATUS)){
                    DeviceStatus.progressDialog.dismiss();
                    connectionsController.getInformation().handleRequest(Requests.DEVICE_STATUS, placeToShow);
                }
                if (intent.getAction().equals(Broadcasts.ERROR_COMM)){
                    DeviceStatus.progressDialog.dismiss();
                    activity.getSupportFragmentManager().popBackStack();
                    Toast.makeText(context, "Bad time", Toast.LENGTH_LONG);

                }
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

            }
        };

        IntentFilter intentFilter = new IntentFilter(Broadcasts.FINISH_STATUS);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, intentFilter);
        intentFilter = new IntentFilter(Broadcasts.ERROR_COMM);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void execute() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        DeviceStatusFragment.DeviceStatus deviceStatus = new DeviceStatusFragment.DeviceStatus(getContext());
        deviceStatus.execute();
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
            activity.getSupportFragmentManager().popBackStackImmediate();

        }
    }

    private static class DeviceStatus extends AsyncTask<Object, Object, Boolean> {

        private static ProgressDialog progressDialog;
        private static Context context;

        private DeviceStatus( Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading device status");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {
                connectionsController.performAction(Actions.RETRIEVE_DEVICE_STATUS);
                // Wait 15 secs if necessary
                long time = SystemClock.currentThreadTimeMillis();
                while (SystemClock.currentThreadTimeMillis() - time < 15000 && progressDialog.isShowing()){

                }

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Broadcasts.ERROR_COMM));
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
