package carrental.pricing;

import carrental.demand.FakeFleetDemandProvider;
import carrental.model.Rental;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceCalculatorTest {

    private final Rental friday3 = new Rental(LocalDate.of(2025, 1, 3), 3, 100, 20000, 30, 5);
    private final Rental monday10 = new Rental(LocalDate.of(2025, 1, 6), 10, 100, 20000, 30, 5);

    private PriceCalculator calc(double occupancy) {
        return new PriceCalculator(new FakeFleetDemandProvider(occupancy));
    }

    @Test
    void standardMultipliesDailyRateByDays() {
        assertEquals(300.0, calc(0.5).calculate(friday3, PricingType.STANDARD), 0.001);
    }

    @Test
    void weekendAddsThirtyPercentOnSaturdayAndSunday() {
        assertEquals(360.0, calc(0.5).calculate(friday3, PricingType.WEEKEND), 0.001);
    }

    @Test
    void longTermGivesCheaperDaysAfterTheFirstWeek() {
        assertEquals(955.0, calc(0.5).calculate(monday10, PricingType.LONG_TERM), 0.001);
    }

    @Test
    void demandAtHalfOccupancyKeepsTheBasePrice() {
        assertEquals(300.0, calc(0.5).calculate(friday3, PricingType.DEMAND), 0.001);
    }
}