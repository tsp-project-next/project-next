package User;

import java.util.Random;

public class Utilities {

    public static String generatePacketIdentifier() {
        int packetID = 1 + new Random().nextInt(999999998);
        String result = String.format("%09d", packetID);
        return result;
    }

}
