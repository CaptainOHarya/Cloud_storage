package utils;

import java.util.UUID;

public class UniqueIdentifier {
    public static String createUniqueID() {
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        System.out.println("uuid = " + uuid);
        return uuid;
    }


}
