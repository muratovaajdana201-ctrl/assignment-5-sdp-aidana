package carrental.pricing;

import carrental.demand.FleetDemandProvider;
import carrental.model.Rental;
import java.util.Map;

public class PriceCalculator {

    // справочник «ярлык -> стратегия»; вместо switch
    private final Map<PricingType, PricingStrategy> strategies;

    public PriceCalculator(FleetDemandProvider demandProvider) {
        strategies = Map.of(
                PricingType.STANDARD, new StandardPricing(),
                PricingType.WEEKEND, new WeekendPricing(),
                PricingType.LONG_TERM, new LongTermPricing(),
                PricingType.DEMAND, new DemandPricing(demandProvider));
    }

    public double calculate(Rental rental, PricingType type) {
        return new PricingContext(strategies.get(type)).calculatePrice(rental);
    }
}