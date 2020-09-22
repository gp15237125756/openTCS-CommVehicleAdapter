package de.fraunhofer.iml.opentcs.example.commadapter.vehicle;

import com.google.inject.assistedinject.Assisted;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange.TcsProcessModelTO;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.OrderRequest;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.StateResponse;
import de.fraunhofer.iml.opentcs.example.common.dispatching.LoadAction;
import de.fraunhofer.iml.opentcs.example.common.telegrams.BoundedCounter;
import de.fraunhofer.iml.opentcs.example.common.telegrams.Request;
import de.fraunhofer.iml.opentcs.example.common.telegrams.Response;
import de.fraunhofer.iml.opentcs.example.common.telegrams.TelegramSender;
import org.opentcs.contrib.tcp.netty.ConnectionEventListener;
import org.opentcs.customizations.kernel.KernelExecutor;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.DriveOrder;
import org.opentcs.drivers.vehicle.BasicVehicleCommAdapter;
import org.opentcs.drivers.vehicle.MovementCommand;
import org.opentcs.drivers.vehicle.VehicleProcessModel;
import org.opentcs.drivers.vehicle.management.VehicleProcessModelTO;
import org.opentcs.util.ExplainedBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static de.fraunhofer.iml.opentcs.example.common.telegrams.BoundedCounter.UINT16_MAX_VALUE;
import static java.util.Objects.requireNonNull;

public class TcsHydrogenAdapter  extends BasicVehicleCommAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(TcsHydrogenAdapter.class);

    /**
     * The kernel's executor service.
     */
    private final ExecutorService kernelExecutor;
    /**
     * Maps commands to order IDs so we know which command to report as finished.
     */
    private final Map<MovementCommand, Integer> orderIds = new ConcurrentHashMap<>();

    /**
     * @param vehicle
     * @param kernelExecutor
     */
    @Inject
    public TcsHydrogenAdapter(@Assisted Vehicle vehicle,
                          @KernelExecutor ExecutorService kernelExecutor) {
        super(new TcsProcessModel(vehicle), 3, 2, LoadAction.CHARGE, kernelExecutor);
        this.kernelExecutor = requireNonNull(kernelExecutor, "kernelExecutor");
    }

    @Override
    protected void connectVehicle() {
        System.out.println("连接next小车");
    }

    @Override
    protected void disconnectVehicle() {
        System.out.println("断开next小车");
    }

    @Override
    protected boolean isVehicleConnected() {
        return true;
    }

    @Override
    public synchronized void sendCommand(MovementCommand cmd)
            throws IllegalArgumentException {
        requireNonNull(cmd, "cmd");
        System.out.println("给next小车发送命令");
    }

    @Override
    public synchronized ExplainedBoolean canProcess(List<String> operations) {
        requireNonNull(operations, "operations");
        boolean canProcess = true;
        String reason = "";
        if (!isEnabled()) {
            canProcess = false;
            reason = "Adapter not enabled";
        }
        return new ExplainedBoolean(canProcess, reason);
    }

    @Override
    public void processMessage(@Nullable Object message) {
        System.out.println("给next小车发送命令");
    }

    @Override
    public final TcsProcessModel getProcessModel() {
        return (TcsProcessModel) super.getProcessModel();
    }

    @Override
    protected VehicleProcessModelTO createCustomTransferableProcessModel() {
        //Add extra information of the vehicle when sending to other software like control center or
        //plant overview
        return new TcsProcessModelTO()
                .setVehicleRef(getProcessModel().getVehicleReference())
                .setCurrentState(getProcessModel().getCurrentState())
                .setPreviousState(getProcessModel().getPreviousState())
                .setLastOrderSent(getProcessModel().getLastOrderSent())
                .setDisconnectingOnVehicleIdle(getProcessModel().isDisconnectingOnVehicleIdle())
                .setLoggingEnabled(getProcessModel().isLoggingEnabled())
                .setReconnectDelay(getProcessModel().getReconnectDelay())
                .setReconnectingOnConnectionLoss(getProcessModel().isReconnectingOnConnectionLoss())
                .setVehicleHost(getProcessModel().getVehicleHost())
                .setVehicleIdle(getProcessModel().isVehicleIdle())
                .setVehicleIdleTimeout(getProcessModel().getVehicleIdleTimeout())
                .setVehiclePort(getProcessModel().getVehiclePort())
                .setPeriodicStateRequestEnabled(getProcessModel().isPeriodicStateRequestEnabled())
                .setStateRequestInterval(getProcessModel().getStateRequestInterval());
    }
}
