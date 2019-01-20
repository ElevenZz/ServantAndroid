package servant.servantandroid.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.core.util.Consumer;
import servant.servantandroid.internal.Logger;

public class ExecuteAsync {
    private static ExecutorService m_pool;

    public static void execute(Runnable runnable) {
        check();
        m_pool.execute(runnable);
    }

    public static<T> void execute(Callable<T> task, Consumer<T> callback) {
        check();

        m_pool.execute(() -> {
            try { callback.accept(task.call()); }
            catch (Exception e) { // let's at least log this
                Logger.getInstance().logError(
                    "some background task threw an exception.",
                    "asnytask", e
                );
            }
        });
    }

    private static void check() {
        // not making a singleton of ExecuteAsync since i believe that would be clunky af
        if(m_pool == null) { m_pool = Executors.newCachedThreadPool(); }
    }
}
