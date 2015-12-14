//
// Created by Xiaodong  Liu on 15/12/14.
//

#include "com_michael_androidiotest_IOTest.h"
#include <stdio.h>
#include <android/log.h>
#include <time.h>
#include <stdlib.h>

char *jstring2string(JNIEnv *env, jstring jstr);

/*
 * Class:     com_michael_androidiotest_IOTest
 * Method:    writeTest
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_michael_androidiotest_IOTest_writeTest
        (JNIEnv *env, jclass thiz, jstring path, jint count)
{
        char *c_path = jstring2string(env, path);

        __android_log_print(ANDROID_LOG_DEBUG, "JNI_IOTest", c_path);

        FILE *file = fopen(c_path, "w");

        char str[1024];
        memset(str, 0, sizeof(str));

        if (NULL == file)
        {
                __android_log_print(ANDROID_LOG_WARN, "JNI_IOTest", "Open file error");
                return -1;
        }

        clock_t start = clock();

        for (int i = 0; i < count; ++i) {
                size_t size = fwrite(&i, sizeof(char), 1, file);
                if (size != 1)
                {
                        __android_log_print(ANDROID_LOG_WARN, "JNI_IOTest", "Write file error");
                        return -1;
                }
        }
        fclose(file);

        clock_t spend = clock() - start;
        sprintf(str, "Native write %d spend %f seconds", count, (float)spend / CLOCKS_PER_SEC);

        __android_log_print(ANDROID_LOG_DEBUG, "JNI_IOTest", str);

        return (int)spend;
}

/*
 * Class:     com_michael_androidiotest_IOTest
 * Method:    readTest
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_michael_androidiotest_IOTest_readTest
        (JNIEnv *env, jclass thiz, jstring path, jint count)
{
        char *c_path = jstring2string(env, path);

        __android_log_print(ANDROID_LOG_DEBUG, "JNI_IOTest", c_path);

        FILE *file = fopen(c_path, "r");

        char str[1024];
        memset(str, 0, sizeof(str));

        if (NULL == file)
        {
                __android_log_print(ANDROID_LOG_WARN, "JNI_IOTest", "Open file error");
                return -1;
        }

        clock_t start = clock();

        char c;

        for (int i = 0; i < count; ++i) {
                size_t size = fread(&c, sizeof(char), 1, file);
                if (size != 1)
                {
                        __android_log_print(ANDROID_LOG_WARN, "JNI_IOTest", "Write file error");
                        return -1;
                }
        }

        fclose(file);
        clock_t spend = clock() - start;
        sprintf(str, "Native read %d spend %f seconds", count, (float)spend / CLOCKS_PER_SEC);

        __android_log_print(ANDROID_LOG_DEBUG, "JNI_IOTest", str);

        return (int)spend;
}

char *jstring2string(JNIEnv *env, jstring jstr)
{
        char* rtn = NULL;
        jclass clsstring = env->FindClass("java/lang/String");
        jstring strencode = env->NewStringUTF("utf-8");
        jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
        jbyteArray barr= (jbyteArray)env->CallObjectMethod(jstr, mid, strencode);
        jsize alen = env->GetArrayLength(barr);
        jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
        if (alen > 0)
        {
                rtn = (char*)malloc(alen + 1);

                memcpy(rtn, ba, alen);
                rtn[alen] = 0;
        }
        env->ReleaseByteArrayElements(barr, ba, 0);
        return rtn;
}