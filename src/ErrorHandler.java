import java.util.LinkedList;

public class ErrorHandler {
    private static LinkedList<String> errors;

    static {
        errors = new LinkedList<String>();
    }

    public static int getNumErrors() {
        return errors.size();
    }

    public static void printErrors() {
        for (String error : errors) {
            System.err.println(error);
        }
    }

    public static void putError(String error) {
        errors.add(error);
        // Prevent flooding
        if (errors.size() >= CONFIG.MAX_ALLOWED_ERRORS) {
            try {
                printErrors();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.exit(1);
        }
    }

    public static void putLineError(int lineNumber, String strLine, String error) {
        putError("LN " + lineNumber + ": " + strLine + " --- " + error);
    }
}
