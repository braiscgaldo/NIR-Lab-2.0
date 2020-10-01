package com.cgaldo.brais.sistema.View.Fragments.Problems;

import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
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
import com.cgaldo.brais.sistema.Controller.ViewsController.ProblemsController;
import com.cgaldo.brais.sistema.Model.InformationFragment.ProblemsViewInformation;
import com.cgaldo.brais.sistema.Model.InformationFragment.ViewInformationInterface;
import com.cgaldo.brais.sistema.R;
import com.cgaldo.brais.sistema.View.FragmentTemplate;
import com.cgaldo.brais.sistema.View.Adapter.ProblemsListAdapter;
import com.cgaldo.brais.sistema.View.Dialogs.Dialogs;
import com.cgaldo.brais.sistema.View.Dialogs.DialogsInterface;
import com.cgaldo.brais.sistema.View.MainActivity;


public class ProblemsFragment extends Fragment implements FragmentTemplate {

    // TextView
    private TextView noProblemsTv;

    // Information
    private ViewInformationInterface informationInterface = ProblemsViewInformation.getInstance();

    // Button
    private FloatingActionButton addProblemButton;
    private ImageButton infoButton;

    // ListView
    private ListView problemsListView;
    private ProblemsListAdapter problemsListAdapter;

    // ProblemsInterface
    private ProblemInterface problemInterface;
    private ProblemsController problemsController;

    // Swipe Layout
    private SwipeRefreshLayout swipeRefreshLayout;

    // Dialogs
    private DialogsInterface dialogs = Dialogs.getInstance();

    // View Fragment
    private View rootView;

    // Two panel
    private boolean mTwoPane;

    public ProblemsFragment(){
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
        rootView = inflater.inflate(R.layout.fragment_problems, container, false);
        setLastView();

        //Test if landscape
        if (getActivity().findViewById(R.id.secondary_container) != null){
            mTwoPane = true;
        }else{
            mTwoPane = false;
        }

        if (mTwoPane){
            horizontalLayout();
        }else {
            verticalLayout();
        }

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

        // Actions for addProblemButton
        addProblemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.createAddProblemDialog(getContext());
            }
        });
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

        // Actions for addProblemButton
        addProblemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.createAddProblemDialog(getContext());
            }
        });
    }

    @Override
    public void execute() {

    }

    // Attach items
    private void getItems(){
        this.problemInterface = Problem.getInstance();
        this.noProblemsTv = rootView.findViewById(R.id.noProblemsView);
        this.problemsListView = rootView.findViewById(R.id.problemsList);
        this.addProblemButton = rootView.findViewById(R.id.addProblemButton);
        this.swipeRefreshLayout = rootView.findViewById(R.id.swipeLayout);
        this.problemsController = ProblemsController.getInstance();
        this.problemsListAdapter = ProblemsListAdapter.getInstance(getContext(), problemInterface.obtainProblems());
        this.problemsListView.setAdapter(this.problemsListAdapter);
        this.problemsListView.setClickable(true);
        this.infoButton = rootView.findViewById(R.id.infoButton);

    }

    private void buttonActionsVertical(){
        //Access to a problem
        this.problemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (problemInterface.testProlem(problemsListView.getItemAtPosition(position).toString())){
                    Log.i("itemClick", "good file");
                    problemsController.selectProblem(problemInterface, problemsListView.getItemAtPosition(position).toString());
                    ScanSelected.getInstance().setProblemSelectedString(problemsListView.getItemAtPosition(position).toString());
                    Fragment fragment = new ScansSavedFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), fragment).addToBackStack(null).commit();

                }else{
                    Log.i("itemClick", "bad file");
                }
                Log.i("itemclick", "item click " + String.valueOf(position));
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.createInformationDialog(getContext(), "Problems Information", getString(informationInterface.getInformation()));
            }
        });

        // Delete a problem
        this.problemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogs.createDialogDeleteProblem(getContext(), problemsListView.getItemAtPosition(position).toString(), noProblemsTv, problemsListAdapter);
                return true;
            }
        });
    }

    private void buttonActionsHorizontal(){
        //Access to a problem
        this.problemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (problemInterface.testProlem(problemsListView.getItemAtPosition(position).toString())){
                    Log.i("itemClick", "good file");
                    problemsController.selectProblem(problemInterface, problemsListView.getItemAtPosition(position).toString());
                    ScanSelected.getInstance().setProblemSelectedString(problemsListView.getItemAtPosition(position).toString());

                    // Change view logic
                    Fragment f = getFragmentManager().findFragmentById(R.id.general_layout);
                    getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();

                    Fragment problemsFragment = new ProblemsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, problemsFragment).commit();
                    Fragment scansSavedFragment = new ScansSavedFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.secondary_container, scansSavedFragment).commit();

                }else{
                    Log.i("itemClick", "bad file");
                }
                Log.i("itemclick", "item click " + String.valueOf(position));
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.createInformationDialog(getContext(), "Problems Information", getString(informationInterface.getInformation()));
            }
        });

        // Delete a problem
        this.problemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogs.createDialogDeleteProblem(getContext(), problemsListView.getItemAtPosition(position).toString(), noProblemsTv, problemsListAdapter);
                return true;
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
            this.problemsListView.setVisibility(View.GONE);
        }else{
            prepareList();
        }
    }

    // Prepare list
    private void prepareList(){
        this.noProblemsTv.setVisibility(View.GONE);
        this.problemsListAdapter.updateList(problemInterface.obtainProblems());
        this.problemsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {


    }

    private void setLastView(){
        MainActivity main =  (MainActivity) getActivity();
        main.setView("problemsView");
    }


}
