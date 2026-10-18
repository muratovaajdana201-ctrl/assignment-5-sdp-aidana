package carrental.pricing;

import carrental.model.Rental;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingContextTest {

    private final Rental rental = new Rental(LocalDate.of(2025, 1, 3), 3, 100, 20000, 30, 5);  // пт-вс

    @Test
    void contextDelegatesToTheStrategyFromTheConstructor() {
        PricingContext context = new PricingContext(new StandardPricing());
        assertEquals(300.0, context.calculatePrice(rental), 0.001);
    }

    @Test
    void setStrategyChangesTheBehaviourAtRuntime() {
        PricingContext context = new PricingContext(new StandardPricing());
        assertEquals(300.0, context.calculatePrice(rental), 0.001);   // сначала Standard

        context.setStrategy(new WeekendPricing());                    // меняем стратегию «на лету»
        assertEquals(360.0, context.calculatePrice(rental), 0.001);   // тот же context, другой результат
    }
}