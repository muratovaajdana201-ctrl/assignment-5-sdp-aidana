package carrental.insurance;

import carrental.model.Rental;

public class BasicInsurance implements InsuranceStrategy {

    private static final double FIXED_PER_DAY = 5.0;

    @Override
    public double calculateInsurance(Rental rental) {
        return rental.days() * FIXED_PER_DAY;
    }

    @Override
    public String getName() {
        return "Basic";
    }
}