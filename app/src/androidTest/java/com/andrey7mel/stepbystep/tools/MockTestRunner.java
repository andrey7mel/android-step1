package com.andrey7mel.stepbystep.tools;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

import com.andrey7mel.stepbystep.di.TestApp;

public class MockTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        return super.newApplication(cl, TestApp.class.getName(), context);
    }
}