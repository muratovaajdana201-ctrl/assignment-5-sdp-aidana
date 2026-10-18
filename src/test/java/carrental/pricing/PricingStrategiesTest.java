package carrental.pricing;

import carrental.demand.FakeFleetDemandProvider;
import carrental.model.Rental;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingStrategiesTest {

    // пятница 2025-01-03, 3 дня (пт, сб, вс), цена 100
    private final Rental friday3 = new Rental(LocalDate.of(2025, 1, 3), 3, 100, 20000, 30, 5);
    // понедельник 2025-01-06, 3 дня (пн, вт, ср): выходных нет
    private final Rental monday3 = new Rental(LocalDate.of(2025, 1, 6), 3, 100, 20000, 30, 5);
    // понедельник, 10 дней
    private final Rental monday10 = new Rental(LocalDate.of(2025, 1, 6), 10, 100, 20000, 30, 5);
    // понедельник, 40 дней
    private final Rental monday40 = new Rental(LocalDate.of(2025, 1, 6), 40, 100, 20000, 30, 5);

    // ---------- StandardPricing ----------
    @Test
    void standardMultipliesDailyRateByDays() {
        assertEquals(300.0, new StandardPricing().calculatePrice(friday3), 0.001);    // 3 × 100
    }

    // ---------- WeekendPricing ----------
    @Test
    void weekendAddsThirtyPercentOnSaturdayAndSunday() {
        assertEquals(360.0, new WeekendPricing().calculatePrice(friday3), 0.001);     // 100 + 130 + 130
    }

    @Test
    void weekendEqualsStandardWhenRentalHasNoWeekendDay() {
        assertEquals(300.0, new WeekendPricing().calculatePrice(monday3), 0.001);     // выходных нет
    }

    // ---------- LongTermPricing ----------
    @Test
    void longTermShortRentalHasNoDiscount() {
        assertEquals(300.0, new LongTermPricing().calculatePrice(monday3), 0.001);    // все дни в 1-й ступени
    }

    @Test
    void longTermGivesCheaperDaysAfterTheFirstWeek() {
        assertEquals(955.0, new LongTermPricing().calculatePrice(monday10), 0.001);   // 7×100 + 3×85
    }

    @Test
    void longTermUsesThirdTierAfterThirtyDays() {
        assertEquals(3355.0, new LongTermPricing().calculatePrice(monday40), 0.001);  // 700 + 23×85 + 10×70
    }

    // ---------- DemandPricing (с подделкой вместо настоящего источника) ----------
    @Test
    void demandAtHalfOccupancyKeepsTheBasePrice() {
        DemandPricing pricing = new DemandPricing(new FakeFleetDemandProvider(0.5));
        assertEquals(300.0, pricing.calculatePrice(friday3), 0.001);                  // множитель 1.0
    }

    @Test
    void demandAtFullOccupancyCostsThirtyPercentMore() {
        DemandPricing pricing = new DemandPricing(new FakeFleetDemandProvider(1.0));
        assertEquals(390.0, pricing.calculatePrice(friday3), 0.001);                  // множитель 1.3
    }

    @Test
    void demandAtEmptyFleetCostsThirtyPercentLess() {
        DemandPricing pricing = new DemandPricing(new FakeFleetDemandProvider(0.0));
        assertEquals(210.0, pricing.calculatePrice(friday3), 0.001);                  // множитель 0.7
    }

    @Test
    void demandIgnoresNonsenseValuesFromTheDataSource() {
        // источник вернул 5.0 (нереально): стратегия обрезает значение до 1.0
        DemandPricing pricing = new DemandPricing(new FakeFleetDemandProvider(5.0));
        assertEquals(390.0, pricing.calculatePrice(friday3), 0.001);
    }
}