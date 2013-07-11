package flex2.compiler.util;

import flex2.compiler.CompilerAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CompillerThreadPoolUtil {
    public static final ExecutorService threadPull = Executors.newFixedThreadPool(100);
    private static List commands = new ArrayList<Callable>();
    private static boolean isBroken = false;


    public static boolean flush() {
        isBroken = false;
        try {
            threadPull.invokeAll(commands);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        commands.clear();
        return isBroken;
    }

    public static boolean flushWithoutMultiThreading() {
        isBroken = false;
        try {
            for (Object command : commands) {
                ((Callable) command).call();
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        commands.clear();
        return isBroken;
    }

    public static void addCommand(final Runnable command) {
        commands.add(new Callable() {
            @Override
            public Object call() throws Exception {
                if (!isBroken) {
                    command.run();
                    if (tooManyErrors()) {
                        ThreadLocalToolkit.log(new CompilerAPI.TooManyErrors());
                        isBroken = true;
                    } else if (forcedToStop()) {
                        ThreadLocalToolkit.log(new CompilerAPI.ForcedToStop());
                        isBroken = true;
                    }

                }
                return null;
            }
        });
    }

    public static void logCommands(String message) {
        //System.out.println(message + ": " + commands.size());
    }


    private synchronized static boolean tooManyErrors() {
        return ThreadLocalToolkit.errorCount() > 100;
    }

    private synchronized static boolean forcedToStop() {
        CompilerControl cc = ThreadLocalToolkit.getCompilerControl();
        return (cc != null && cc.getStatus() == CompilerControl.STOP);
    }

    public static void stopExecute() {
        isBroken = true;
    }
}
