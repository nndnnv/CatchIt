package com.catchit.lib;

import com.catchit.lib.models.AppInfo;

import org.junit.Before;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AppInfoUnitTest {

    private AppInfo.Builder builder;

    @Before
    public void setUp() {
        builder = new AppInfo.Builder();
    }

    @Test
    public void addExceptionHandler() {
        builder.setApplicationContext(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildIt() {
        builder.build();
    }

}