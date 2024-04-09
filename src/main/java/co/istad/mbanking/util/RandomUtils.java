package co.istad.mbanking.util;

import java.util.Random;

public class RandomUtils {
  public static String generate9digit(){
      Random random = new Random();
      return String.format("%09d", random.nextInt(1000000000));

    }
}
