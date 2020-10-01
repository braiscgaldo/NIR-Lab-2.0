package com.cgaldo.brais.sistema.Controller.ViewsController;

import com.cgaldo.brais.sistema.Controller.Storage.Problem;
import com.cgaldo.brais.sistema.Controller.Storage.ProblemInterface;
import com.cgaldo.brais.sistema.Controller.Storage.ScanSelected;

public class SaveScanController implements SaveScanControllerInterface {

    //Singleton
    private static SaveScanController saveScanController;

    private SaveScanController(){

    }

    public static SaveScanController getInstance(){
        if (saveScanController == null){
            saveScanController = new SaveScanController();
        }

        return saveScanController;
    }


    @Override
    public void selectScan(String nameScan) {
        ScanSelected.getInstance().setScanSelectedString(nameScan);
        ScanSelected.getInstance().setLabelsMap(Problem.getInstance().takeLabelsFromProblems(ScanSelected.getInstance().getProblemSelectedString(), ScanSelected.getInstance().getScanSelectedString()));
    }
}
