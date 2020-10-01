package com.cgaldo.brais.sistema.Model;

import android.view.View;
import java.util.Map;

public abstract class Handler {

    // Chain of Responsibility
    private Handler succesor;

    public Handler(){

    }

    public Handler(Handler succesor){
        this.succesor = succesor;
    }

    public void handleRequest(Integer requiredData, Map<String, View> placeToShow){
        // Forward successor (if any)
        if (succesor != null)
            succesor.handleRequest(requiredData, placeToShow);
    }

    public Object handleGetRequest(Integer requiredData, Integer requiredObject){
        // Forward successor (if any)
        if (succesor != null)
            return succesor.handleGetRequest(requiredData, requiredObject);

        return null;
    }

    public void handleSetRequest(Integer requiredData, Integer requiredObject, Object data){
        // Forward successor (if any)
        if (succesor != null)
            succesor.handleSetRequest(requiredData, requiredObject, data);
    }

    public boolean canHandleRequest(Integer requiredData){
        // Checking runtime conditions
        return false;
    }

}
