/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.testCommAdapter.vehicle.exchange;

import org.opentcs.drivers.vehicle.VehicleCommAdapterDescription;

/**
 * The comm adapter's {@link VehicleCommAdapterDescription}.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class TcsHydrogenAdapterDescription
    extends VehicleCommAdapterDescription {

  @Override
  public String getDescription() {
    return "TCS hydrogen adapter";
  }

  @Override
  public boolean isSimVehicleCommAdapter() {
    return false;
  }
}
