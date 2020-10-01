package com.cgaldo.brais.sistema.Model.InformationFragment;

import com.cgaldo.brais.sistema.R;

public class ProblemsViewInformation implements ViewInformationInterface {
    private Integer info = R.string.info_problems;

    //Singleton
    private static ProblemsViewInformation infoProblem;

    private ProblemsViewInformation(){

    }

    public static ProblemsViewInformation getInstance(){
        if (infoProblem == null){
            infoProblem = new ProblemsViewInformation();
        }

        return infoProblem;
    }

    @Override
    public Integer getInformation() {
        return info;
    }
}
