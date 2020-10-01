package com.cgaldo.brais.sistema.Controller.ViewsController;

import android.content.Context;

public interface ScanConfigurationControllerInterface {

    boolean setConfigurationScanData(Context context, String name, String minRange, String maxRange, String wavePoints, Boolean repeatScan);

    void activeConfiguration();

    void treatConfiguration();
}
