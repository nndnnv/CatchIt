package com.catchit.lib;

import com.catchit.lib.sync.worker.SyncExceptionsWorker;

import org.junit.Before;
import org.junit.Test;

public class SyncExceptionsWorkerUnitTest {

    private SyncExceptionsWorker.Builder builder;

    @Before
    public void setUp() {
        builder = new SyncExceptionsWorker.Builder();
    }

    @Test
    public void addRepo() {
        builder.setRepo(null);
    }

    @Test
    public void addAppInfo() {
        builder.setAppInfo(null);
    }

    @Test
    public void addDeviceInfo() {
        builder.setDeviceInfo(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildIt() {
        builder.build();
    }

}
