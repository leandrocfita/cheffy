package br.com.fiap.cheffy.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTest {

    @Test
    void constructorRoundsToTwoDecimalPlaces() {
        Money money = new Money(new BigDecimal("10.126"));

        assertThat(money.value()).isEqualByComparingTo("10.13");
    }

    @Test
    void constructorThrowsWhenNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("-1.00")));
    }

    @Test
    void addSubtractMultiplyAndComparisonWorkAsExpected() {
        Money value = new Money(new BigDecimal("10.00"));
        Money other = new Money(new BigDecimal("2.50"));

        assertThat(value.add(other).value()).isEqualByComparingTo("12.50");
        assertThat(value.subtract(other).value()).isEqualByComparingTo("7.50");
        assertThat(other.multiply(3).value()).isEqualByComparingTo("7.50");
        assertThat(value.isGreaterThan(other)).isTrue();
        assertThat(new Money(BigDecimal.ZERO).isZero()).isTrue();
    }

    @Test
    void subtractThrowsWhenResultWouldBeNegative() {
        Money smaller = new Money(new BigDecimal("1.00"));
        Money greater = new Money(new BigDecimal("2.00"));

        assertThrows(IllegalArgumentException.class, () -> smaller.subtract(greater));
    }
}
