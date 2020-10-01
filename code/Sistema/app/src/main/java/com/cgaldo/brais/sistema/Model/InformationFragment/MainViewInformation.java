package com.cgaldo.brais.sistema.Model.InformationFragment;

//Class that returns information about of the MainActivity class

import com.cgaldo.brais.sistema.R;

public class MainViewInformation implements ViewInformationInterface {

    private Integer info = R.string.info_main;

    //Singleton
    private static MainViewInformation infoMain;

    private MainViewInformation(){

    }

    public static MainViewInformation getInstance(){
        if (infoMain == null){
            infoMain = new MainViewInformation();
        }

        return infoMain;
    }

    @Override
    public Integer getInformation() {
        return info;
    }
}
