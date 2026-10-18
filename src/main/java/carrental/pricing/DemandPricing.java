package carrental.pricing;

import carrental.demand.FleetDemandProvider;
import carrental.model.Rental;

public class DemandPricing implements PricingStrategy {

    private final FleetDemandProvider demandProvider;

    public DemandPricing(FleetDemandProvider demandProvider) {
        this.demandProvider = demandProvider;
    }

    @Override
    public double calculatePrice(Rental rental) {
        double occupancy = Math.max(0.0, Math.min(1.0, demandProvider.getOccupancyRate()));
        double multiplier = 0.7 + 0.6 * occupancy;
        return rental.days() * rental.dailyRate() * multiplier;
    }

    @Override
    public String getName() {
        return "Demand-based";
    }
}