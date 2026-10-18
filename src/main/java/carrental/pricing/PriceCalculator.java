package carrental.pricing;

import carrental.demand.FleetDemandProvider;
import carrental.model.Rental;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class PriceCalculator {
    private final FleetDemandProvider demandProvider;

    public PriceCalculator(FleetDemandProvider demandProvider) {
        this.demandProvider = demandProvider;
    }

    public double calculate(Rental rental, PricingType type) {
        switch (type) {
            case STANDARD:
                return rental.days() * rental.dailyRate();

            case WEEKEND: {
                double total = 0;
                for (int i = 0; i < rental.days(); i++) {
                    LocalDate day = rental.startDate().plusDays(i);
                    boolean weekend = day.getDayOfWeek() == DayOfWeek.SATURDAY
                            || day.getDayOfWeek() == DayOfWeek.SUNDAY;
                    total += weekend ? rental.dailyRate() * 1.3 : rental.dailyRate();
                }
                return total;
            }

            case LONG_TERM: {
                int days = rental.days();
                int firstTier = Math.min(days, 7);
                int secondTier = Math.max(0, Math.min(days, 30) - 7);
                int thirdTier = Math.max(0, days - 30);
                return rental.dailyRate() * (firstTier + secondTier * 0.85 + thirdTier * 0.70);
            }

            case DEMAND: {
                double occupancy = Math.max(0.0, Math.min(1.0, demandProvider.getOccupancyRate()));
                double multiplier = 0.7 + 0.6 * occupancy;
                return rental.days() * rental.dailyRate() * multiplier;
            }

            default:
                throw new IllegalArgumentException("Unknown pricing type: " + type);
        }
    }
}