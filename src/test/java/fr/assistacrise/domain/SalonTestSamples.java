package fr.assistacrise.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SalonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Salon getSalonSample1() {
        return new Salon().id(1L);
    }

    public static Salon getSalonSample2() {
        return new Salon().id(2L);
    }

    public static Salon getSalonRandomSampleGenerator() {
        return new Salon().id(longCount.incrementAndGet());
    }
}
