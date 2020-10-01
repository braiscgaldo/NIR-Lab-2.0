package com.cgaldo.brais.sistema.Controller.JNICalls;

public class JNICall {

    static {
        System.loadLibrary("native-lib");
    }

    public JNICall(){

    }

    // JNI function for interpret configuration
    public native Object dlpSpecScanReadConfiguration(byte[] data);

    // JNI functione for interpret scan
    public native Object dlpSpecScanInterpReference(byte[] data, byte[] coeff, byte[] matrix);
}
