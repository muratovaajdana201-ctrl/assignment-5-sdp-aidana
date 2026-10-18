package carrental.insurance;

import carrental.model.Rental;

public interface InsuranceStrategy {
    double calculateInsurance(Rental rental);
    String getName();
}