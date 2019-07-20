package com.train4game.munoon.service;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.train4game.munoon.utils.ValidationUtils.getRootCause;
import static org.hamcrest.CoreMatchers.instanceOf;

public abstract class AbstractServiceTest {
    private static final Logger log = LoggerFactory.getLogger("result");
    private static Map<String, Long> statistics = new HashMap<>();

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String methodName = description.getMethodName();
            long time = TimeUnit.NANOSECONDS.toMillis(nanos);
            log.info("Finished {}, it takes {} ms", methodName, time);
            statistics.put(methodName, time);
        }
    };

    @AfterClass
    public static void showStatistics() {
        log.info("Result test:");
        statistics.forEach((methodName, time) -> log.info("{}: {} ms", methodName, time));
    }

    public <T extends Throwable> void validateRootCause(Runnable runnable, Class<T> exceptionClass) {
        try {
            runnable.run();
            Assert.fail("Expected " + exceptionClass.getName());
        } catch (Exception e) {
            Assert.assertThat(getRootCause(e), instanceOf(exceptionClass));
        }
    }
}
