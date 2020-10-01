package com.cgaldo.brais.sistema.Controller.ViewsController;

import java.util.List;

public interface ConfigurationsControllerInterface {

    Integer getCountConfigurations();
    List<Byte[]> getConfigurations();
    void getConfigurationData(byte[] data);
}
