package com.catchit.lib;

import com.catchit.lib.sync.notifier.SyncNotifier;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class SyncNotifierUnitTest {

    private SyncNotifier.Builder builder;

    @Before
    public void setUp() throws Exception {
        builder = new SyncNotifier.Builder();
    }

    @Test
    public void addEmptyListener() {
        builder.setListener(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWrongInterval() {
        builder.setInterval(-2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWrongTimeUnit() {
        builder.setIntervalTimeUnit(TimeUnit.DAYS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildIt() throws Exception {
        builder.build();
    }
}
