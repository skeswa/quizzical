package org.gauntlet.quizzes.generator.defaults.impl;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {

    private final AtomicInteger number;

    public Counter(int number) {
      this.number = new AtomicInteger(number);
    }

    public Integer incr() {
      return number.incrementAndGet();
    }
}
