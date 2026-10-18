package carrental.pricing;

import carrental.demand.FleetDemandProvider;
import carrental.model.Rental;
import java.time.DayOfWeek;

public class PricingSelector {

    private static final double HIGH_DEMAND_THRESHOLD = 0.8;  // выше 80% загрузки = «много заказов»
    private static final int LONG_TERM_THRESHOLD_DAYS = 7;    // дольше 7 дней = «долгая аренда»

    private final FleetDemandProvider demandProvider;
    private final PricingStrategy standard = new StandardPricing();  // стратегии создаём один раз
    private final PricingStrategy weekend = new WeekendPricing();    // и переиспользуем
    private final PricingStrategy longTerm = new LongTermPricing();
    private final PricingStrategy demand;

    public PricingSelector(FleetDemandProvider demandProvider) {
        this.demandProvider = demandProvider;
        this.demand = new DemandPricing(demandProvider);
    }

    // Правила идут по порядку, побеждает первое подходящее
    public PricingStrategy select(Rental rental) {
        if (demandProvider.getOccupancyRate() > HIGH_DEMAND_THRESHOLD) {
            return demand;                      // правило 1: парк почти занят
        }
        if (rental.days() > LONG_TERM_THRESHOLD_DAYS) {
            return longTerm;                    // правило 2: долгая аренда
        }
        if (includesWeekend(rental)) {
            return weekend;                     // правило 3: в аренду попадают выходные
        }
        return standard;                        // правило 4: всё остальное
    }

    // Есть ли в аренде суббота или воскресенье
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