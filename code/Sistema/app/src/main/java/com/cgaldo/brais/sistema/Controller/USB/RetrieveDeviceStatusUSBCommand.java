package com.cgaldo.brais.sistema.Controller.USB;

import com.cgaldo.brais.sistema.Controller.Commands.RetrieveDeviceStatusCommand;

public class RetrieveDeviceStatusUSBCommand implements RetrieveDeviceStatusCommand {

    //Singleton
    private static RetrieveDeviceStatusUSBCommand retrieveDeviceStatusUSBCommand;

    private RetrieveDeviceStatusUSBCommand(){

    }

    public static RetrieveDeviceStatusUSBCommand getInstance(){
        if (retrieveDeviceStatusUSBCommand == null){
            retrieveDeviceStatusUSBCommand = new RetrieveDeviceStatusUSBCommand();
        }

        return retrieveDeviceStatusUSBCommand;
    }
    @Override
    public void execute() {

    }
}
