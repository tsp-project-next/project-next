package User;

import java.util.Random;

public class Utilities {

    public static String codeGenerator() {
        // Creates an alphanumeric string of length 4
        String alphaNumeric = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; //SALTCHARS
        StringBuilder codeBuilder = new StringBuilder(); //salt
        Random rand = new Random();
        while (codeBuilder.length() < 4) {
            int index = (int) (rand.nextFloat() * alphaNumeric.length());
            codeBuilder.append(alphaNumeric.charAt(index));
        }
        String code = codeBuilder.toString();
        makeHostCode(code);
        return code;
    }

    public static String makeHostCode(String code) {
        return "Host-" + code;
    }

    public static String generatePacketIdentifier() {
        int packetID = 1 + new Random().nextInt(999999998);
        String result = String.format("%09d", packetID);
        return result;
    }

}
