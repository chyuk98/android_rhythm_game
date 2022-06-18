#include <jni.h>
#include <string>
#include <android/log.h>
#include <fcntl.h>
#include <unistd.h>

#include "fpga_dot_font.h"

#define DOT_DEVICE "/dev/fpga_dot"

int fpga_dot(int x){
    int i;
    int dev;
    size_t str_size;

    str_size = sizeof(fpga_number[x]);

    dev = open(DOT_DEVICE, O_RDWR);

    if(dev < 0){
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", x);
    }
    else{
        __android_log_print(ANDROID_LOG_INFO, "Device Open Success", "Driver = %d", x);

        switch(x){
            case 0:
                write(dev, fpga_number[0], str_size);
                break;
            case 1:
                write(dev, fpga_number[1], str_size);
                break;
            case 2:
                write(dev, fpga_number[2], str_size);
                break;
            case 3:
                write(dev, fpga_number[3], str_size);
                break;
            case 4:
                write(dev, fpga_number[4], str_size);
                break;
            case 5:
                write(dev, fpga_number[5], str_size);
                break;
            case 6:
                write(dev, fpga_number[6], str_size);
                break;
            case 7:
                write(dev, fpga_number[7], str_size);
                break;
            case 8:
                write(dev, fpga_number[8], str_size);
                break;
            case 9:
                write(dev, fpga_number[9], str_size);
                break;
            case 10:
                write(dev, fpga_set_full, sizeof(fpga_set_full));
                break;
            case 20:
                write(dev, fpga_set_blank, sizeof(fpga_set_blank));
                break;
        }
        close(dev);
    }
    return 0;
}

extern "C" JNIEXPORT jstring JNICALL Java_org_techtown_rhythmgame_GameActivity_ReceiveDotValue(
        JNIEnv* env,
        jobject thiz, jint val) {

    __android_log_print(ANDROID_LOG_INFO, "FpgaDotExample", "dot value = %d", val);
    fpga_dot(val);

    return 0;
}

