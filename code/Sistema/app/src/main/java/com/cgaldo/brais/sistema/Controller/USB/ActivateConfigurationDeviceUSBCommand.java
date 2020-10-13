package com.cgaldo.brais.sistema.Controller.USB;

import com.cgaldo.brais.sistema.Controller.Commands.ActivateConfigurationDeviceCommand;

public class ActivateConfigurationDeviceUSBCommand implements ActivateConfigurationDeviceCommand {

    private byte[] configurationToActive;

    // Singleton
    private static ActivateConfigurationDeviceUSBCommand activateConfigurationDeviceUSBCommand;

    private ActivateConfigurationDeviceUSBCommand(){

    }

    public static ActivateConfigurationDeviceUSBCommand getInstance(){
        if (activateConfigurationDeviceUSBCommand == null){
            activateConfigurationDeviceUSBCommand = new ActivateConfigurationDeviceUSBCommand();
        }

        return activateConfigurationDeviceUSBCommand;
    }

    public byte[] getConfigurationToActive() {
        return configurationToActive;
    }

    public void setConfigurationToActive(byte[] configurationToActive) {
        this.configurationToActive = configurationToActive;
    }

    @Override
    public void execute() {

    }
}
