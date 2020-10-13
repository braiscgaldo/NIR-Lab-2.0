package com.cgaldo.brais.sistema.Controller.USB;

import com.cgaldo.brais.sistema.Controller.Commands.CalibrateDeviceCommand;

public class CalibrateDeviceUSBCommand implements CalibrateDeviceCommand {

    // Singleton
    private static CalibrateDeviceUSBCommand calibrateDeviceUSBCommand;

    private CalibrateDeviceUSBCommand(){

    }

    public static CalibrateDeviceUSBCommand getInstance(){
        if (calibrateDeviceUSBCommand == null){
            calibrateDeviceUSBCommand = new CalibrateDeviceUSBCommand();
        }

        return calibrateDeviceUSBCommand;
    }
    @Override
    public void execute() {

    }
}
