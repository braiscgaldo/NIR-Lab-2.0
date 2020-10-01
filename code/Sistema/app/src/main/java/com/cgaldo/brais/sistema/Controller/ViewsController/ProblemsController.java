package com.cgaldo.brais.sistema.Controller.ViewsController;

import com.cgaldo.brais.sistema.Controller.Storage.ProblemInterface;
import com.cgaldo.brais.sistema.Controller.Storage.ScanSelected;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProblemsController implements ProblemsControllerInterface {

    private String problemName = "";

    private String output = "";

    private Integer numberTags = 0;

    private Map<String, List<String>> tags = new HashMap<>();

    //Singleton
    private static ProblemsController problemsController;

    private ProblemsController(){

    }

    public static ProblemsController getInstance(){
        if (problemsController == null){
            problemsController = new ProblemsController();
        }

        return problemsController;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Integer getNumberTags() {
        return numberTags;
    }

    public void setNumberTags(Integer numberTags) {
        this.numberTags = numberTags;
    }

    public Map<String, List<String>> getTags() {
        return tags;
    }

    public void setTags(Map<String, List<String>> tags) {
        this.tags = tags;
    }

    public void reset(){
        this.numberTags = 0;
        this.output = "";
        this.problemName = "";
        this.tags = new HashMap<>();
    }

    @Override
    public boolean selectProblem(ProblemInterface problemInterface, String problemName) {
        if (problemInterface.testProlem(problemName)){
            ScanSelected.getInstance().setProblemSelectedString(problemName);
            return true;
        }

        return false;

    }
}
