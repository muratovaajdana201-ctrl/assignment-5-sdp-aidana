package carrental.insurance;

import carrental.model.Rental;

public class YoungDriverInsurance implements InsuranceStrategy {

    private static final double BASE_PER_DAY = 8.0;

    @Override
    public double calculateInsurance(Rental rental) {
        double perDay = BASE_PER_DAY;
        if (rental.driverAge() < 25) {
            perDay *= 1.5;
        }
        if (rental.drivingYears() < 2) {
            perDay *= 1.3;
        }
        return perDay * rental.days();
    }

    @Override
    public String getName() {
        return "Young driver";
    }
}