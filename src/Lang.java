import java.io.RandomAccessFile;

public class Lang {
    private static int callType;
    public static int mode;
    public static RandomAccessFile first;
    public static RandomAccessFile second;
    public static void main(String[] args) {
        switch (args[0]) {
            case "run" -> callType = 0;
            case "compile" -> callType = 1;
            default -> Err.sendErrAndExit("unknown call type", 1);
        }
        switch (args[1]){
            case "-n" -> mode = 0;
            case "-d" -> mode = 1;
            default -> Err.sendErrAndExit("unknown parameter", 2);
        }
        first = Files.setRAF(args[2], "r");
        second = Files.setRAF(args[3], "rw");
        switch (callType){
            case 0 -> Runner.run();
            case 1 -> Compiler.run();
        }
    }
}
// -n normal
// -d debug

// java Lang run -n file_to_run file_to_save
// java Lang compile -d file_to_compile file_to_write

