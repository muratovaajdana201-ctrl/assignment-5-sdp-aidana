package carrental.model;

import java.time.LocalDate;

public record Rental(LocalDate startDate,
                     int days,
                     double dailyRate,
                     double carValue,
                     int driverAge,
                     int drivingYears) {

    public Rental {
        if (startDate == null) throw new IllegalArgumentException("startDate must not be null");
        if (days <= 0) throw new IllegalArgumentException("days must be positive");
        if (dailyRate < 0 || carValue < 0) throw new IllegalArgumentException("money must not be negative");
        if (driverAge < 18 || drivingYears < 0) throw new IllegalArgumentException("invalid driver data");
    }
}