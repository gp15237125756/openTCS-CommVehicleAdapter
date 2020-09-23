package de.fraunhofer.iml.opentcs.example.testCommAdapter.vehicle;

import com.google.inject.assistedinject.Assisted;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.TcsProcessModel;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange.TcsProcessModelTO;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.StateResponse;
import de.fraunhofer.iml.opentcs.example.common.dispatching.LoadAction;
import org.opentcs.customizations.kernel.KernelExecutor;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.Route;
import org.opentcs.drivers.vehicle.BasicVehicleCommAdapter;
import org.opentcs.drivers.vehicle.MovementCommand;
import org.opentcs.drivers.vehicle.management.VehicleProcessModelTO;
import org.opentcs.util.ExplainedBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import static java.util.Objects.requireNonNull;

public class TcsHydrogenAdapter  extends BasicVehicleCommAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(TcsHydrogenAdapter.class);

    private TcsHydrogenAdapterComponentsFactory tcsHydrogenAdapterComponentsFactory;

    private Vehicle vehicle;

    private boolean initialized;
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
    public TcsHydrogenAdapter(@Assisted Vehicle vehicle,TcsHydrogenAdapterComponentsFactory factory,
                          @KernelExecutor ExecutorService kernelExecutor) {
        super(new TestVehicleModel(vehicle), 3, 2, LoadAction.CHARGE, kernelExecutor);
        this.kernelExecutor = requireNonNull(kernelExecutor, "kernelExecutor");
        this.tcsHydrogenAdapterComponentsFactory = factory;
        this.vehicle = vehicle;
    }

    protected void runActualTask() {
        try {
            //获取状态  位置  速度  反向
            final MovementCommand curCommand;
            synchronized (TcsHydrogenAdapter.this) {
                curCommand = getSentQueue().peek();
            }
            final Route.Step curStep = curCommand.getStep();
            //运行Step，略
            if (!curCommand.isWithoutOperation()) {
                //运行操作（上料或者下料，略）
            }
            if (getSentQueue().size() <= 1 && getCommandQueue().isEmpty()) {
                getProcessModel().setVehicleState(Vehicle.State.IDLE);
            }
            //更新UI
            synchronized (TcsHydrogenAdapter.this) {
                MovementCommand sentCmd = getSentQueue().poll();
                if (sentCmd != null && sentCmd.equals(curCommand)) {
                    getProcessModel().commandExecuted(curCommand);
                    TcsHydrogenAdapter.this.notify();
                }
            }
        }
        catch (Exception ex) {
        }
    }

    @Override
    protected void connectVehicle() {
        System.out.println("连接next小车");
        //connectVehicle();
    }

    @Override
    protected void disconnectVehicle() {
        System.out.println("断开next小车");
        //disconnectVehicle();
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
    public final TestVehicleModel getProcessModel() {
        return (TestVehicleModel) super.getProcessModel();
    }

    @Nonnull
    @Override
    public ExplainedBoolean canProcess(@Nonnull List<String> operations) {
        requireNonNull(operations, "operations");
        boolean canProcess = true;
        String reason = "";
        if (!isEnabled()) {
            canProcess = false;
            reason = "Adapter not enabled";
        }
        if (canProcess && !isVehicleConnected()) {
            canProcess = false;
            reason = "Vehicle does not seem to be connected";
        }
        return new ExplainedBoolean(canProcess, reason);
    }

    @Override
    public void processMessage(@Nullable Object message) {
        System.out.println("给next小车发送命令");
    }

}
