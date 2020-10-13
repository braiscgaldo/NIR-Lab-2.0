package com.cgaldo.brais.sistema.Controller.USB;

import com.cgaldo.brais.sistema.Controller.Commands.ScanSampleCommand;

public class ScanSampleUSBCommand implements ScanSampleCommand {

    // Singleton
    private static ScanSampleUSBCommand scanSampleUSBCommand;

    private ScanSampleUSBCommand(){

    }

    public static ScanSampleUSBCommand getInstance(){
        if (scanSampleUSBCommand == null){
            scanSampleUSBCommand = new ScanSampleUSBCommand();
        }

        return scanSampleUSBCommand;
    }

    @Override
    public void execute() {

    }
}
