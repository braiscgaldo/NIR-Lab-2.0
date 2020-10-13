package com.cgaldo.brais.sistema.Controller.USB;

import com.cgaldo.brais.sistema.Controller.Commands.ConfigurateDeviceCommand;

public class ConfigurateDeviceUSBCommand implements ConfigurateDeviceCommand {

    // Singleton
    private static ConfigurateDeviceUSBCommand configurateDeviceUSBCommand;

    private ConfigurateDeviceUSBCommand(){

    }

    public static ConfigurateDeviceUSBCommand getInstance(){
        if (configurateDeviceUSBCommand == null){
            configurateDeviceUSBCommand = new ConfigurateDeviceUSBCommand();
        }

        return configurateDeviceUSBCommand;
    }
    @Override
    public void execute() {

    }
}
