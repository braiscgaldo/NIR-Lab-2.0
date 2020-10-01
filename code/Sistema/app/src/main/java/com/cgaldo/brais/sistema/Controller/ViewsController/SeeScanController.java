package com.cgaldo.brais.sistema.Controller.ViewsController;

public class SeeScanController implements SeeScanControllerInterface {

    private static SeeScanController seeScanController;

    private SeeScanController(){

    }

    public static SeeScanController getInstance(){
        if (seeScanController == null){
            seeScanController = new SeeScanController();
        }

        return seeScanController;
    }
}
