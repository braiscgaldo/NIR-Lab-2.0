package com.cgaldo.brais.sistema.View.Adapter;

import java.util.List;

public interface ListAdapterInterface {

    void updateList(List<String> problemFiles);
    void add(String problem);
    List<String> getProblemFiles();
    List<String> getScans();
}
