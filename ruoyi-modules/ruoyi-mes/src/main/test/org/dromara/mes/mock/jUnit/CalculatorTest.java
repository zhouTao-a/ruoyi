package org.dromara.mes.mock.jUnit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CalculatorTest {

    @Test
    void testAdd() {
        Calculator c = new Calculator();
        int result = c.add(2, 3);
        assertEquals(5, result);
    }

    @Test
    void testSubtract() {
        Calculator c = new Calculator();
        int result = c.subtract(5, 3);
        assertEquals(2, result);
    }
}
