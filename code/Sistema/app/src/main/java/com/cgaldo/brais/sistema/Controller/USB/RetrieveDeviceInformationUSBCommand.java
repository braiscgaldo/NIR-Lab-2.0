package com.cgaldo.brais.sistema.Controller.USB;

import com.cgaldo.brais.sistema.Controller.Commands.RetrieveDeviceInformationCommand;

public class RetrieveDeviceInformationUSBCommand implements RetrieveDeviceInformationCommand {

    // Singleton
    private static RetrieveDeviceInformationUSBCommand retrieveDeviceInformationUSBCommand;

    private RetrieveDeviceInformationUSBCommand(){

    }

    public static RetrieveDeviceInformationUSBCommand getInstance(){
        if (retrieveDeviceInformationUSBCommand == null){
            retrieveDeviceInformationUSBCommand = new RetrieveDeviceInformationUSBCommand();
        }

        return retrieveDeviceInformationUSBCommand;
    }

    @Override
    public void execute() {

    }
}
