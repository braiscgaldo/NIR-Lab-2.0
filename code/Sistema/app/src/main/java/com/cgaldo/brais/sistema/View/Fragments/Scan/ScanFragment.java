package com.cgaldo.brais.sistema.View.Fragments.Scan;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
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
import com.cgaldo.brais.sistema.Controller.Storage.Problem;
import com.cgaldo.brais.sistema.Controller.Storage.ProblemInterface;
import com.cgaldo.brais.sistema.Controller.Storage.ScanSelected;
import com.cgaldo.brais.sistema.Controller.ViewsController.ScanController;
import com.cgaldo.brais.sistema.Model.InformationFragment.ScanViewInformation;
import com.cgaldo.brais.sistema.Model.InformationFragment.ViewInformationInterface;
import com.cgaldo.brais.sistema.Model.StaticData.Actions;
import com.cgaldo.brais.sistema.Model.StaticData.Broadcasts;
import com.cgaldo.brais.sistema.Model.StaticData.GraphViewConstants;
import com.cgaldo.brais.sistema.View.FragmentTemplate;
import com.cgaldo.brais.sistema.View.Dialogs.Dialogs;
import com.cgaldo.brais.sistema.View.Dialogs.DialogsInterface;
import com.cgaldo.brais.sistema.View.Fragments.MainFragment;
import com.cgaldo.brais.sistema.View.Fragments.Scan.Configuration.ConfigurationFragment;
import com.cgaldo.brais.sistema.View.MainActivity;
import com.jjoe64.graphview.GraphView;

import com.cgaldo.brais.sistema.R;



public class ScanFragment extends Fragment implements FragmentTemplate {

    //Bluetooth
    private static ConnectionsController connectionsController;

    //Graph
    private GraphView graphView;

    //Tabs
    private TabLayout tableLayout;

    // Information
    private ViewInformationInterface informationInterface = ScanViewInformation.getInstance();

    //Buttons
    private ImageButton infoButton;
    private ImageButton scanConfigurationButton;
    private Button startScanButton;
    private Button saveScanButton;
    private Button predictButton;


    private ProblemInterface problemInterface;
    private ScanSelected scanSelected;


    //Know which action is done
    private boolean scan = false;

    //Controller interface
    private ScanController scanController;

    //BroadcastReceiver
    private BroadcastReceiver broadcastReceiver;

    // Dialogs
    private DialogsInterface dialogs = Dialogs.getInstance();

    // View
    private View rootView;

    private AppCompatActivity activity;

    // Landscape
    private boolean mTwoPane;

    public ScanFragment(){
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
        rootView = inflater.inflate(R.layout.fragment_scan, container, false);

        if (rootView.findViewById(R.id.secondary_container) != null){
            mTwoPane = true;
        }else{
            mTwoPane = false;
        }

        if(mTwoPane){
            horizontalLayout();
        }else{
            verticalLayout();
        }

        modifyGraph();


        broadcast();

        //We calibrate de device
        execute();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity =(AppCompatActivity) context;
    }

    private void getObjects(){
        //Buttons
        scanConfigurationButton = rootView.findViewById(R.id.scanConfigButton);
        startScanButton = rootView.findViewById(R.id.startScanButton);
        saveScanButton = rootView.findViewById(R.id.saveScanButton);
        predictButton = rootView.findViewById(R.id.predictButton);

        //GraphView
        graphView = rootView.findViewById(R.id.graphView);

        // Information
        infoButton = rootView.findViewById(R.id.infoButton);

        //Tabs
        tableLayout = rootView.findViewById(R.id.graphTabs);

        //Scan Controller
        scanController = ScanController.getInstance();

        //Connections controller
        connectionsController = BluetoothController.getInstance();

        // Problem interface
        problemInterface = Problem.getInstance();
        scanSelected = ScanSelected.getInstance();


    }

    private void buttonActionsVertical(){
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.getInstance().createInformationDialog(getContext(), "Scan Information", getString(informationInterface.getInformation()));
            }
        });

        //Actions with scanConfigurationButton
        scanConfigurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ConfigurationFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).addToBackStack(null).commit();

            }
        });

        //Actions with startScanButton
        startScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan = true;
                execute();
            }
        });

        //Actions with save button
        saveScanButton.setEnabled(false);

        //Table layout
        tableLayout.setEnabled(false);

        //Actions with predict button
        predictButton.setEnabled(false);
    }

    private void buttonActionsHorizontal(){
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.getInstance().createInformationDialog(getContext(), "Scan Information", getString(informationInterface.getInformation()));
            }
        });

        //Actions with scanConfigurationButton
        scanConfigurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ConfigurationFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).addToBackStack(null).commit();

            }
        });

        //Actions with startScanButton
        startScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan = true;
                execute();
            }
        });

        saveScanButton.setEnabled(false);

        tableLayout.setEnabled(false);

        predictButton.setEnabled(false);

    }

    private void prepareTabs(){
        tableLayout.setEnabled(true);
        ScanController.getInstance().setLinesVisibles(graphView, GraphViewConstants.INTENSITY);
        // Actions with tabs
        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getString(R.string.intensity))){
                    scanController.setLinesVisibles(graphView, GraphViewConstants.INTENSITY);
                } else if (tab.getText().toString().equals(getString(R.string.absorbance))){
                    scanController.setLinesVisibles(graphView, GraphViewConstants.ABSORBANCE);
                } else if (tab.getText().toString().equals(getString(R.string.reflectance))){
                    scanController.setLinesVisibles(graphView, GraphViewConstants.REFLECTANCE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void broadcast(){
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Broadcasts.FINISH_CALIBRATION)){
                    ScanCalibration.progressDialog.dismiss();
                    dialogs.createDialogProblems(activity);
                }


                if (intent.getAction().equals(Broadcasts.FINISH_SCAN)){
                    scanController.computeScan();
                    scanController.setData(graphView);
                    buttonActionsScanPerformed();


                    Log.i("configTreated", "treated");
                    Scan.progressDialog.dismiss();
                }
                if (intent.getAction().equals(Broadcasts.ERROR_COMM)){
                    try {
                        ScanFragment.ScanCalibration.progressDialog.dismiss();
                    } catch (Exception e){

                    }
                    try {
                        ScanFragment.Scan.progressDialog.dismiss();
                    }catch (Exception e){

                    }

                    Toast.makeText(context, getString(R.string.toast_bad_time), Toast.LENGTH_LONG);
                    activity.getSupportFragmentManager().popBackStack();
                }

                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);


            }
        };

        try{
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        }catch (Exception e){
            Log.i("notReg", "not Registered");
        }

        IntentFilter filter = new IntentFilter(Broadcasts.FINISH_CALIBRATION);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, filter);
        filter = new IntentFilter(Broadcasts.FINISH_SCAN);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, filter);
        filter = new IntentFilter(Broadcasts.ERROR_COMM);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    void modifyGraph(){
        // set manual X bounds
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);

        // enable scaling and scrolling
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScalableY(true);
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

    @Override
    public void execute() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        if (scan){
            scan = false;
            // If we want to perform a scan
            ScanFragment.Scan scan = new Scan(getContext());
            scan.execute();
            return;
        }

        ScanFragment.ScanCalibration scanCalibration = new ScanCalibration(getContext());
        scanCalibration.execute();
    }

    @Override
    public void verticalLayout() {
        getObjects();
        buttonActionsVertical();
    }

    @Override
    public void horizontalLayout() {
        getObjects();
        buttonActionsHorizontal();
    }


    public void buttonActionsScanPerformed(){
        saveScanButton.setEnabled(true);
        //Actions with save button
        saveScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.createDialogLabels(getContext(), scanSelected.getProblemSelectedString(), "");
            }
        });

        //Actions with predict button
        predictButton.setEnabled(true);
        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.getInstance().createSelectModelDialog(getActivity(), scanSelected.getProblemSelectedString(), connectionsController.getInformation());
            }
        });

        prepareTabs();
    }


    private static class ScanCalibration extends AsyncTask<Object, Object, Boolean> {

        public static ProgressDialog progressDialog;
        private Context context;

        private ScanCalibration( Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Calibrating device! This might take a while!");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {
                connectionsController.performAction(Actions.CALIBRATE_DEVICE);
                // Wait 30 secs if necessary
                long time = SystemClock.currentThreadTimeMillis();
                while (SystemClock.currentThreadTimeMillis() - time < 30000 && progressDialog.isShowing()){

                }

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Broadcasts.ERROR_COMM));
                    Toast.makeText(context, R.string.toast_bad_time, Toast.LENGTH_LONG);
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

    private static class Scan extends AsyncTask<Object, Object, Boolean> {

        static ProgressDialog progressDialog;
        private Context context;

        private Scan( Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Scanning sample! This might take a while!");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {
                connectionsController.performAction(Actions.SCAN_SAMPLE);
                // Wait 30 secs if necessary
                long time = SystemClock.currentThreadTimeMillis();
                while (SystemClock.currentThreadTimeMillis() - time < 30000 && progressDialog.isShowing()){

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


    private void setLastView(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setView("scanView");
    }

}
