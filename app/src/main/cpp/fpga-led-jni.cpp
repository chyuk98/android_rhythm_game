#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <fcntl.h>
#include <unistd.h>  // write(), close()

#define LED_DEVICE "/dev/fpga_led"

int fpga_led(int x)
{
    int dev;
    unsigned char data;
    unsigned char retval;

    //                      8     7     6     5     4     3     2     1     0
    unsigned char val[] = {0xff, 0x7f, 0x3f, 0x1f, 0x0f, 0x07, 0x03, 0x01, 0x00};

    dev = open(LED_DEVICE, O_RDWR);
    if (dev < 0) {
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", x);
    }
    else {
        __android_log_print(ANDROID_LOG_INFO, "Device OpenSuccess", "Driver = %d", x);
        write(dev, &val[x], sizeof(unsigned char));
        close(dev);
    }
    return 0;
}

extern "C" JNIEXPORT jstring JNICALL
Java_org_techtown_rhythmgame_GameActivity_ReceiveLedValue(
        JNIEnv *env, jobject thiz, jint val) {
    __android_log_print(ANDROID_LOG_INFO, "FpgaLedJniExample", "led value = %d", val);
    fpga_led(val);

    return NULL;
}