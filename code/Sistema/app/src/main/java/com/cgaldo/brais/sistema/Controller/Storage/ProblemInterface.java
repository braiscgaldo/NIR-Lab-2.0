package com.cgaldo.brais.sistema.Controller.Storage;

import com.cgaldo.brais.sistema.Model.Handler;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ProblemInterface {

    boolean canReadAndWrite();

    boolean existsProblems();

    List<String> obtainProblems();

    File createProblem(String nameProblem, String output, Map<String, List<String>> tags);

    boolean testProlem(String nameProblem);
    boolean removeProblem(String nameProblem);

    boolean addScan(String nameProblem, String nameScan, Handler data, Map<String, String> labelsValue);

    boolean testScan(String nameProblem, String nameScan);
    boolean removeScan(String nameProblem, String nameScan);
    boolean existsScan(String nameProblem, String nameScan);
    List<String> obtainScans(String nameProblem);
    boolean putScan(String nameProblem, String nameScan, Handler handler);

    boolean editScan(String nameProblem, String nameScan, Handler data, Map<String, String> labelsValue);

    Map<String, List<String>> takeLabels(String nameProblem);

    Map<String, String> takeLabelsFromProblems(String nameProblem, String nameScan);

}
