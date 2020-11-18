package com.cgaldo.brais.sistema.View.Fragments;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cgaldo.brais.sistema.Controller.Bluetooth.BluetoothController;
import com.cgaldo.brais.sistema.Controller.ConnectionsController;
import com.cgaldo.brais.sistema.Controller.USB.USBController;
import com.cgaldo.brais.sistema.Controller.ViewsController.MainController;
import com.cgaldo.brais.sistema.Controller.ViewsController.MainControllerInterface;
import com.cgaldo.brais.sistema.Model.InformationFragment.MainViewInformation;
import com.cgaldo.brais.sistema.Model.InformationFragment.ViewInformationInterface;
import com.cgaldo.brais.sistema.Model.StaticData.Broadcasts;
import com.cgaldo.brais.sistema.Model.StaticData.CommandsUSB;
import com.cgaldo.brais.sistema.R;
import com.cgaldo.brais.sistema.View.FragmentTemplate;
import com.cgaldo.brais.sistema.View.Dialogs.Dialogs;
import com.cgaldo.brais.sistema.View.Fragments.Problems.ProblemsFragment;
import com.cgaldo.brais.sistema.View.Fragments.Scan.ScanFragment;
import com.cgaldo.brais.sistema.View.Fragments.Spectrophotometer.SpectrometerFragment;
import com.cgaldo.brais.sistema.View.MainActivity;

import java.util.HashMap;

/*
    MainFragment of the application
 */

public class MainFragment extends Fragment implements FragmentTemplate {
    //Constants
    int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 2; // uses to permissions
    int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 3;
    int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 4;
    public static final String ACTION_USB_DEVICE_ATTACHED = "com.example.ACTION_USB_DEVICE_ATTACHED";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";


    //Controller
    private MainControllerInterface controllerInterface;

    // Information
    private ViewInformationInterface informationInterface = MainViewInformation.getInstance();

    //ScreenButton
    private ImageButton infoButton;
    private ImageButton bluetoothButton;
    private Button scanButton;
    private Button spectrometerButton;
    private Button problemsButton;

    //Bluetooth
    private static ConnectionsController connectionsController;
    private UsbManager usbManager;

    //Broadcast
    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver usbBroadcast;

    // View
    private View rootView;

    private AppCompatActivity activity;

    // Landscape
    private boolean mTwoPane;

    public MainFragment(){
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
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if (rootView.findViewById(R.id.secondary_container) != null){
            mTwoPane = true;
        }else{
            mTwoPane = false;
        }

        if (mTwoPane){
            horizontalLayout();
        }else {
            verticalLayout();
        }


        // Check directories

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_READ_EXTERNAL_STORAGE);

        if (!controllerInterface.prepareApplicationStorage()){
            Toast.makeText(getContext(), "Cannot create directories", Toast.LENGTH_LONG);
            Log.i("errorDir", "cannot create directories");
        }

        // Broadcast
        //usbConnection();
        broadcast();

        return rootView;
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity =(AppCompatActivity) context;
    }

    @Override
    public void verticalLayout(){
        getObjects();
        buttonActionsVertical();
    }

    @Override
    public void horizontalLayout() {
        getObjects();
        buttonActionsHorizontal();
    }

    private void getObjects(){
        //Controller
        controllerInterface = MainController.getInstance();
        //connectionsController = BluetoothController.getInstance();
        connectionsController = USBController.getInstance();
        connectionsController.connectUSB(getActivity());


        //Buttons
        infoButton = rootView.findViewById(R.id.infoButton);
        bluetoothButton = rootView.findViewById(R.id.bluetoothButton);
        scanButton = rootView.findViewById(R.id.scanButton);
        spectrometerButton = rootView.findViewById(R.id.spectrometerButton);
        problemsButton = rootView.findViewById(R.id.problemsButton);
    }

    private void buttonActionsVertical(){
        //Actions for infoButton
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.getInstance().createInformationDialog(getContext(), "Main Information", getString(informationInterface.getInformation()));
            }

        });

        //Actions for bluetoothButton
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                execute();

            } //Connect device


        });

        //Actions for spectrometer button
        spectrometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SpectrometerFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), fragment).addToBackStack(null).commit();


            }
        });

        //Actions for scanButton
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionsController.getConnected()) {
                    Fragment fragment = new ScanFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), fragment).addToBackStack(null).commit();

                }else{
                    String message = getString(R.string.toast_must_be_connected_to_device);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                }

            }
        });

        //Actions for problems button
        problemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProblemsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), fragment).addToBackStack(null).commit();

            }
        });
    }

    private void buttonActionsHorizontal(){
        //Actions for infoButton
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.getInstance().createInformationDialog(getContext(), "Main Information", getString(informationInterface.getInformation()));
            }

        });

        //Actions for bluetoothButton
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                execute();

            } //Connect device


        });

        //Actions for spectrometer button
        spectrometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SpectrometerFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), fragment).addToBackStack(null).commit();


            }
        });

        //Actions for scanButton
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionsController.getConnected()) {
                    Fragment fragment = new ScanFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), fragment).addToBackStack(null).commit();

                }else{
                    String message = getString(R.string.toast_must_be_connected_to_device);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                }

            }
        });

        //Actions for problems button
        problemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProblemsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), fragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public void execute() {
        connectionsController.connect(getContext());

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(getContext(), getString(R.string.toast_change_orientation), Toast.LENGTH_SHORT).show();
            connectionsController.unRegister(getContext());
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
    }

    //@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    private void broadcast(){
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Broadcasts.FINISH_CONNECTION)){
                    DeviceConnection.progressDialog.dismiss();

                }
                if (intent.getAction().equals(Broadcasts.ERROR_COMM)){
                    DeviceConnection.progressDialog.dismiss();
                    Toast.makeText(context, getString(R.string.toast_bad_time), Toast.LENGTH_LONG);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(Broadcasts.FINISH_CONNECTION);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, intentFilter);
        intentFilter = new IntentFilter(Broadcasts.ERROR_COMM);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, intentFilter);

        usbBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("USB", intent.getAction());
                String usbStateChangeAction = "android.hardware.usb.action.USB_STATE";
                if (intent.getAction().equalsIgnoreCase(usbStateChangeAction)) {
                    if (intent.getExtras().getBoolean("connected")) {
                        Log.i("USB", "usb connected");
                        controllerInterface.manageControllers(true);
                    } else {
                        controllerInterface.manageControllers(false);
                        Log.i("USB", "usb disconnected");
                    }
                }
            }
        };

        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(getContext(), 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(this.usbBroadcast, filter);

        //this.usbManager.requestPermission(device, mPermissionIntent);


    }

    private void getInfo(){
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    public static class DeviceConnection extends AsyncTask<Object, Object, Boolean>{

        static ProgressDialog progressDialog;
        private Context context;

        public DeviceConnection( Context context) {
            this.context = context;
        }

        public void setUpProgressDialog(){
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Connecting to device");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        public void dissmissProgressDialog(){
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {
                connectionsController.connect(context);
                // Wait 10 secs if necessary
                long time = SystemClock.currentThreadTimeMillis();
                while (SystemClock.currentThreadTimeMillis() - time < 10000 && progressDialog.isShowing()){

                }

                if (progressDialog.isShowing()){
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Broadcasts.ERROR_COMM));
                    return false;
                }

            }catch (Exception e){
                Log.e("Exception", String.valueOf(e));
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
        mainActivity.setView("mainView");
    }


}
