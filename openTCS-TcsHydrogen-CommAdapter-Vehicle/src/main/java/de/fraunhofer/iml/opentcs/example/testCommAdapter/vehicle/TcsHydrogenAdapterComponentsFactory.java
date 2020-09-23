/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.testCommAdapter.vehicle;

import de.fraunhofer.iml.opentcs.example.common.telegrams.RequestResponseMatcher;
import de.fraunhofer.iml.opentcs.example.common.telegrams.StateRequesterTask;
import de.fraunhofer.iml.opentcs.example.common.telegrams.TelegramSender;
import org.opentcs.data.model.Vehicle;

import java.awt.event.ActionListener;

/**
 * A factory for various instances specific to the comm adapter.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public interface TcsHydrogenAdapterComponentsFactory {

  /**
   * Creates a new TcsCommAdapter for the given vehicle.
   *
   * @param vehicle The vehicle
   * @return A new TcsHydrogenAdapter for the given vehicle
   */
  TcsHydrogenAdapter createTcsCommAdapter(Vehicle vehicle);

  /**
   * Creates a new {@link RequestResponseMatcher}.
   *
   * @param telegramSender Sends telegrams/requests.
   * @return The created {@link RequestResponseMatcher}.
   */
  //RequestResponseMatcher createRequestResponseMatcher(TelegramSender telegramSender);

  /**
   * Creates a new {@link StateRequesterTask}.
   *
   * @param stateRequestAction The actual action to be performed to enqueue requests.
   * @return The created {@link StateRequesterTask}.
   */
  //StateRequesterTask createStateRequesterTask(ActionListener stateRequestAction);
}
