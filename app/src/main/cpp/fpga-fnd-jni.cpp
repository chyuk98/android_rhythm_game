#include <jni.h>
#include <string>
#include <android/log.h>
#include <fcntl.h>
#include <unistd.h>

#define FND_DEVICE "/dev/fpga_fnd"
#define MAX_DIGIT 4

int fpga_fnd(const char* str)
{
    int dev;
    unsigned char data[4];
    unsigned char retval;
    int i;
    int str_size;

    memset(data, 0, sizeof(data));

    str_size = (strlen(str));

    if(str_size > MAX_DIGIT){
        str_size = MAX_DIGIT;
    }

    for(i = 0; i < str_size; i++){
        if((str[i] < 0x30) || (str[i] > 0x39)){
            return 1;
        }
        data[i] = str[i] - 0x30;
    }

    dev = open(FND_DEVICE, O_RDWR);
    if(dev < 0){
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %s", str);
        return -1;
    }
    else{
        __android_log_print(ANDROID_LOG_INFO, "Device Open Success", "Driver = %d", str);
        write(dev, &data, 4);
        close(dev);
        return 0;
    }
}

// int를 return 하므로(result) 'jstring'이 아닌 'jint' JNICALL
extern "C" JNIEXPORT jint JNICALL
Java_org_techtown_rhythmgame_GameActivity_ReceiveFndValue(
        JNIEnv* env,
        jobject thiz, jstring val) {
    jint result;
    // Get을 한 후 메모리를 차지하므로
    const char *str = (*env).GetStringUTFChars(val,NULL);
    __android_log_print(ANDROID_LOG_INFO, "FpgaFNDExample", "value = %s", str);
    result = fpga_fnd(str);
    // Release로 해당 메모리를 해제해줌
    (*env).ReleaseStringUTFChars(val, str);

    return result;
}