package carrental.pricing;

import carrental.demand.FleetDemandProvider;
import carrental.model.Rental;
import java.time.DayOfWeek;

public class PricingSelector {

    private static final double HIGH_DEMAND_THRESHOLD = 0.8;
    private static final int LONG_TERM_THRESHOLD_DAYS = 7;

    private final FleetDemandProvider demandProvider;
    private final PricingStrategy standard = new StandardPricing();
    private final PricingStrategy weekend = new WeekendPricing();
    private final PricingStrategy longTerm = new LongTermPricing();
    private final PricingStrategy demand;

    public PricingSelector(FleetDemandProvider demandProvider) {
        this.demandProvider = demandProvider;
        this.demand = new DemandPricing(demandProvider);
    }


    public PricingStrategy select(Rental rental) {
        if (demandProvider.getOccupancyRate() > HIGH_DEMAND_THRESHOLD) {
            return demand;
        }
        if (rental.days() > LONG_TERM_THRESHOLD_DAYS) {
            return longTerm;
        }
        if (includesWeekend(rental)) {
            return weekend;
        }
        return standard;
    }


    private boolean includesWeekend(Rental rental) {
        for (int i = 0; i < rental.days(); i++) {
            DayOfWeek day = rental.startDate().plusDays(i).getDayOfWeek();
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                return true;
            }
        }
        return false;
    }
}