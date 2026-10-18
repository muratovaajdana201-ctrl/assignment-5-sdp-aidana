package carrental.pricing;

import carrental.model.Rental;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeekendPricing implements PricingStrategy {

    private static final double WEEKEND_MULTIPLIER = 1.3;   // выходной дороже на 30%

    @Override
    public double calculatePrice(Rental rental) {
        double total = 0;
        for (int i = 0; i < rental.days(); i++) {
            LocalDate day = rental.startDate().plusDays(i);
            total += isWeekend(day) ? rental.dailyRate() * WEEKEND_MULTIPLIER : rental.dailyRate();
        }
        return total;
    }

    private boolean isWeekend(LocalDate day) {
        DayOfWeek dayOfWeek = day.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    @Override
    public String getName() {
        return "Weekend";
    }
}