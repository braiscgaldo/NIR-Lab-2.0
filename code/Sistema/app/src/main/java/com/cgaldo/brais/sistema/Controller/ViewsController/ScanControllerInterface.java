package com.cgaldo.brais.sistema.Controller.ViewsController;

import com.jjoe64.graphview.GraphView;

public interface ScanControllerInterface {

    void setData(GraphView graphView);
    void setLinesVisibles(GraphView graphView, Integer parameter);
    void computeScan();
}
