#include <jni.h>
#include <string>
#include <android/log.h>
#include <fcntl.h>
#include <unistd.h>
#include <math.h>

#define LINE_BUFF 16
#define MAX_BUFF 32

#define FPGA_TEXT_LCD_DEVICE "/dev/fpga_text_lcd"


int fpga_text_lcd(const char* str1, const char* str2)
{
    int i = 0;
    int dev;
    size_t str_size1;
    size_t str_size2;

    unsigned char LCDData[40];                 // LCD에 출력할 문자열
    memset(LCDData, 0, sizeof(LCDData));    // 출력 문자열 초기화

    str_size1 = strlen(str1);       // 입력 인수로 들어온 Line1 문자열 크기
    str_size2 = strlen(str2);       // 입력 인수로 들어온 Line2 문자열 크기

    dev = open(FPGA_TEXT_LCD_DEVICE, O_RDWR);

    if (dev < 0) {
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", dev);
        return -1;
    }
    // 디바이스가 정상적으로 연결되었다면 아래 프로세스 실행
    else {
        memset(LCDData, 0, sizeof(LCDData));    // 출력 문자열 초기화

        // 각 문자열이 LINE_BUFF 를 넘지 않도록 길이 제한
        if (str_size1 > 16) str_size1 = 16;
        if (str_size2 > 16) str_size2 = 16;

        // Line 1
        strncat(reinterpret_cast<char *>(LCDData), str1, str_size1);
        memset(LCDData + str_size1, ' ', LINE_BUFF - str_size1);

        // Line 2
        strncat(reinterpret_cast<char *>(LCDData), str2, str_size2);
        memset(LCDData + LINE_BUFF + str_size2, ' ', LINE_BUFF - str_size2);

        // LCD 출력
        write(dev, LCDData, MAX_BUFF-1);

        close(dev);
    }

    return 0;
}


extern "C" JNIEXPORT jint JNICALL Java_org_techtown_rhythmgame_GameActivity_ReceiveTextLcdValue(
        JNIEnv* env, jobject thiz, jstring val1, jstring val2) {

    jint result;

    const char* pstr1 = (*env).GetStringUTFChars(val1, NULL);
    __android_log_print(ANDROID_LOG_INFO, "FpgaFndExample", "value = %s", pstr1);

    const char* pstr2 = (*env).GetStringUTFChars(val2, NULL);
    __android_log_print(ANDROID_LOG_INFO, "FpgaFndExample", "value = %s", pstr2);

    fpga_text_lcd(pstr1, pstr2);

    (*env).ReleaseStringUTFChars(val1, pstr1);
    (*env).ReleaseStringUTFChars(val2, pstr2);

    return result;
}