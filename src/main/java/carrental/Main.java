package carrental;

import carrental.demand.FleetDemandProvider;
import carrental.demand.SimulatedFleetDemandProvider;
import carrental.insurance.BasicInsurance;
import carrental.insurance.FullCoverInsurance;
import carrental.insurance.InsuranceContext;
import carrental.insurance.InsuranceStrategy;
import carrental.insurance.YoungDriverInsurance;
import carrental.model.Rental;
import carrental.pricing.DemandPricing;
import carrental.pricing.LongTermPricing;
import carrental.pricing.PricingContext;
import carrental.pricing.PricingSelector;
import carrental.pricing.PricingStrategy;
import carrental.pricing.StandardPricing;
import carrental.pricing.WeekendPricing;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {

        FleetDemandProvider calmFleet = () -> 0.4;
        FleetDemandProvider busyFleet = () -> 0.95;


        Rental weekendTrip = new Rental(LocalDate.of(2025, 1, 3), 3, 100, 20000, 22, 1);

        part1AllStrategies(weekendTrip, calmFleet);
        part2ManualVersusAutomatic(calmFleet, busyFleet);
        part3SwitchAtRuntime(weekendTrip);


        double simulated = new SimulatedFleetDemandProvider().getOccupancyRate();
        System.out.println("\nSimulated source says: " + String.format(Locale.US, "%.0f%%", simulated * 100)
                + " of the fleet is rented right now");
    }


    private static void part1AllStrategies(Rental rental, FleetDemandProvider demand) {
        System.out.println("=== 1. Same rental, every strategy of each family ===");
        System.out.println(rental);


        List<PricingStrategy> pricing = List.of(
                new StandardPricing(), new WeekendPricing(), new LongTermPricing(), new DemandPricing(demand));
        for (PricingStrategy strategy : pricing) {
            print("Price     / " + strategy.getName(), strategy.calculatePrice(rental));
        }

        List<InsuranceStrategy> insurance = List.of(
                new BasicInsurance(), new FullCoverInsurance(), new YoungDriverInsurance());
        for (InsuranceStrategy strategy : insurance) {
            print("Insurance / " + strategy.getName(), strategy.calculateInsurance(rental));
        }
    }


    private static void part2ManualVersusAutomatic(FleetDemandProvider calm, FleetDemandProvider busy) {
        System.out.println("\n=== 2. Manual choice versus automatic choice by the Selector ===");

        Rental shortWeekday = new Rental(LocalDate.of(2025, 1, 6), 3, 100, 20000, 40, 10);
        Rental weekend      = new Rental(LocalDate.of(2025, 1, 3), 3, 100, 20000, 40, 10);
        Rental twoWeeks     = new Rental(LocalDate.of(2025, 1, 6), 14, 100, 20000, 40, 10);


        PricingContext manual = new PricingContext(new StandardPricing());
        System.out.println("Manual (always Standard):");
        print("  short weekday trip", manual.calculatePrice(shortWeekday));
        print("  weekend trip", manual.calculatePrice(weekend));
        print("  two weeks", manual.calculatePrice(twoWeeks));


        PricingSelector calmSelector = new PricingSelector(calm);
        System.out.println("Automatic (calm fleet, 40% rented):");
        autoPrice("  short weekday trip", calmSelector, shortWeekday);
        autoPrice("  weekend trip", calmSelector, weekend);
        autoPrice("  two weeks", calmSelector, twoWeeks);

        PricingSelector busySelector = new PricingSelector(busy);
        System.out.println("Automatic (busy fleet, 95% rented):");
        autoPrice("  short weekday trip", busySelector, shortWeekday);
        autoPrice("  two weeks", busySelector, twoWeeks);
    }


    private static void autoPrice(String label, PricingSelector selector, Rental rental) {
        PricingStrategy chosen = selector.select(rental);
        PricingContext context = new PricingContext(chosen);
        print(label + " -> " + context.getStrategyName(), context.calculatePrice(rental));
    }


    private static void part3SwitchAtRuntime(Rental rental) {
        System.out.println("\n=== 3. Switching the strategy while the program is running ===");

        InsuranceContext context = new InsuranceContext(new BasicInsurance());
        print("Insurance / " + context.getStrategyName(), context.calculateInsurance(rental));

        context.setStrategy(new FullCoverInsurance());
        print("Insurance / " + context.getStrategyName(), context.calculateInsurance(rental));

        context.setStrategy(new YoungDriverInsurance());
        print("Insurance / " + context.getStrategyName(), context.calculateInsurance(rental));
    }


    private static void print(String label, double amount) {
        System.out.println(String.format(Locale.US, "%-40s %9.2f", label, amount));
    }
}