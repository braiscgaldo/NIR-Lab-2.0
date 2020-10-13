package com.cgaldo.brais.sistema.View;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.cgaldo.brais.sistema.Controller.Storage.Problem;
import com.cgaldo.brais.sistema.Controller.Storage.ScanSelected;
import com.cgaldo.brais.sistema.Controller.ViewsController.MainActivityController;
import com.cgaldo.brais.sistema.Controller.ViewsController.ProblemsController;
import com.cgaldo.brais.sistema.Model.InformationFragment.ScansSavedViewInformation;
import com.cgaldo.brais.sistema.R;
import com.cgaldo.brais.sistema.View.Fragments.MainFragment;
import com.cgaldo.brais.sistema.View.Fragments.Problems.ProblemsFragment;
import com.cgaldo.brais.sistema.View.Fragments.Problems.ScansSavedFragment;
import com.cgaldo.brais.sistema.View.Fragments.Spectrophotometer.SpectrometerFragment;

import java.util.List;

/*
    Main activity for starting the application
 */
public class MainActivity extends AppCompatActivity implements FragmentTemplate {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private String view;
    private String problem;
    private String scan;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.secondary_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }

        Fragment fragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).commit();

        if (savedInstanceState != null){

            String view = savedInstanceState.getString("View");
            String problem = savedInstanceState.getString("Problem");
            String scan = savedInstanceState.getString("Scan");

            ScanSelected.getInstance().setProblemSelectedString(problem);
            ScanSelected.getInstance().setScanSelectedString(scan);

            if (mTwoPane) {
                MainActivityController.getInstance(this).loadLandscape(view);
            }else{
                MainActivityController.getInstance(this).loadPortrait(view);
            }
        }

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if (mTwoPane){
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            takeBack(fragments.get(0));

        }else{

            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            takeBack(fragments.get(0));

        }

        MainActivityController.getInstance(this).backPressed(view);
    }

    private void takeBack(Fragment fragment){
        if (fragment.getClass().equals(ProblemsFragment.class)){
            view = "scansSavedView";
        }

        if (fragment.getClass().equals(ScansSavedFragment.class)){
            view = "seeScansSavedView";
        }

        if (fragment.getClass().equals(SpectrometerFragment.class)){
            view = "deviceInformationView";
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("View", view);
        outState.putString("Problem", problem);
        outState.putString("Scan", scan);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public void execute() {

    }

    @Override
    public void verticalLayout() {

    }

    @Override
    public void horizontalLayout() {

    }

    public void setView(String view){
        this.view = view;
    }

    public String getView(){
        return view;
    }

    public void setProblem(String problem){
        this.problem = problem;
    }

    public void setScan(String scan){
        this.scan = scan;
    }

}
