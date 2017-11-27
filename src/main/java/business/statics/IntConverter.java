package business.statics;

import java.nio.ByteBuffer;

public class IntConverter {
    private IntConverter() {}

    public static byte[] intToByteArray(int integer) {
        return ByteBuffer.allocate(4).putInt(integer).array();
    }

    public static int byteArrayToInt(byte[] array) {
        return ByteBuffer.wrap(array).getInt();
    }
}