package com.example.webapplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WebapplicationApplicationTests {

    @Test
    public void testSum() {
        int a = 1;
        int b = 1;
        int c = a + b;

        Assertions.assertEquals(2, c);
    }
}
