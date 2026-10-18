package carrental.demand;

import java.util.Random;

public class SimulatedFleetDemandProvider implements FleetDemandProvider {
    private final Random random = new Random();

    @Override
    public double getOccupancyRate() {
        return random.nextDouble();
    }
}