package com.cgaldo.brais.sistema.View.Fragments.Problems;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cgaldo.brais.sistema.Controller.Bluetooth.BluetoothController;
import com.cgaldo.brais.sistema.Controller.ConnectionsController;
import com.cgaldo.brais.sistema.Controller.Storage.Problem;
import com.cgaldo.brais.sistema.Controller.Storage.ProblemInterface;
import com.cgaldo.brais.sistema.Controller.Storage.ScanSelected;
import com.cgaldo.brais.sistema.Controller.ViewsController.ScanController;
import com.cgaldo.brais.sistema.Controller.ViewsController.SeeScanController;
import com.cgaldo.brais.sistema.Controller.ViewsController.SeeScanControllerInterface;
import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.InformationFragment.SeeScanSavedViewInformation;
import com.cgaldo.brais.sistema.Model.InformationFragment.ViewInformationInterface;
import com.cgaldo.brais.sistema.Model.StaticData.Broadcasts;
import com.cgaldo.brais.sistema.Model.StaticData.GraphViewConstants;
import com.cgaldo.brais.sistema.R;
import com.cgaldo.brais.sistema.View.FragmentTemplate;
import com.cgaldo.brais.sistema.View.Adapter.LabelsListAdapter;
import com.cgaldo.brais.sistema.View.Dialogs.Dialogs;
import com.cgaldo.brais.sistema.View.MainActivity;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;
import java.util.List;

public class SeeScanSavedFragment extends Fragment implements FragmentTemplate {

    //Graph
    private GraphView graphView;

    //Tabs
    private TabLayout tableLayout;

    // Information
    private ViewInformationInterface informationInterface = SeeScanSavedViewInformation.getInstance();

    // Buttons
    private ImageButton infoButton;
    private Button predictButton;
    private Button editButton;

    // ListView
    private ListView labelsListView;
    private LabelsListAdapter labelsListAdapter;

    private ProblemInterface problemInterface;

    private SeeScanControllerInterface seeScanControllerInterface;
    private ScanSelected scanSelected;
    private ScanController scanController;

    private Handler handler;

    // Connections controller
    private ConnectionsController connectionsController;

    // View Fragment
    private View rootView;
    private BroadcastReceiver broadcastReceiver;

    // Two Pane
    private boolean mTwoPane;

    public SeeScanSavedFragment(){
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
        rootView = inflater.inflate(R.layout.fragment_see_scan_saved, container, false);

        if (rootView.findViewById(R.id.secondary_container) != null){
            mTwoPane = true;
        }else{
            mTwoPane = false;
        }

        if (mTwoPane){
            horizontalLayout();
        }else{
            verticalLayout();
        }

        broadcast();

        return rootView;
    }

    private void getObjects(){
        //GraphView
        graphView = rootView.findViewById(R.id.graphScanSavedView);

        modifyGraph();

        //Tabs
        tableLayout = rootView.findViewById(R.id.graphTabs);

        problemInterface = Problem.getInstance();

        seeScanControllerInterface = SeeScanController.getInstance();

        scanSelected = ScanSelected.getInstance();
        scanController = ScanController.getInstance();
        TextView title = rootView.findViewById(R.id.title);
        title.setText(scanSelected.getScanSelectedString());

        connectionsController = BluetoothController.getInstance();
        handler = connectionsController.getInformation();

        problemInterface.putScan(scanSelected.getProblemSelectedString(), scanSelected.getScanSelectedString(), handler);

        scanController.setData(graphView);
        scanController.setLinesVisibles(graphView, GraphViewConstants.INTENSITY);

        this.labelsListAdapter = LabelsListAdapter.getInstance(getContext(), getLabelsFromScan());
        this.labelsListView = rootView.findViewById(R.id.labelsList);
        this.labelsListView.setAdapter(this.labelsListAdapter);

        prepareTabs();

        infoButton = rootView.findViewById(R.id.infoButton);


        predictButton = rootView.findViewById(R.id.predictSavedButton);
        editButton = rootView.findViewById(R.id.editLabelsButton);

    }

    void modifyGraph(){
        // set manual X bounds
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);

        // enable scaling and scrolling
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScalableY(true);

        graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
        graphView.getGridLabelRenderer().setNumVerticalLabels(9);

    }

    private void buttonActionsVertical(){
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.getInstance().createInformationDialog(getContext(), "See Scan Saved Information", getString(informationInterface.getInformation()));
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.getInstance().createEditLabelsDialog(getContext(), scanSelected.getProblemSelectedString(), scanSelected.getScanSelectedString());
            }
        });

        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.getInstance().createSelectModelDialog(getActivity(), scanSelected.getProblemSelectedString(), connectionsController.getInformation());
            }
        });
    }

    private void buttonActionsHorizontal(){
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.getInstance().createInformationDialog(getContext(), "See Scan Saved Information", getString(informationInterface.getInformation()));
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.getInstance().createEditLabelsDialog(getContext(), scanSelected.getProblemSelectedString(), scanSelected.getScanSelectedString());
            }
        });

        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.getInstance().createSelectModelDialog(getActivity(), scanSelected.getProblemSelectedString(), connectionsController.getInformation());
            }
        });
    }

    private List<String> getLabelsFromScan(){
        List<String> labels = new ArrayList<>();
        for (String label : scanSelected.getLabelsMap().keySet()){
            labels.add(label + ": " + scanSelected.getLabelsMap().get(label));
        }
        return labels;
    }

    private void prepareTabs(){
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
                if (intent.getAction().equals(Broadcasts.FINISH_EDIT_SCAN)){
                    labelsListAdapter.updateList(getLabelsFromScan());
                    labelsListView.setAdapter(labelsListAdapter);
                }

            }
        };

        IntentFilter filter = new IntentFilter(Broadcasts.FINISH_EDIT_SCAN);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void execute() {

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

    private void setLastView(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setView("seeScanSavedView");
        mainActivity.setProblem(ScanSelected.getInstance().getProblemSelectedString());
        mainActivity.setScan(ScanSelected.getInstance().getScanSelectedString());
    }

}
