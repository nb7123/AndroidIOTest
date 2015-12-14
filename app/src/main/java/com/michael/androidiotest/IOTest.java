package com.michael.androidiotest;

/**
 * Created by michael on 15/12/14.
 */
public class IOTest
{
    static {
        System.loadLibrary("IOTest");
    }
    public static native int writeTest(String path, int count);

    public static native int readTest(String path, int count);
}
