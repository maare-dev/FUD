import java.io.RandomAccessFile;

public class Files {
    public static RandomAccessFile setRAF(String path, String mode) {
        try {
            return new RandomAccessFile(path, mode);
        } catch (Exception e) {
            return null;
        }
    }
    public static void seek(RandomAccessFile raf, long pos) {
        try {
            raf.seek(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void close(RandomAccessFile raf) {
        try {
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void writeWithDebug(RandomAccessFile raf, int b) {
        try {
            raf.write(b);
            if(Lang.mode == 1) System.out.printf(b + " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void writeBytes(RandomAccessFile raf, int[] b) {
        try {
            for (int i : b) {
                raf.write(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int read(RandomAccessFile raf) {
        int b = -1;
        try {
            b = raf.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
    public static int readWithDebug(RandomAccessFile raf) {
        int b = -1;
        try {
            b = raf.read();
            if(Lang.mode == 1) System.out.printf(b + " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
    public static int[] readBytes(RandomAccessFile raf, int n) {
        int[] res = new int[n];
        for (int i = 0; i < n; i++){
            res[i] = read(raf);
        }
        return res;
    }
    public static int[] readBytesWithDebug(RandomAccessFile raf, int n) {
        int[] res = new int[n];
        for (int i = 0; i < n; i++){
            res[i] = readWithDebug(raf);
        }
        return res;
    }
    public static String readWord(RandomAccessFile raf){
        String w = "";
        int b = read(raf);
        while (b != 10 && b != 32 && b != 9 && b != -1) {
            w += (char) b;
            b = read(raf);
        }
        if (b == 10 && w.isEmpty()) {
            w = "\n";
        }
        if (b == 9 && w.isEmpty()) {
            w = "\t";
        }
        return w;
    }
    public static String[] readWords(RandomAccessFile raf, int n) {
        String[] res = new String[n];
        for (int i = 0; i < n; i++){
            res[i] = readWord(raf);
        }
        return res;
    }
    public static void readLine(RandomAccessFile raf) {
        try {
            raf.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
