package carrental.insurance;

import carrental.model.Rental;

public class FullCoverInsurance implements InsuranceStrategy {

    private static final double SHARE_PER_DAY = 0.0005;

    @Override
    public double calculateInsurance(Rental rental) {
        return rental.carValue() * SHARE_PER_DAY * rental.days();
    }

    @Override
    public String getName() {
        return "Full cover";
    }
}