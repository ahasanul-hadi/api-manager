package com.bnda.webapi.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Utils {

    //public static DateTimeFormatter sdf= DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static SecureRandom random = new SecureRandom();

    public static String[] ALLOWED_IMAGE_EXTENSION={"png","jpg","jpeg","gif"};

    public static String getRandomPassword(int length){
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static String validateImageFile(MultipartFile file){
        String extension;
        int index = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf('.');
        if (index > 0) {
            extension = file.getOriginalFilename().substring(index+1);
        } else {
            extension = "";
        }
        if(Arrays.stream(ALLOWED_IMAGE_EXTENSION).noneMatch(ext->ext.equals(extension.toLowerCase()))){
            return "Extension didn't match with allowed extension ["+ Arrays.toString(ALLOWED_IMAGE_EXTENSION)+"]";
        }

        if(file.getSize()*0.00000095367432>1){
            return "Image size is greater than 1 MB";
        }

        String type=file.getContentType();
        System.out.println("file type:"+type);

        return null;
    }

    public static Long generateRandomTeamID(int length){
        StringBuilder sb = new StringBuilder();
        sb.append((char)('0' + (random.nextInt(9) + 1)));
        for(int i=0; i < length-1; i++)
            sb.append((char)('0' + random.nextInt(10)));
        return Long.parseLong(sb.toString());
    }

}
