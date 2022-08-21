package utils.others;

import java.util.Base64;

public class Decode {
    public static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }
}
