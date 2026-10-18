package carrental.pricing;

import carrental.model.Rental;

public class LongTermPricing implements PricingStrategy {

    private static final int FIRST_TIER_END = 7;
    private static final int SECOND_TIER_END = 30;
    private static final double SECOND_TIER_MULTIPLIER = 0.85;
    private static final double THIRD_TIER_MULTIPLIER = 0.70;

    @Override
    public double calculatePrice(Rental rental) {
        int days = rental.days();
        int firstTierDays = Math.min(days, FIRST_TIER_END);
        int secondTierDays = Math.max(0, Math.min(days, SECOND_TIER_END) - FIRST_TIER_END);
        int thirdTierDays = Math.max(0, days - SECOND_TIER_END);
        return rental.dailyRate() * (firstTierDays
                + secondTierDays * SECOND_TIER_MULTIPLIER
                + thirdTierDays * THIRD_TIER_MULTIPLIER);
    }

    @Override
    public String getName() {
        return "Long-term";
    }
}