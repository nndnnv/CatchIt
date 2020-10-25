package com.catchit.lib;

import com.catchit.lib.models.CatchItException;

import org.junit.Before;
import org.junit.Test;

public class CatchItExceptionUnitTest {

    private CatchItException.Builder builder;

    @Before
    public void setUp() {
        builder = new CatchItException.Builder();
    }

    @Test
    public void addThrowable() {
        builder.setThrowable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildIt() {
        builder.build();
    }

}
