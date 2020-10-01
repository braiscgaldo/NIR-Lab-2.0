package com.cgaldo.brais.sistema.View.Fragments.Scan.Configuration;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgaldo.brais.sistema.Controller.Bluetooth.BluetoothController;
import com.cgaldo.brais.sistema.Controller.ConnectionsController;
import com.cgaldo.brais.sistema.Controller.ViewsController.ConfigurationsController;
import com.cgaldo.brais.sistema.Controller.ViewsController.ConfigurationsControllerInterface;
import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.InformationFragment.ConfigurationViewInformation;
import com.cgaldo.brais.sistema.Model.InformationFragment.ViewInformationInterface;
import com.cgaldo.brais.sistema.Model.StaticData.Actions;
import com.cgaldo.brais.sistema.Model.StaticData.Broadcasts;
import com.cgaldo.brais.sistema.Model.StaticData.ObjectsRequired;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;
import com.cgaldo.brais.sistema.R;
import com.cgaldo.brais.sistema.View.FragmentTemplate;
import com.cgaldo.brais.sistema.View.Adapter.ConfigurationsListAdapter;
import com.cgaldo.brais.sistema.View.Dialogs.Dialogs;
import com.cgaldo.brais.sistema.View.Dialogs.DialogsInterface;
import com.cgaldo.brais.sistema.View.Fragments.MainFragment;
import com.cgaldo.brais.sistema.View.Fragments.Scan.ScanFragment;
import com.cgaldo.brais.sistema.View.MainActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class ConfigurationFragment extends Fragment implements FragmentTemplate {

    // TextView
    private TextView noConfigurationsTv;

    // ListView
    private ListView configurationsListView;
    private ConfigurationsListAdapter configurationsListAdapter;

    // Controller
    private ConfigurationsControllerInterface configurationController;

    // Information
    private ViewInformationInterface informationInterface = ConfigurationViewInformation.getInstance();

    private ImageButton infoButton;

    // Dialog
    private DialogsInterface dialogs = Dialogs.getInstance();

    // Connections Controller
    private static ConnectionsController connectionsController;
    private BroadcastReceiver broadcastReceiver;

    // Configurating
    boolean configurating = false;

    // View
    private View rootView;

    private AppCompatActivity activity;


    public ConfigurationFragment(){
        // Empty constructor required
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setLastView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_configuration, container, false);

        // We get the items
        getItems();

        // We prepare the layout
        prepareList();

        // Broadcast
        broadcast();

        execute();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity =(AppCompatActivity) context;
    }

    @Override
    public void execute() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        ConfigurationFragment.DeviceConfiguration deviceConfiguration = new DeviceConfiguration(getContext());
        deviceConfiguration.execute();
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
            Toast.makeText(getContext(), getString(R.string.toast_change_orientation), Toast.LENGTH_SHORT).show();
            connectionsController.unRegister(getContext());
            Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.general_layout);
            activity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, new MainFragment()).commit();
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
    }

    // Attach items
    private void getItems(){
        this.connectionsController = BluetoothController.getInstance();
        this.noConfigurationsTv = rootView.findViewById(R.id.noConfigurationsView);
        this.configurationsListView = rootView.findViewById(R.id.configurationsList);
        this.configurationsListAdapter = configurationsListAdapter.getInstance(getContext(),new ArrayList<String>());
        this.configurationsListView.setAdapter(this.configurationsListAdapter);
        this.configurationsListView.setClickable(true);

        // Active configuration
        this.configurationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Activate configuration
                String configString = String.valueOf(configurationsListAdapter.getItem(position));
                String configStringF = String.valueOf(configString.toCharArray()[0]) + String.valueOf(configString.toCharArray()[1]);
                String configStringS = String.valueOf(configString.toCharArray()[2]);
                if (configString.length() == 3){
                    configStringS += "0";
                }else{
                    configStringS += String.valueOf(configString.toCharArray()[3]);
                }

                byte[] configToActive = {Byte.valueOf(configStringF), Byte.valueOf(configStringS)};

                connectionsController.configurationToActive(configToActive);
                ConfigurationFragment.ActivateDeviceConfiguration activateDeviceConfiguration = new ActivateDeviceConfiguration(getContext());
                activateDeviceConfiguration.execute();
                Log.i("itemclick", "item click " + String.valueOf(position));
            }
        });

        infoButton = rootView.findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.createInformationDialog(getContext(), "Configuration Information", getString(informationInterface.getInformation()));
            }
        });


    }

    // Prepare list
    private void prepareList(){
        this.noConfigurationsTv.setVisibility(View.GONE);
        this.configurationsListAdapter.updateList(new ArrayList<String>());
        this.configurationsListAdapter.notifyDataSetChanged();
    }

    private void broadcast(){
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Broadcasts.FINISH_CONFIGURATION)){
                    connectionsController.getInformation().handleRequest(Requests.DEVICE_PRE_SCAN_CONFIGURATIONS, null);

                    Log.i("configTreated", "treated");
                    ConfigurationFragment.DeviceConfiguration.progressDialog.dismiss();

                    getConfigurationsIndex();

                    if (configurating){
                        dialogs.createConfigurationDialog(getContext(), connectionsController.getInformation());
                        configurating = false;
                    }
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                }

                if (intent.getAction().equals(Broadcasts.FINISH_ACTIVATE_CONFIGURATION)){
                    connectionsController.getInformation().handleRequest(Requests.DEVICE_SCAN_CONFIGURATION, null);


                    ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream)connectionsController.getInformation().handleGetRequest(Requests.DEVICE_PRE_SCAN_CONFIGURATIONS, ObjectsRequired.SCAN_CONF_DATA);

                    byte[] data = byteArrayOutputStream.toByteArray();

                    configurationController.getConfigurationData(data);
                    configurating = true;

                    ConfigurationFragment.ActivateDeviceConfiguration.progressDialog.dismiss();
                    ConfigurationFragment.DeviceConfiguration deviceConfiguration = new DeviceConfiguration(activity);
                    deviceConfiguration.execute();

                }
                if (intent.getAction().equals(Broadcasts.ERROR_COMM)){
                    try {
                        ConfigurationFragment.ActivateDeviceConfiguration.progressDialog.dismiss();
                    } catch (Exception e){

                    }
                    try {
                        ConfigurationFragment.DeviceConfiguration.progressDialog.dismiss();
                    }catch (Exception e){

                    }
                    Toast.makeText(context, getString(R.string.toast_bad_time), Toast.LENGTH_LONG);
                    activity.getSupportFragmentManager().popBackStack();
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(Broadcasts.FINISH_CONFIGURATION);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, intentFilter);
        intentFilter = new IntentFilter(Broadcasts.FINISH_ACTIVATE_CONFIGURATION);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, intentFilter);
        intentFilter = new IntentFilter(Broadcasts.ERROR_COMM);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    private void getConfigurationsIndex(){
        Handler handler = connectionsController.getInformation();
        this.configurationController = ConfigurationsController.getInstance((Integer) handler.handleGetRequest(Requests.DEVICE_PRE_SCAN_CONFIGURATIONS, ObjectsRequired.NUMBER_STORED_CONFIGURATION));
        for(int i = 0; i < this.configurationController.getCountConfigurations(); i++){
            String index = String.valueOf(this.configurationController.getConfigurations().get(i)[0]) + String.valueOf(this.configurationController.getConfigurations().get(i)[1]);

            if (!this.configurationsListAdapter.getConfigurations().contains(index)) {
                this.configurationsListAdapter.add(index);
            }
        }

    }

    private static class DeviceConfiguration extends AsyncTask<Object, Object, Boolean> {

        static ProgressDialog progressDialog;
        private Context context;

        private DeviceConfiguration( Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading device configurations!! ");
            progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {
                // Wait 10 secs if necessary
                long time = SystemClock.currentThreadTimeMillis();
                while (SystemClock.currentThreadTimeMillis() - time < 10000 && progressDialog.isShowing()){

                }

                connectionsController.performAction(Actions.CONFIGURATE_DEVICE);
                // Wait 20 secs if necessary
                time = SystemClock.currentThreadTimeMillis();
                while (SystemClock.currentThreadTimeMillis() - time < 20000 && progressDialog.isShowing()){

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

    private static class ActivateDeviceConfiguration extends AsyncTask<Object, Object, Boolean> {

        static ProgressDialog progressDialog;
        private Context context;

        private ActivateDeviceConfiguration( Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Activating device configuration! ");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {
                connectionsController.performAction(Actions.ACTIVATE_CONFIGURATION);
                // Wait 20 secs if necessary
                long time = SystemClock.currentThreadTimeMillis();
                while (SystemClock.currentThreadTimeMillis() - time < 20000 && progressDialog.isShowing()){

                }

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Broadcasts.ERROR_COMM));

                    Toast.makeText(context, context.getString(R.string.toast_bad_time), Toast.LENGTH_LONG);
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

    public void setLastView() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setView("scanView");
    }


}
