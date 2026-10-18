package carrental.pricing;

import carrental.model.Rental;

public class StandardPricing implements PricingStrategy {

    @Override
    public double calculatePrice(Rental rental) {
        return rental.days() * rental.dailyRate();
    }

    @Override
    public String getName() {
        return "Standard";
    }
}