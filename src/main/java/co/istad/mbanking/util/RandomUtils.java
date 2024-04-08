package co.istad.mbanking.util;

public class RandomUtils {
    String generate9digit(){
        String result = "";
        for (long i = 1; i <= 999999999; i++) {
            result = String.format("%09d\n",i);
        }
        return result;
    }
}
