package com.catchit.lib;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CatchItOptionsBuilderUnitTest {

    private CatchItOptions.Builder builder;

    @Before
    public void setUp() throws Exception {
        builder = new CatchItOptions.Builder();
    }

    @Test
    public void setServerAddress() {
        builder.setServerAddress(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setIntervalTimeUnit() {
        builder.setSyncInterval(-2, TimeUnit.DAYS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setIntervalTimeUnit1() {
        builder.setSyncInterval(2, TimeUnit.SECONDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildIt() throws Exception {
        builder.build();
    }

}
