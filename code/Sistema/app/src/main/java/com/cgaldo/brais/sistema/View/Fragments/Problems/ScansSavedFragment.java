package com.cgaldo.brais.sistema.View.Fragments.Problems;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cgaldo.brais.sistema.Controller.Storage.Problem;
import com.cgaldo.brais.sistema.Controller.Storage.ProblemInterface;
import com.cgaldo.brais.sistema.Controller.Storage.ScanSelected;
import com.cgaldo.brais.sistema.Controller.ViewsController.SaveScanController;
import com.cgaldo.brais.sistema.Controller.ViewsController.SaveScanControllerInterface;
import com.cgaldo.brais.sistema.Model.InformationFragment.ScansSavedViewInformation;
import com.cgaldo.brais.sistema.Model.InformationFragment.ViewInformationInterface;
import com.cgaldo.brais.sistema.R;
import com.cgaldo.brais.sistema.View.FragmentTemplate;
import com.cgaldo.brais.sistema.View.Adapter.ScanListAdapter;
import com.cgaldo.brais.sistema.View.Dialogs.Dialogs;
import com.cgaldo.brais.sistema.View.Dialogs.DialogsInterface;
import com.cgaldo.brais.sistema.View.MainActivity;


public class ScansSavedFragment extends Fragment implements FragmentTemplate {

    // TextView
    private TextView noScansTv;

    // Information
    private ViewInformationInterface informationInterface = ScansSavedViewInformation.getInstance();

    //Button
    private ImageButton infoButton;

    // ListView
    private ListView scansListView;
    private ScanListAdapter scansListAdapter;

    // ProblemsInterface
    private ProblemInterface problemInterface;
    private SaveScanControllerInterface saveScanControllerInterface;

    // Swipe Layout
    private SwipeRefreshLayout swipeRefreshLayout;

    // String problemName
    private String problemName;

    // Dialog
    private DialogsInterface dialogs = Dialogs.getInstance();

    // View
    private View rootView;

    public ScansSavedFragment(){
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
        rootView = inflater.inflate(R.layout.fragment_scans_saved, container, false);

        verticalLayout();
        setLastView();


        return rootView;
    }

    @Override
    public void verticalLayout(){
        // We get the items
        getItems();

        buttonActionsVertical();

        // We test data
        testData();

        // We prepare the layout
        prepareLayout();
    }

    @Override
    public void horizontalLayout() {
        // We get the items
        getItems();

        buttonActionsHorizontal();

        // We test data
        testData();

        // We prepare the layout
        prepareLayout();
    }

    @Override
    public void execute() {

    }

    // Attach items
    private void getItems(){
        this.problemInterface = Problem.getInstance();
        this.problemName = ScanSelected.getInstance().getProblemSelectedString();
        this.noScansTv = rootView.findViewById(R.id.noScansView);
        this.scansListView = rootView.findViewById(R.id.scansList);
        this.swipeRefreshLayout = rootView.findViewById(R.id.swipeScanLayout);
        this.saveScanControllerInterface = SaveScanController.getInstance();
        TextView title = rootView.findViewById(R.id.title);
        String titleString = ScanSelected.getInstance().getProblemSelectedString() + " Problem";
        title.setText(titleString);
        this.scansListAdapter = ScanListAdapter.getInstance(getContext(), problemInterface.obtainScans(problemName));
        this.scansListView.setAdapter(this.scansListAdapter);
        this.scansListView.setClickable(true);
        this.infoButton = rootView.findViewById(R.id.infoButton);

    }

    private void buttonActionsVertical(){
        // See scan
        this.scansListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (problemInterface.testScan(problemName, scansListView.getItemAtPosition(position).toString())){
                    Log.i("itemClick", "good file");
                    saveScanControllerInterface.selectScan(scansListView.getItemAtPosition(position).toString());
                    Fragment fragment = new SeeScanSavedFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).addToBackStack(null).commit();

                }else{
                    Log.i("itemClick", "bad file");
                }
                Log.i("itemclick", "item click " + String.valueOf(position));
            }
        });

        // Delete scan
        this.scansListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogs.createDialogDeleteScan(getContext(), ScanSelected.getInstance().getProblemSelectedString(), scansListView.getItemAtPosition(position).toString(), noScansTv, scansListAdapter);
                return true;
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.createInformationDialog(getContext(), "Scans Saved Information", getString(informationInterface.getInformation()));
            }
        });

    }

    private void buttonActionsHorizontal(){
        // See scan
        this.scansListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (problemInterface.testScan(problemName, scansListView.getItemAtPosition(position).toString())){
                    Log.i("itemClick", "good file");
                    saveScanControllerInterface.selectScan(scansListView.getItemAtPosition(position).toString());
                    // Change view logic
                    Fragment problems = getFragmentManager().findFragmentById(R.id.main_container);
                    getActivity().getSupportFragmentManager().beginTransaction().remove(problems).commit();
                    Fragment scans = getFragmentManager().findFragmentById(R.id.secondary_container);
                    getActivity().getSupportFragmentManager().beginTransaction().remove(scans).commit();

                    Fragment fragment = new SeeScanSavedFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).addToBackStack(null).commit();

                }else{
                    Log.i("itemClick", "bad file");
                }
                Log.i("itemclick", "item click " + String.valueOf(position));
            }
        });

        // Delete scan
        this.scansListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogs.createDialogDeleteScan(getContext(), ScanSelected.getInstance().getProblemSelectedString(), scansListView.getItemAtPosition(position).toString(), noScansTv, scansListAdapter);
                return true;
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.createInformationDialog(getContext(), "Scans Saved Information", getString(informationInterface.getInformation()));
            }
        });

    }

    //Prepare layout
    private void prepareLayout(){
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                testData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    // Probe data
    private void testData(){
        // If there are data to show
        if (!problemInterface.canReadAndWrite() || !problemInterface.existsProblems()){
            this.scansListView.setVisibility(View.GONE);
        }else{
            prepareList();
        }
    }

    // Prepare list
    private void prepareList(){
        this.noScansTv.setVisibility(View.GONE);
        this.scansListAdapter.updateList(problemInterface.obtainScans(problemName));
        this.scansListAdapter.notifyDataSetChanged();
    }

    private void setLastView(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setView("scansSavedView");
        mainActivity.setProblem(ScanSelected.getInstance().getProblemSelectedString());
    }



}
