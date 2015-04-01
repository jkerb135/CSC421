import java.io.PrintStream;
import java.text.MessageFormat;

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
