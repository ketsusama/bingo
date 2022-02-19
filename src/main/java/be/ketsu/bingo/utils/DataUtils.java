package be.ketsu.bingo.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class DataUtils {
    private static final char[] HEX_ARRAY = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public DataUtils() {
    }

    public static String toHexadecimalString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; ++j) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 15];
        }

        return new String(hexChars);
    }

    public static byte[] toByteArray(List<Integer> intList) {
        byte[] array = new byte[intList.size()];

        for (int i = 0; i < intList.size(); ++i) {
            array[i] = ((Integer) intList.get(i)).byteValue();
        }

        return array;
    }

    public static byte[] toByteArray(int[] intArray) {
        byte[] array = new byte[intArray.length];

        for (int i = 0; i < intArray.length; ++i) {
            array[i] = (byte) intArray[i];
        }

        return array;
    }

    public static int[] toIntArray(byte[] byteArray) {
        int[] array = new int[byteArray.length];

        for (int i = 0; i < byteArray.length; ++i) {
            array[i] = byteArray[i];
        }

        return array;
    }

    public static void writeIntArray(DataOutputStream dos, int[] array) throws IOException {
        dos.writeInt(array.length);
        int[] var2 = array;
        int var3 = array.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            int i = var2[var4];
            dos.writeInt(i);
        }

    }

    public static int[] readIntArray(DataInputStream din) throws IOException {
        int size = din.readInt();
        int[] array = new int[size];

        for (int i = 0; i < size; ++i) {
            array[i] = din.readInt();
        }

        return array;
    }

    public static void writeByteArray(DataOutputStream dos, byte[] array) throws IOException {
        dos.writeInt(array.length);
        dos.write(array);
    }

    public static byte[] readByteArray(DataInputStream din) throws IOException {
        int size = din.readInt();
        byte[] array = new byte[size];
        din.readFully(array);
        return array;
    }

    public static void write2DByteArray(DataOutputStream dos, byte[][] array2d) throws IOException {
        dos.writeInt(array2d.length);
        byte[][] var2 = array2d;
        int var3 = array2d.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            byte[] array = var2[var4];
            writeByteArray(dos, array);
        }

    }

    public static byte[][] read2DByteArray(DataInputStream din) throws IOException {
        int size2d = din.readInt();
        byte[][] array2d = new byte[size2d][];

        for (int i = 0; i < size2d; ++i) {
            array2d[i] = readByteArray(din);
        }

        return array2d;
    }

    public static byte[] createSha1Hash(InputStream in) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        byte[] buffer = new byte[4096];

        int len;
        while ((len = in.read(buffer)) != -1) {
            md.update(buffer, 0, len);
        }

        in.close();
        return md.digest();
    }

    public static String sha1HashFromUrl(String url) throws IOException, NoSuchAlgorithmException {
        return toHexadecimalString(createSha1Hash(new URL(url).openStream()));
    }
}

