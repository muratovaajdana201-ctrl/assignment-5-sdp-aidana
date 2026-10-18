package carrental.pricing;

import carrental.model.Rental;

public interface PricingStrategy {
    double calculatePrice(Rental rental);
    String getName();
}