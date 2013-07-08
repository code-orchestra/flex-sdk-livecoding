package flex2.compiler.util;

import flex2.compiler.CompilerAPI;
import flex2.compiler.Source;
import flex2.compiler.SubCompiler;
import flex2.compiler.SymbolTable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CompillerThreadPoolUtil {
    public static final ExecutorService threadPull = Executors.newFixedThreadPool(1);
    private static List commands = new ArrayList<Callable>();
    private static boolean isBrocken = false;


    public static boolean flush() {
        isBrocken = false;
        try {
            threadPull.invokeAll(commands);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        commands.clear();
        return isBrocken;
    }

    public static boolean flush(Boolean noThreads) {
        if (noThreads) {
            isBrocken = false;
            try {
                for (Object command : commands) {
                    ((Callable) command).call();
                }
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            commands.clear();
            return isBrocken;

        } else {
            return flush();
        }
    }

    public static void addCommand(final Runnable command) {
        commands.add(new Callable() {
            @Override
            public Object call() throws Exception {
                if (!isBrocken) {
                    command.run();
                    if (tooManyErrors()) {
                        ThreadLocalToolkit.log(new CompilerAPI.TooManyErrors());
                        isBrocken = true;
                    } else if (forcedToStop()) {
                        ThreadLocalToolkit.log(new CompilerAPI.ForcedToStop());
                        isBrocken = true;
                    }

                }
                return null;
            }
        });
    }


    private synchronized static boolean tooManyErrors() {
        return ThreadLocalToolkit.errorCount() > 100;
    }

    public synchronized static boolean forcedToStop() {
        CompilerControl cc = ThreadLocalToolkit.getCompilerControl();
        return (cc != null && cc.getStatus() == CompilerControl.STOP);
    }
}
