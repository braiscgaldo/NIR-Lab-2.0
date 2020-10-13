package com.cgaldo.brais.sistema.Controller.ViewsController;


import android.os.Environment;

import com.cgaldo.brais.sistema.Controller.Bluetooth.BluetoothController;
import com.cgaldo.brais.sistema.Controller.ConnectionsController;
import com.cgaldo.brais.sistema.Controller.USB.USBController;
import com.cgaldo.brais.sistema.Model.InformationFragment.MainViewInformation;
import com.cgaldo.brais.sistema.Model.StaticData.Storage;

import java.io.File;

public class MainController implements MainControllerInterface {

    // Singleton
    private static MainController mainController;

    private MainController(){

    }

    public static MainController getInstance(){
        if (mainController == null){
            mainController = new MainController();
        }

        return mainController;
    }

    @Override
    public String showInfo() {
        return MainViewInformation.getInstance().toString();
    }

    @Override
    public boolean prepareApplicationStorage() {
        // We check if the external storage is available
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return this.prepareDirectories();
        }

        return false;
    }

    boolean prepareDirectories(){
        // We create the files
        File documentsDirectory = new File(Environment.getExternalStorageDirectory(),"Documents/");
        File parentDirectory = new File(Environment.getExternalStorageDirectory(),"Documents/" + Storage.PARENT_DIRECTORY);
        File problemsDirectory = new File(Environment.getExternalStorageDirectory(),"Documents/" + Storage.PARENT_DIRECTORY + '/' + Storage.PROBLEMS_DIRECTORY);
        File modelsDirectory = new File(Environment.getExternalStorageDirectory(),"Documents/" + Storage.PARENT_DIRECTORY + '/' + Storage.MODELS_DIRECTORY);


        // We check if directory already exists
        if (!documentsDirectory.exists()) {
            if (!documentsDirectory.mkdir()){
                return false;
            }
        }

        if (parentDirectory.exists()){
            if (!problemsDirectory.exists()){
                if (!problemsDirectory.mkdir()){
                    return false;
                }
            }

            if (!modelsDirectory.exists()){
                if (!modelsDirectory.mkdir()){
                    return false;
                }
            }

            return true;
        }


        // If not exists, we create them
        if (parentDirectory.mkdir()){

            if (!problemsDirectory.mkdir()){
                return false;
            }

            if (!modelsDirectory.mkdir()){
                return false;
            }

            return true;
        }

        return false;
    }

    @Override
    public ConnectionsController manageControllers(boolean isUsbConnected){
        if (isUsbConnected){
            return USBController.getInstance();
        }
        return BluetoothController.getInstance();
    }

}
