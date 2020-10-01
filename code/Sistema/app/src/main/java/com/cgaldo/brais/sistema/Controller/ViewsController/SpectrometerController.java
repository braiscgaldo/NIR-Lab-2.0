package com.cgaldo.brais.sistema.Controller.ViewsController;

import com.cgaldo.brais.sistema.Model.InformationFragment.SpectrometerViewInformation;

public class SpectrometerController implements SpectrometerControllerInterface {

    // Singleton
    private static SpectrometerController spectrometerController;

    private SpectrometerController(){

    }

    public static SpectrometerController getInstance(){
        if (spectrometerController == null){
            spectrometerController = new SpectrometerController();
        }

        return spectrometerController;
    }

    @Override
    public String showInfo() {
        return SpectrometerViewInformation.getInstance().toString();
    }
}
