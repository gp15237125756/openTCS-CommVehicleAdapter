/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.commadapter.vehicle;

import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.opentcs.customizations.kernel.KernelInjectionModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcsKernelInjectionModule
    extends KernelInjectionModule {
  
  private static final Logger LOG = LoggerFactory.getLogger(TcsKernelInjectionModule.class);

  @Override
  protected void configure() {
    
    TcsCommAdapterConfiguration configuration
        = getConfigBindingProvider().get(TcsCommAdapterConfiguration.PREFIX,
                                         TcsCommAdapterConfiguration.class);
    
    if (!configuration.enable()) {
      LOG.info("Tcs communication adapter disabled by configuration.");
      return;
    }
    
    //install(new FactoryModuleBuilder().build(TcsAdapterComponentsFactory.class));
    //vehicleCommAdaptersBinder().addBinding().to(TcsCommAdapterFactory.class);
    install(new FactoryModuleBuilder().build(TcsHydrogenAdapterComponentsFactory.class));
    vehicleCommAdaptersBinder().addBinding().to(TcsHydrogenAdapterFactory.class);

  }
}
