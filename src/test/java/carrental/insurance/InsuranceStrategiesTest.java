package carrental.insurance;

import carrental.model.Rental;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InsuranceStrategiesTest {

    // 3 дня, машина 20000, водителю 22 года, стаж 1 год
    private final Rental youngDriver = new Rental(LocalDate.of(2025, 1, 3), 3, 100, 20000, 22, 1);
    // тот же заказ, но водителю 40 лет, стаж 10 лет
    private final Rental experienced = new Rental(LocalDate.of(2025, 1, 3), 3, 100, 20000, 40, 10);

    @Test
    void basicChargesFixedAmountPerDay() {
        assertEquals(15.0, new BasicInsurance().calculateInsurance(youngDriver), 0.001);      // 3 × 5
    }

    @Test
    void fullCoverDependsOnCarValue() {
        assertEquals(30.0, new FullCoverInsurance().calculateInsurance(youngDriver), 0.001);  // 20000 × 0.0005 × 3
    }

    @Test
    void youngDriverWithLittleExperiencePaysSurcharges() {
        assertEquals(46.8, new YoungDriverInsurance().calculateInsurance(youngDriver), 0.001); // 8 × 1.5 × 1.3 × 3
    }

    @Test
    void youngDriverExperiencedAdultPaysBaseRate() {
        assertEquals(24.0, new YoungDriverInsurance().calculateInsurance(experienced), 0.001); // 8 × 3
    }
}