package carrental.demand;

public class FakeFleetDemandProvider implements FleetDemandProvider {
    private final double rate;

    public FakeFleetDemandProvider(double rate) { this.rate = rate; }

    @Override
    public double getOccupancyRate() { return rate; }
}