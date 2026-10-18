package carrental.insurance;

import carrental.model.Rental;

public class InsuranceContext {

    private InsuranceStrategy strategy;

    public InsuranceContext(InsuranceStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(InsuranceStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculateInsurance(Rental rental) {
        return strategy.calculateInsurance(rental);
    }

    public String getStrategyName() {
        return strategy.getName();
    }
}