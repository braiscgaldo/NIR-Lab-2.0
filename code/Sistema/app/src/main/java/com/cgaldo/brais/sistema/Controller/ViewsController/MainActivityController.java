package com.cgaldo.brais.sistema.Controller.ViewsController;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cgaldo.brais.sistema.Controller.Storage.Problem;
import com.cgaldo.brais.sistema.Controller.Storage.ScanSelected;
import com.cgaldo.brais.sistema.R;
import com.cgaldo.brais.sistema.View.Fragments.MainFragment;
import com.cgaldo.brais.sistema.View.Fragments.Problems.ProblemsFragment;
import com.cgaldo.brais.sistema.View.Fragments.Problems.ScansSavedFragment;
import com.cgaldo.brais.sistema.View.Fragments.Problems.SeeScanSavedFragment;
import com.cgaldo.brais.sistema.View.Fragments.Scan.ScanFragment;
import com.cgaldo.brais.sistema.View.Fragments.Spectrophotometer.DeviceInformationFragment;
import com.cgaldo.brais.sistema.View.Fragments.Spectrophotometer.DeviceStatusFragment;
import com.cgaldo.brais.sistema.View.Fragments.Spectrophotometer.SpectrometerFragment;
import com.cgaldo.brais.sistema.View.MainActivity;

public class MainActivityController implements MainActivityControllerInterface {

    private static AppCompatActivity activity;

    private static MainActivityController mainActivityController;

    private MainActivityController(){
    }

    public static MainActivityController getInstance(AppCompatActivity act){
        if (mainActivityController == null){
            mainActivityController = new MainActivityController();
        }

        activity = act;

        return mainActivityController;
    }

    @Override
    public void loadPortrait(String view) {
        if (view.equals("mainView")){
            loadMain();
        }

        if (view.equals("problemsView")){
            loadProblemsP();
        }

        if (view.equals("scansSavedView")){
            loadSavedScansP();
        }

        if (view.equals("seeScanSavedView")){
            loadSeeSavedScanViewP();
        }

        if (view.equals("spectrometerView")){
            loadSpectrometerP();
        }

        if (view.equals("deviceInformationView")){
            loadSpectrometerP();
        }

        if (view.equals("deviceStatusView")){
            loadSpectrometerP();
        }

        if (view.equals("scanView")){
            loadMain();
        }
    }

    @Override
    public void loadLandscape(String view) {
        if (view.equals("mainView")){
            loadMain();
        }

        if (view.equals("problemsView")){
            loadProblemsL();
        }

        if (view.equals("scansSavedView")){
            loadSavedScansL();
        }

        if (view.equals("seeScanSavedView")){
            loadSeeSavedScanViewL();
        }

        if (view.equals("spectrometerView")){
            loadSpectrometerL();
        }

        if (view.equals("deviceInformationView")){
            loadSpectrometerL();
        }

        if (view.equals("deviceStatusView")){
            loadSpectrometerL();
        }

        if (view.equals("scanView")){
            loadMain();
        }
    }

    private void loadMain(){
        Fragment fragment = new MainFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).commit();
    }

    private void loadScanP(){
        Fragment fragment = new ScanFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).addToBackStack(null).commit();

    }

    private void loadScanL(){
        Fragment fragment = new ScanFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).addToBackStack(null).commit();

    }

    private void loadProblemsP(){

        Fragment fragment = new ProblemsFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).addToBackStack(null).commit();
    }

    private void loadProblemsL(){

        Fragment fragment = new ProblemsFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).addToBackStack(null).commit();
    }

    private void loadSavedScansP(){
        loadProblemsP();
        Fragment fragment1 = activity.getSupportFragmentManager().findFragmentById(R.id.main_container);
        activity.getSupportFragmentManager().beginTransaction().remove(fragment1).commit();
        Fragment fragment2 = activity.getSupportFragmentManager().findFragmentById(R.id.secondary_container);
        activity.getSupportFragmentManager().beginTransaction().remove(fragment2).commit();
        ProblemsController.getInstance().selectProblem(Problem.getInstance(), ScanSelected.getInstance().getProblemSelectedString());
        ScanSelected.getInstance().setProblemSelectedString(ScanSelected.getInstance().getProblemSelectedString());
        Fragment fragment = new ScansSavedFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).addToBackStack(null).commit();

    }

    private void loadSavedScansL(){
        loadProblemsL();
        ProblemsController.getInstance().selectProblem(Problem.getInstance(), ScanSelected.getInstance().getProblemSelectedString());
        ScanSelected.getInstance().setProblemSelectedString(ScanSelected.getInstance().getProblemSelectedString());

        activity.getSupportFragmentManager().executePendingTransactions();

        Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.general_layout);
        fragment.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();

        Fragment problemsFragment = new ProblemsFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container, problemsFragment).commit();
        Fragment scansSavedFragment = new ScansSavedFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.secondary_container, scansSavedFragment).commit();
    }

    private void loadSeeSavedScanViewP(){
        loadSavedScansP();
        Log.i("itemClick", "good file");
        SaveScanController.getInstance().selectScan(ScanSelected.getInstance().getScanSelectedString());
        Fragment fragment = new SeeScanSavedFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).addToBackStack(null).commit();

    }

    private void loadSeeSavedScanViewL(){
        loadSavedScansL();
        SaveScanController.getInstance().selectScan(ScanSelected.getInstance().getScanSelectedString());
        Fragment fragment = new SeeScanSavedFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).addToBackStack(null).commit();

    }

    private void loadSpectrometerP(){
        Fragment fragment = new SpectrometerFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).addToBackStack(null).commit();

    }

    private void loadSpectrometerL(){
        Fragment fragment = new SpectrometerFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).addToBackStack(null).commit();

    }

    private void loadDeviceInformationP(){
        loadSpectrometerP();
        Fragment fragment1 = activity.getSupportFragmentManager().findFragmentById(R.id.main_container);
        activity.getSupportFragmentManager().beginTransaction().remove(fragment1).commit();
        Fragment fragment2 = activity.getSupportFragmentManager().findFragmentById(R.id.secondary_container);
        activity.getSupportFragmentManager().beginTransaction().remove(fragment2).commit();
        ProblemsController.getInstance().selectProblem(Problem.getInstance(), ScanSelected.getInstance().getProblemSelectedString());
        ScanSelected.getInstance().setProblemSelectedString(ScanSelected.getInstance().getProblemSelectedString());
        Fragment fragment = new DeviceInformationFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).addToBackStack(null).commit();

    }

    private void loadDeviceInformationL(){
        loadSpectrometerL();

        activity.getSupportFragmentManager().executePendingTransactions();

        Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.general_layout);
        activity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        ProblemsController.getInstance().selectProblem(Problem.getInstance(), ScanSelected.getInstance().getProblemSelectedString());
        ScanSelected.getInstance().setProblemSelectedString(ScanSelected.getInstance().getProblemSelectedString());

        Fragment spectrometerFragment = new SpectrometerFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container, spectrometerFragment).commit();
        Fragment infoFragment = new DeviceInformationFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.secondary_container, infoFragment).commit();
    }

    private void loadDeviceStatusP(){
        loadSpectrometerP();
        Fragment fragment1 = activity.getSupportFragmentManager().findFragmentById(R.id.main_container);
        activity.getSupportFragmentManager().beginTransaction().remove(fragment1).commit();
        Fragment fragment2 = activity.getSupportFragmentManager().findFragmentById(R.id.secondary_container);
        activity.getSupportFragmentManager().beginTransaction().remove(fragment2).commit();
        ProblemsController.getInstance().selectProblem(Problem.getInstance(), ScanSelected.getInstance().getProblemSelectedString());
        ScanSelected.getInstance().setProblemSelectedString(ScanSelected.getInstance().getProblemSelectedString());
        Fragment fragment = new DeviceStatusFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.general_layout, fragment).addToBackStack(null).commit();
    }

    private void loadDeviceStatusL(){
        loadSpectrometerL();

        activity.getSupportFragmentManager().executePendingTransactions();

        Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.general_layout);
        activity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        ProblemsController.getInstance().selectProblem(Problem.getInstance(), ScanSelected.getInstance().getProblemSelectedString());
        ScanSelected.getInstance().setProblemSelectedString(ScanSelected.getInstance().getProblemSelectedString());

        Fragment spectrometerFragment = new SpectrometerFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container, spectrometerFragment).commit();
        Fragment statusFragment = new DeviceStatusFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.secondary_container, statusFragment).commit();

    }

    private void backView(String view){
        MainActivity mainActivity = (MainActivity) activity;
        mainActivity.setView(view);
    }

    @Override
    public void backPressed(String view){
        if (view.equals("mainView")){
            backView("mainView");
        }

        if (view.equals("problemsView")){
            backView("mainView");
        }

        if (view.equals("scansSavedView")){
            backView("problemsView");
        }

        if (view.equals("seeScanSavedView")){
            backView("scansSavedView");
        }

        if (view.equals("spectrometerView")){
            backView("mainView");
        }

        if (view.equals("deviceInformationView")){
            backView("spectrometerView");
        }

        if (view.equals("deviceStatusView")){
            backView("spectrometerView");
        }

        if (view.equals("scanView")){
            backView("mainView");
        }
    }

}
