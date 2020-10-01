package com.cgaldo.brais.sistema.Controller.ViewsController;

import com.cgaldo.brais.sistema.Controller.Storage.ProblemInterface;

public interface ProblemsControllerInterface {

    boolean selectProblem(ProblemInterface problemInterface, String problemName);
}
