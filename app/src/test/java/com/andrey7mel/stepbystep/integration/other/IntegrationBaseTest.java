package com.andrey7mel.stepbystep.integration.other;

import com.andrey7mel.stepbystep.BuildConfig;
import com.andrey7mel.stepbystep.integration.other.di.IntegrationTestComponent;
import com.andrey7mel.stepbystep.other.App;
import com.andrey7mel.stepbystep.other.ShadowSnackbar;
import com.andrey7mel.stepbystep.other.TestConst;
import com.andrey7mel.stepbystep.other.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@RunWith(RobolectricTestRunner.class)
@Config(application = IntegrationTestApp.class,
        constants = BuildConfig.class,
        sdk = 23,
        shadows = {ShadowSnackbar.class})
@Ignore
public class IntegrationBaseTest {

    public IntegrationTestComponent component;
    public TestUtils testUtils;

    @Inject
    protected MockWebServer mockWebServer;

    @Before
    public void setUp() throws Exception {
        component = (IntegrationTestComponent) App.getComponent();
        testUtils = new TestUtils();
        component.inject(this);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    protected void setErrorAnswerWebServer() {
        mockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                return new MockResponse().setResponseCode(500);
            }
        });
    }

    protected void setCustomAnswer(boolean enableBranches, boolean enableContributors) {
        mockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

                if (request.getPath().equals("/users/" + TestConst.TEST_OWNER + "/repos")) {
                    return new MockResponse().setResponseCode(200)
                            .setBody(testUtils.readString("json/repos.json"));
                } else if (request.getPath().equals("/repos/" + TestConst.TEST_OWNER + "/" + TestConst.TEST_REPO + "/branches") && enableBranches) {
                    return new MockResponse().setResponseCode(200)
                            .setBody(testUtils.readString("json/branches.json"));
                } else if (request.getPath().equals("/repos/" + TestConst.TEST_OWNER + "/" + TestConst.TEST_REPO + "/contributors") && enableContributors) {
                    return new MockResponse().setResponseCode(200)
                            .setBody(testUtils.readString("json/contributors.json"));
                }
                return new MockResponse().setResponseCode(404);
            }
        });
    }
}
