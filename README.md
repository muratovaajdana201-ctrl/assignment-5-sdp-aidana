# Car Rental: Strategy Pattern

This is my Assignment 5 for the Strategy pattern. I chose the Car Rental topic. My program calculates the price and the insurance of a car rental. Every way of calculating is a separate class, and the program can change them while it is running.

I used Java 17, Maven and JUnit 5. To run the tests I use mvn test, and to see the demo I run carrental.Main.

## Why I chose this topic

I wanted a topic that is easy to explain. Everybody understands how a rental price works, and the price can be calculated in many ways: a normal price, a more expensive weekend, a discount for a long rental, a higher price when almost all cars are taken. The insurance also has several variants. In all these cases the task is the same ("calculate the money") and only the way of doing it is different. That is exactly where the Strategy pattern helps.

## What I made

I made two families of strategies.

The first family is pricing. The interface is PricingStrategy and the context is PricingContext. It has four strategies:

-StandardPricing multiplies the daily rate by the number of days.

-WeekendPricing goes through every day of the rental and takes 30 percent more for Saturday and Sunday.

-LongTermPricing splits the days into tiers. Days 1 to 7 cost the full price, days 8 to 30 cost 85 percent, and the days after 30 cost 70 percent. 

-DemandPricing asks how busy the fleet is and changes the price from 0.7 to 1.3 times.

The second family is insurance. The interface is InsuranceStrategy and the context is InsuranceContext. It has three strategies:

-BasicInsurance is a fixed amount for every day.

-FullCoverInsurance depends on the value of the car.

-YoungDriverInsurance depends on the age and the driving experience of the driver.

I tried to make the logic of the strategies really different, not only the numbers. One of them is a simple multiplication, one has a loop over the days, one has tiers, and one uses data from outside.

## The Selector

PricingSelector is the only class that decides which pricing strategy to use. It checks the rules from top to bottom and the first rule that fits wins:

1.If the fleet is more than 80 percent occupied, it takes DemandPricing.

2.If the rental is longer than 7 days, it takes LongTermPricing.

3.If the rental includes a Saturday or a Sunday, it takes WeekendPricing.

4.In all other cases it takes StandardPricing.

This is the only place where I use if to choose a strategy. The two contexts have no if, no switch and no instanceof. They just keep a strategy and call its method.

## External data

DemandPricing needs to know how busy the fleet is. In a real company this number would come from a database or an API. I did not want my pricing code to depend on it, so I created my own interface FleetDemandProvider with one method, getOccupancyRate().

I have two classes for it. SimulatedFleetDemandProvider returns a random number and pretends to be the real system. FakeFleetDemandProvider is in the test folder and returns a number that the test gives it. Because of this, the tests always have the same result, and I do not need any real API.

If the real source changes one day, I only write a new class for the interface. The strategies and the selector do not change.

## Tests

-I wrote a test for every strategy.

-I wrote a test for every rule of the Selector, also for the borders: exactly 80 percent and exactly 7 days.

-I tested both contexts, including the change of a strategy with setStrategy().

-I made parameterized tests in StrategyContractTest. One check ("the price is never negative") runs for every strategy of a family. When I add a new strategy, I only add it to the list.

## Demo

carrental.Main shows three things:

1.One rental calculated by every strategy of both families.

2.Manual choice (always Standard) against automatic choice by the Selector, with a calm fleet and with a busy fleet.

3.Changing the strategy while the program is running.

## Classes and responsibilities

-Rental: Holds the validated data of one car rental request.

-FleetDemandProvider: Defines how the program reads the current fleet occupancy rate.

-SimulatedFleetDemandProvider: Produces a random occupancy rate that imitates a real fleet system.

-FakeFleetDemandProvider (test): Returns a fixed occupancy rate chosen by the test.

-PricingStrategy: Defines the contract for calculating the rental price.

-StandardPricing: Multiplies the daily rate by the number of rental days.

-WeekendPricing: Adds a 30 percent surcharge to every weekend day of the rental.

-LongTermPricing: Applies cheaper daily rates in tiers as the rental grows longer.

-DemandPricing: Scales the price with the fleet occupancy rate read from the provider.

-PricingContext: Holds the current pricing strategy, then delegates the price calculation to it.

-PricingSelector: Chooses a pricing strategy using demand level, rental length, weekend days.

-InsuranceStrategy: Defines the contract for calculating the insurance cost.

-BasicInsurance: Charges a fixed amount per rental day.

-FullCoverInsurance: Charges a small share of the car value per rental day.

-YoungDriverInsurance: Raises the daily insurance cost for young drivers with short driving experience.

-InsuranceContext: Holds the current insurance strategy, then delegates the cost calculation to it. 

-Main: Runs the console demonstration of all strategies, the selector, runtime switching.

## Why this is Strategy, not State or Template Method

**It is not State.** In State an object changes its behavior when its own state changes, and the states often switch each other. For example, an order goes from "new" to "paid" to "shipped". In my program this does not happen. My pricing strategies do not know about each other and never switch each other on. Somebody from outside, the client or the Selector, chooses the strategy for the situation. A rental has no life cycle that moves from one strategy to the next.

**It is not Template Method.** Template Method has one fixed skeleton of the algorithm in a base class, and subclasses override only some steps. The choice is made at compile time through inheritance. In my code there is no common skeleton. Every strategy replaces the whole calculation with its own logic. The strategies implement an interface and do not extend a class, and the context uses them through composition. That is why I can change the algorithm while the program is running with `setStrategy()`. With inheritance I could not do this.

**It is Strategy** because it fits the definition from the lecture. I have a family of algorithms, each one is in its own class, all of them have one interface, they can be swapped at runtime, and the context does not know the concrete class. If I want a new strategy, I write a new class and do not edit the old ones. This is the Open/Closed Principle.

## Refactoring history (before and after)

I wrote the pricing part two times. First I made one class, PriceCalculator, with a big switch for all the variants, and I wrote tests for it. This is the "before" version. Then I moved every case into its own strategy class and removed the switch. This is the "after" version. The same test file PriceCalculatorTest passed on both commits, so the behavior did not change.

-Before (everything in one switch): https://github.com/muratovaajdana201-ctrl/assignment-5-sdp-aidana/commit/3e3e8b5

-After (Strategy pattern): https://github.com/muratovaajdana201-ctrl/assignment-5-sdp-aidana/commit/488a23b
After the refactoring I deleted PriceCalculator and its test, because every strategy now has its own tests. Both commits stay in the git history.

## Conclusion: what I understood in this assignment

Before this assignment I thought a design pattern is just a rule that you must remember. Now I understand that it is a solution for a real problem. My problem was a class with a big switch. Every new way of calculating the price meant opening this class and changing it, and every change could break the old code. With the Strategy pattern I put every way into its own small class. Now to add a new way I only write a new class, and the old classes stay untouched.

I understood the roles of the parts. The strategy interface is a contract, it says what every algorithm must be able to do. A concrete strategy is one algorithm. The context keeps one strategy and passes the work to it, and it does not know which strategy it has. The client or the selector chooses the strategy. When I kept these roles separate, the code became clear. At the beginning I wanted to put the choice into the context, but then I understood that this brings the `if` back, only in another place.

The refactoring part helped me a lot. When I wrote the switch version first, I could feel the problem myself, and after the refactoring the same tests were still green. This showed me that a refactoring changes the structure and not the behavior, and that tests are what proves it.

I also learned why the external data is hidden behind my own interface. When DemandPricing asked a real source directly, I could not test it, because the number was always different. With FleetDemandProvider I can give the strategy a fake source with a fixed number. So an interface helps not only to swap algorithms, it also helps to test the code. This is the idea of depending on abstractions and not on concrete classes.

The parameterized test was new for me. One test runs for every strategy, and it checks the contract that all strategies must follow. This is a good check for a family of strategies, because a new strategy gets the check after one added line.

I also understood the difference between Strategy, State and Template Method. In Strategy the choice comes from outside and the algorithms do not know each other. In State the object changes by itself when its state changes. In Template Method the skeleton is fixed and the subclasses change only the steps. The three patterns look similar, but they solve different problems.

The Strategy pattern also has a price. For two or three simple and stable algorithms it is too much, because it adds many small classes and an interface. I would use it when the ways of doing something change often, when I need to choose them at runtime, or when a method has a long chain of if and switch. In my topic it was a good choice, because the pricing rules really can change and grow.