package com.cgaldo.brais.sistema.Controller.ViewsController;


import com.cgaldo.brais.sistema.Controller.ConnectionsController;

public interface MainControllerInterface {

    //Methods
    String showInfo();

    boolean prepareApplicationStorage();

    ConnectionsController manageControllers(boolean isUsbConnected);

}
