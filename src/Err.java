public class Err {
    public static void sendErrAndExit(String err, int code){
        System.err.println("( " + code + " ) ERROR: " + err);
        System.exit(1);
    }
}
