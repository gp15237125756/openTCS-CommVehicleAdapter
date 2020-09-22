package de.fraunhofer.iml.opentcs.example.commadapter.vehicle;

import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange.TcsHydrogenAdapterDescription;
import de.fraunhofer.iml.opentcs.example.common.VehicleProperties;
import org.opentcs.data.model.Vehicle;
import org.opentcs.drivers.vehicle.VehicleCommAdapter;
import org.opentcs.drivers.vehicle.VehicleCommAdapterDescription;
import org.opentcs.drivers.vehicle.VehicleCommAdapterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static de.fraunhofer.iml.opentcs.example.common.VehicleProperties.PROPKEY_VEHICLE_HOST;
import static de.fraunhofer.iml.opentcs.example.common.VehicleProperties.PROPKEY_VEHICLE_PORT;
import static java.util.Objects.requireNonNull;
import static org.opentcs.util.Assertions.checkInRange;

public class TcsHydrogenAdapterFactory implements VehicleCommAdapterFactory {

    private static final Logger LOG = LoggerFactory.getLogger(TcsHydrogenAdapterFactory.class);

    private boolean initialized;

    /**
     * The factory to create components specific to the comm adapter.
     */
    private final TcsHydrogenAdapterComponentsFactory componentsFactory;

    @Inject
    public TcsHydrogenAdapterFactory(TcsHydrogenAdapterComponentsFactory componentsFactory) {
        this.componentsFactory = requireNonNull(componentsFactory, "componentsFactory");
    }
    @Override
    public VehicleCommAdapterDescription getDescription() {
        return new TcsHydrogenAdapterDescription();
    }

    @Override
    public boolean providesAdapterFor(@Nonnull Vehicle vehicle) {
        requireNonNull(vehicle, "vehicle");

        if (vehicle.getProperty(PROPKEY_VEHICLE_HOST) == null) {
            return false;
        }

        if (vehicle.getProperty(VehicleProperties.PROPKEY_VEHICLE_PORT) == null) {
            return false;
        }
        try {
            checkInRange(Integer.parseInt(vehicle.getProperty(PROPKEY_VEHICLE_PORT)),
                    1024,
                    65535);
        }
        catch (IllegalArgumentException exc) {
            return false;
        }

        return true;
    }

    @Nullable
    @Override
    public VehicleCommAdapter getAdapterFor(@Nonnull Vehicle vehicle) {
        requireNonNull(vehicle, "vehicle");
        if (!providesAdapterFor(vehicle)) {
            return null;
        }

        TcsHydrogenAdapter adapter = componentsFactory.createTcsCommAdapter(vehicle);
        adapter.getProcessModel().setVehicleHost(vehicle.getProperty(PROPKEY_VEHICLE_HOST));
        adapter.getProcessModel().setVehiclePort(
                Integer.parseInt(vehicle.getProperty(PROPKEY_VEHICLE_PORT))
        );
        return adapter;
    }

    @Override
    public void initialize() {
        if (initialized) {
            LOG.debug("Already initialized.");
            return;
        }
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void terminate() {
        if (!initialized) {
            LOG.debug("Not initialized.");
            return;
        }
        initialized = false;
    }
}
