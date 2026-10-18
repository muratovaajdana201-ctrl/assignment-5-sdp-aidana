package carrental.pricing;

import carrental.demand.FakeFleetDemandProvider;
import carrental.model.Rental;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class PricingSelectorTest {

    // понедельник 2025-01-06, 3 дня (пн, вт, ср): ни выходных, ни долгой аренды
    private final Rental shortWeekday = new Rental(LocalDate.of(2025, 1, 6), 3, 100, 20000, 30, 5);
    // пятница 2025-01-03, 3 дня (пт, сб, вс): есть выходные
    private final Rental withWeekend = new Rental(LocalDate.of(2025, 1, 3), 3, 100, 20000, 30, 5);
    // понедельник, 14 дней
    private final Rental twoWeeks = new Rental(LocalDate.of(2025, 1, 6), 14, 100, 20000, 30, 5);
    // понедельник, ровно 7 дней (пн...вс): включает выходные, но это ещё не «долгая» аренда
    private final Rental sevenDays = new Rental(LocalDate.of(2025, 1, 6), 7, 100, 20000, 30, 5);

    // Selector с подделкой, которая возвращает нужную загрузку
    private PricingSelector selectorWithOccupancy(double occupancy) {
        return new PricingSelector(new FakeFleetDemandProvider(occupancy));
    }

    @Test
    void rule1_highOccupancySelectsDemandPricing() {
        assertInstanceOf(DemandPricing.class, selectorWithOccupancy(0.95).select(shortWeekday));
    }

    @Test
    void rule1_highOccupancyBeatsLongRentalAndWeekend() {
        // правило 1 сильнее остальных: даже при долгой аренде выбирается Demand
        assertInstanceOf(DemandPricing.class, selectorWithOccupancy(0.95).select(twoWeeks));
    }

    @Test
    void rule1_exactlyEightyPercentIsNotHighDemand() {
        // граница: ровно 0.8 это НЕ «выше 0.8», поэтому Demand не выбирается
        assertInstanceOf(StandardPricing.class, selectorWithOccupancy(0.8).select(shortWeekday));
    }

    @Test
    void rule2_longRentalSelectsLongTermPricing() {
        assertInstanceOf(LongTermPricing.class, selectorWithOccupancy(0.4).select(twoWeeks));
    }

    @Test
    void rule2_exactlySevenDaysIsNotLongTerm() {
        // 7 дней это не «больше 7», значит правило 2 не сработает; сработает правило 3 (в аренде есть выходные)
        assertInstanceOf(WeekendPricing.class, selectorWithOccupancy(0.4).select(sevenDays));
    }

    @Test
    void rule3_rentalWithWeekendSelectsWeekendPricing() {
        assertInstanceOf(WeekendPricing.class, selectorWithOccupancy(0.4).select(withWeekend));
    }

    @Test
    void rule4_shortWeekdayRentalSelectsStandardPricing() {
        assertInstanceOf(StandardPricing.class, selectorWithOccupancy(0.4).select(shortWeekday));
    }
}