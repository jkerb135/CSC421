import java.io.PrintStream;
import java.text.MessageFormat;
/**
 * The Debug stream is used for debugging purposes to show the class and line
 * number the executed print line comes from. Its uses are to help locate
 * errors in files faster.
 *
 * @author Josh Kerbaugh
 * @version 1.0
 * @see PrintStream
 * @since 2015-26-2
 */
public class DebugStream extends PrintStream {
    private static final DebugStream INSTANCE = new DebugStream();

    public static void activate(){
        System.setOut(INSTANCE);
    }

    private DebugStream(){
        super(System.out);
    }

    @Override
    public void println(Object x){
        showLocation();
        super.println(x);
    }

    @Override
    public void println(String x) {
        showLocation();
        super.println(x);
    }

    private void showLocation() {
        StackTraceElement element = Thread.currentThread().getStackTrace()[4];
        super.print(MessageFormat.format("({0}) : ", element));
    }
}
