package com.storyart.storyservice.utils;

import java.util.Random;
import java.util.UUID;

public class MyStringUtils {
    public static String generateUniqueId(){
        UUID uuid = UUID.randomUUID();
        System.out.println("uuid: " + uuid.toString());
        return uuid.toString();
    }

    public static String randomString(int max, int min){
        String str = "abcdefghi jklmnop qrstuvwxyz";
        Random r = new Random();
        int l = r.nextInt((max - min) + 1) + min;
        String value = "";
        for(int i = 0; i <= l; i++){
            int index = r.nextInt(str.length());
            value += str.charAt(index);
        }
        return value;
    }
}
