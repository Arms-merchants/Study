//NDK中Android log的使用
#include <android/log.h>
//在某个地方有实现对应的方法
extern "C"{
const char *jstringToCharPoint(JNIEvn *env,jstring jstr)
}

extern "C"
void test(){
    //实现具体打印的地方
    _android_log_print(ANDROID_LOG_ERROR,"name","value")
}


