package com.cgaldo.brais.sistema.Controller;

import com.cgaldo.brais.sistema.Controller.Commands.Command;

public class Invoker {

    // Commands to execute
    private Command retrieveDeviceInformationCommand;
    private Command retrieveDevideStatusCommand;
    private Command calibrateDeviceCommand;
    private Command scanSampleCommand;
    private Command configurateDeviceCommand;
    private Command resetDeviceCommand;
    private Command activateConfigurationDeviceCommand;

    private static Invoker invoker;

    private Invoker(Command retrieveDeviceInformationCommand, Command retrieveDevideStatusCommand, Command calibrateDeviceCommand,
                    Command scanSampleCommand, Command configurateDeviceCommand, Command resetDeviceCommand, Command activateConfigurationDeviceCommand){
        this.retrieveDeviceInformationCommand = retrieveDeviceInformationCommand;
        this.retrieveDevideStatusCommand = retrieveDevideStatusCommand;
        this.calibrateDeviceCommand = calibrateDeviceCommand;
        this.scanSampleCommand = scanSampleCommand;
        this.configurateDeviceCommand = configurateDeviceCommand;
        this.resetDeviceCommand = resetDeviceCommand;
        this.activateConfigurationDeviceCommand = activateConfigurationDeviceCommand;
    }

    public static Invoker getInstance(Command retrieveDeviceInformationCommand, Command retrieveDevideStatusCommand, Command calibrateDeviceCommand,
                                      Command scanSampleCommand, Command configurateDeviceCommand, Command resetDeviceCommand, Command activateConfigurationDeviceCommand){
        if (invoker == null){
            invoker = new Invoker(retrieveDeviceInformationCommand, retrieveDevideStatusCommand, calibrateDeviceCommand, scanSampleCommand, configurateDeviceCommand, resetDeviceCommand, activateConfigurationDeviceCommand);
        }

        return invoker;
    }

    // Command to execute
    public void retrieveDeviceInformation(){
        retrieveDeviceInformationCommand.execute();
    }

    public void retrieveDeviceStatus(){
        retrieveDevideStatusCommand.execute();
    }

    public void calibrateDevice(){
        calibrateDeviceCommand.execute();
    }

    public void scanSample(){
        scanSampleCommand.execute();
    }

    public void configurateDevice(){
        configurateDeviceCommand.execute();
    }

    public void resetDevice(){
        resetDeviceCommand.execute();
    }

    public void activateConfiguration(){
        activateConfigurationDeviceCommand.execute();
    }

}
