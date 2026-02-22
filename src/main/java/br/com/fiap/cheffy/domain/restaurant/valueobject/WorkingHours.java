package br.com.fiap.cheffy.domain.restaurant.valueobject;

import br.com.fiap.cheffy.domain.user.exception.UserOperationNotAllowedException;

import java.time.Duration;
import java.time.OffsetTime;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.*;

public class WorkingHours {

    private final OffsetTime openingTime;
    private final OffsetTime closingTime;
    private final boolean open24Hours;

    private WorkingHours(
            OffsetTime openingTime,
            OffsetTime closingTime,
            boolean open24Hours,
            boolean skippValidation
    ) {

        if(!skippValidation) {
            validateWorkingTime(openingTime, closingTime, open24Hours);
        }

        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.open24Hours = open24Hours;
    }

    private void validateWorkingTime(OffsetTime openingTime, OffsetTime closingTime, boolean open24Hours) {
        if (open24Hours) {
            if (openingTime != null || closingTime != null) {
                throw new UserOperationNotAllowedException(
                        AROUND_THE_CLOCK_RESTAURANTS_SHOULDNT_DECLARE_WORKING_HOURS
                );
            }
        } else {
            if (openingTime == null || closingTime == null) {
                throw new UserOperationNotAllowedException(
                        RESTAURANT_INVALID_WORKING_TIME
                );
            }

            if (openingTime.equals(closingTime)) {
                throw new UserOperationNotAllowedException(
                        RESTAURANT_INVALID_WORKING_TIME
                );
            }

            Duration duration = calculateDuration(openingTime, closingTime);

            if (duration.toHours() < 1) {
                throw new UserOperationNotAllowedException(WORKING_TIME_TOO_SHORT);
            }
        }
    }

    public static WorkingHours reconstitute(
            OffsetTime opening,
            OffsetTime closing,
            boolean open24hours
    ) {
        return new WorkingHours(opening, closing, open24hours, true);
    }

    public static WorkingHours open24Hours() {
        return new WorkingHours(null, null, true, false);
    }

    public static WorkingHours of(OffsetTime opening, OffsetTime closing) {
        return new WorkingHours(opening, closing, false, false);
    }

    private Duration calculateDuration(OffsetTime opening, OffsetTime closing) {
        if (closing.isAfter(opening)) {
            return Duration.between(opening, closing);
        }
        return Duration.between(opening, closing.plusHours(24));
    }

    public OffsetTime getOpeningTime() {
        return openingTime;
    }

    public OffsetTime getClosingTime() {
        return closingTime;
    }

    public boolean isOpen24Hours() {
        return open24Hours;
    }
}
