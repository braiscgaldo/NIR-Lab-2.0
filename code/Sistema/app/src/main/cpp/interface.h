//
// Created by brais on 26/03/19.
//

#ifndef SISTEMA_INTERFACE_H
#define SISTEMA_INTERFACE_H

#endif //SISTEMA_INTERFACE_H


#include <string.h>
#include <jni.h>

jstring
Java_com_kstechnologies_NanoScan_ScanListActivity_getJniString(JNIEnv* env, jobject thiz);

jobject Java_com_cgaldo_brais_sistema_Controller_JNICalls_JNICall_dlpSpecScanReadConfiguration(JNIEnv* env, jobject thiz,jbyteArray data);

jobject Java_com_cgaldo_brais_sistema_Controller_JNICalls_JNICall_dlpSpecScanInterpReference(JNIEnv* env, jobject thiz,jbyteArray data, jbyteArray coeff, jbyteArray matrix);