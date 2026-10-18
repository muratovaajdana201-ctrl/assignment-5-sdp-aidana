package carrental;

import carrental.demand.FakeFleetDemandProvider;
import carrental.insurance.BasicInsurance;
import carrental.insurance.FullCoverInsurance;
import carrental.insurance.InsuranceStrategy;
import carrental.insurance.YoungDriverInsurance;
import carrental.model.Rental;
import carrental.pricing.DemandPricing;
import carrental.pricing.LongTermPricing;
import carrental.pricing.PricingStrategy;
import carrental.pricing.StandardPricing;
import carrental.pricing.WeekendPricing;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.time.LocalDate;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StrategyContractTest {

    private final Rental rental = new Rental(LocalDate.of(2025, 1, 3), 3, 100, 20000, 22, 1);

    // Поставщик данных: все стратегии цены. Метод обязан быть static.
    static Stream<PricingStrategy> pricingStrategies() {
        return Stream.of(
                new StandardPricing(),
                new WeekendPricing(),
                new LongTermPricing(),
                new DemandPricing(new FakeFleetDemandProvider(0.5)));
    }

    // Поставщик данных: все стратегии страховки
    static Stream<InsuranceStrategy> insuranceStrategies() {
        return Stream.of(new BasicInsurance(), new FullCoverInsurance(), new YoungDriverInsurance());
    }

    @ParameterizedTest                      // вместо @Test: запустится по разу на каждую стратегию
    @MethodSource("pricingStrategies")      // имя метода-поставщика
    void priceIsNeverNegative(PricingStrategy strategy) {
        assertTrue(strategy.calculatePrice(rental) >= 0,
                strategy.getName() + " returned a negative price");
    }

    @ParameterizedTest
    @MethodSource("insuranceStrategies")
    void insuranceIsNeverNegative(InsuranceStrategy strategy) {
        assertTrue(strategy.calculateInsurance(rental) >= 0,
                strategy.getName() + " returned a negative insurance cost");
    }
}