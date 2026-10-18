package carrental.insurance;

import carrental.model.Rental;

public interface InsuranceStrategy {
    double calculateInsurance(Rental rental);   // посчитать стоимость страховки
    String getName();                           // название (для демо)
}