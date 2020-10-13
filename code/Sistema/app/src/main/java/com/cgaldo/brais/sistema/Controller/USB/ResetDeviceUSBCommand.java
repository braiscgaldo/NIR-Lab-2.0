package com.cgaldo.brais.sistema.Controller.USB;

import com.cgaldo.brais.sistema.Controller.Commands.ResetDeviceCommand;

public class ResetDeviceUSBCommand implements ResetDeviceCommand {

    // Singleton
    private static ResetDeviceUSBCommand resetDeviceUSBCommand;

    private ResetDeviceUSBCommand(){

    }

    public static ResetDeviceUSBCommand getInstance(){
        if (resetDeviceUSBCommand == null){
            resetDeviceUSBCommand = new ResetDeviceUSBCommand();
        }

        return resetDeviceUSBCommand;
    }

    @Override
    public void execute() {



    }
}
