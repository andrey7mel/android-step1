package com.andrey7mel.stepbystep.other;

import com.andrey7mel.stepbystep.BuildConfig;
import com.andrey7mel.stepbystep.other.di.TestComponent;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class,
        constants = BuildConfig.class,
        sdk = 23)
@Ignore
public class BaseTest {

    public TestComponent component;
    public TestUtils testUtils;

    @Before
    public void setUp() throws Exception {
        component = (TestComponent) App.getComponent();
        testUtils = new TestUtils();
    }
}
