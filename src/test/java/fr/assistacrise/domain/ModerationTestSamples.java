package fr.assistacrise.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ModerationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Moderation getModerationSample1() {
        return new Moderation().id(1L).motif("motif1");
    }

    public static Moderation getModerationSample2() {
        return new Moderation().id(2L).motif("motif2");
    }

    public static Moderation getModerationRandomSampleGenerator() {
        return new Moderation().id(longCount.incrementAndGet()).motif(UUID.randomUUID().toString());
    }
}
