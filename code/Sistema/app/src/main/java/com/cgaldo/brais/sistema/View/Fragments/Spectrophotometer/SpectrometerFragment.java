package com.cgaldo.brais.sistema.View.Fragments.Spectrophotometer;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cgaldo.brais.sistema.Controller.Bluetooth.BluetoothController;
import com.cgaldo.brais.sistema.Controller.ConnectionsController;
import com.cgaldo.brais.sistema.Controller.ViewsController.SpectrometerController;
import com.cgaldo.brais.sistema.Controller.ViewsController.SpectrometerControllerInterface;
import com.cgaldo.brais.sistema.Model.InformationFragment.SpectrometerViewInformation;
import com.cgaldo.brais.sistema.Model.InformationFragment.ViewInformationInterface;
import com.cgaldo.brais.sistema.Model.StaticData.Actions;
import com.cgaldo.brais.sistema.R;
import com.cgaldo.brais.sistema.View.FragmentTemplate;
import com.cgaldo.brais.sistema.View.Dialogs.Dialogs;
import com.cgaldo.brais.sistema.View.MainActivity;

public class SpectrometerFragment extends Fragment implements FragmentTemplate {
    //Bluetooth
    private static ConnectionsController connectionsController;

    //Controller
    private SpectrometerControllerInterface controller;

    // Information
    private ViewInformationInterface informationInterface = SpectrometerViewInformation.getInstance();
    //Buttons
    private ImageButton infoButton;

    private Button deviceInfoButton;
    private Button deviceStatusButton;
    private Button deviceResetButton;

    // Activity to launch
    private Fragment fragment;

    // Flag for reset the device
    boolean reset = false;

    // View
    private View rootView;
    private AppCompatActivity activity;

    // Landscape
    private boolean mTwoPane;

    public SpectrometerFragment(){
        // Empty constructor required
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        activity = (AppCompatActivity) context;
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
        rootView = inflater.inflate(R.layout.fragment_spectrometer, container, false);

        if (getActivity().findViewById(R.id.secondary_container) != null){
            mTwoPane = true;
        }else{
            mTwoPane = false;
        }

        if(mTwoPane){
            horizontalLayout();
        }else{
            verticalLayout();
        }

        return rootView;
    }

    private void getObjects(){
        //Obtain bluetooth connection
        connectionsController = BluetoothController.getInstance();

        //Controller
        controller = SpectrometerController.getInstance();

        //Buttons
        deviceInfoButton = rootView.findViewById(R.id.deviceInfo);
        deviceStatusButton = rootView.findViewById(R.id.deviceStatus);
        infoButton = rootView.findViewById(R.id.infoButton);
        deviceResetButton = rootView.findViewById(R.id.deviceReset);
    }

    private void buttonActionsVertical(){
        //Actions for infoButton
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.getInstance().createInformationDialog(getContext(), "Spectrophotometer Information", getString(informationInterface.getInformation()));

            }
        });

        //Actions for deviceInfoButton
        deviceInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new DeviceInformationFragment();

                execute();
            }
        });

        //Actions for deviceStatusButton
        deviceStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new DeviceStatusFragment();

                execute();
            }
        });

        //Actions for deviceResetButton
        deviceResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset = true;
                execute();
            }
        });
    }

    private void buttonActionsHorizontal(){
        //Actions for infoButton
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.getInstance().createInformationDialog(getContext(), "Spectrometer Information", getString(informationInterface.getInformation()));

            }
        });

        //Actions for deviceInfoButton
        deviceInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new DeviceInformationFragment();

                execute();
            }
        });

        //Actions for deviceStatusButton
        deviceStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new DeviceStatusFragment();

                execute();
            }
        });

        //Actions for deviceResetButton
        deviceResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset = true;
                execute();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(getContext(), getString(R.string.toast_change_orientation), Toast.LENGTH_SHORT).show();
            connectionsController.unRegister(activity);
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.secondary_container);
                Fragment fragment1 = activity.getSupportFragmentManager().findFragmentById(R.id.main_container);
                activity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                activity.getSupportFragmentManager().beginTransaction().remove(fragment1).commit();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, new SpectrometerFragment()).commit();
            }
            activity.getSupportFragmentManager().popBackStackImmediate();
            onDestroy();

        }
    }

    @Override
    public void execute() {
        if (mTwoPane){
            executeHorizontal();
        }else {
            executeVertical();
        }
    }

    private void executeVertical(){
        if(connectionsController.getConnected()) {
            if (reset){
                ResetDevice resetDevice = new ResetDevice(getContext());
                resetDevice.execute();
                reset = false;
                return;
            }
            //Give feed to user
            Toast.makeText(getContext(), getString(R.string.toast_download_information), Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), fragment).addToBackStack(null).commit();

        }else{
            String message = getString(R.string.toast_must_be_connected_to_device);
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    private void executeHorizontal(){
        if(connectionsController.getConnected()) {
            if (reset){
                ResetDevice resetDevice = new ResetDevice(getContext());
                resetDevice.execute();
                reset = false;
                return;
            }
            //Give feed to user
            Toast.makeText(getContext(), R.string.toast_download_information, Toast.LENGTH_LONG).show();
            Fragment f = getFragmentManager().findFragmentById(R.id.general_layout);
            if(f != null){
                // Change view logic
                getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
                Fragment spectrometerFragment = new SpectrometerFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, spectrometerFragment).commit();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.secondary_container, fragment).commit();
            }else{
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.secondary_container, fragment).commit();

            }

        }else{
            String message = getString(R.string.toast_must_be_connected_to_device);
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
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

    private static class ResetDevice extends AsyncTask<Object, Object, Boolean> {

        private Context context;

        private ResetDevice( Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {
                connectionsController.performAction(Actions.RESET_DEVICE);

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
        mainActivity.setView("spectrometerView");
    }
}
