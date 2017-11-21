package Business.staticClasses;

import java.nio.ByteBuffer;

public class IntConverter {
    public static byte[] intToByteArray(int integer) {
//        return new byte[]{
//                (byte) ((integer >> 24) & 0xFF),
//                (byte) ((integer >> 16) & 0xFF),
//                (byte) ((integer >> 8) & 0xFF),
//                (byte) (integer & 0xFF)
//        };

        return ByteBuffer.allocate(4).putInt(integer).array();
    }

    public static int byteArrayToInt(byte[] array) {
//        return array[3] & 0xFF |
//                (array[2] & 0xFF) << 8 |
//                (array[1] & 0xFF) << 16 |
//                (array[0] & 0xFF) << 24;
//    }

        return ByteBuffer.wrap(array).getInt();
    }
}