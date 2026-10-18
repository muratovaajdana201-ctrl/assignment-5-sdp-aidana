package carrental.pricing;

import carrental.model.Rental;

public class PricingContext {

    private PricingStrategy strategy;

    public PricingContext(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculatePrice(Rental rental) {
        return strategy.calculatePrice(rental);
    }

    public String getStrategyName() {
        return strategy.getName();
    }
}