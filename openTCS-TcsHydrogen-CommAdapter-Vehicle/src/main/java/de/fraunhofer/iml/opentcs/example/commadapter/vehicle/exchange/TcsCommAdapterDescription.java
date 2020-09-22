/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange;

import java.util.ResourceBundle;
import org.opentcs.drivers.vehicle.VehicleCommAdapterDescription;

/**
 * The comm adapter's {@link VehicleCommAdapterDescription}.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class TcsCommAdapterDescription
    extends VehicleCommAdapterDescription {

  @Override
  public String getDescription() {
    return ResourceBundle.getBundle("de/fraunhofer/iml/opentcs/example/commadapter/vehicle/Bundle").
        getString("TcsAdapterFactoryDescription");
  }

  @Override
  public boolean isSimVehicleCommAdapter() {
    return false;
  }
}
