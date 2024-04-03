package co.istad.mbanking.util;

import org.springframework.web.multipart.MultipartFile;

public class MediaUtil {
    public static String extractExtension(String mediaName){

        int lastDotindex =mediaName.lastIndexOf(".");
        return mediaName.substring(lastDotindex + 1);  // if lastDotIndex + 1 doesn't have .
    }
}
