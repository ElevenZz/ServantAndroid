package servant.servantandroid.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.core.util.Consumer;
import servant.servantandroid.internal.Logger;

/**
 * a helper class for executing a function in a cached thread pool
 */
public class ExecuteAsync {
    private static ExecutorService m_pool;

    public static void execute(Runnable runnable) {
        check();
        m_pool.execute(runnable);
    }

    /**
     * execute function and call the callback with the result
     * @param task the function to execute asynchronously. must return a value
     * @param callback function to receive the result of task
     * @param <T> return type of task and parameter type of callback
     */
    public static<T> void execute(Callable<T> task, Consumer<T> callback) {
        check();

        m_pool.execute(() -> {
            try { callback.accept(task.call()); }
            catch (Exception e) { // let's at least log this
                Logger.getInstance().logError(
                    "a background task threw an exception.",
                    "asnytask", e
                );
            }
        });
    }

    /**
     * check if thread pool has already been instantiated and instantiates it if not
     */
    private static void check() {
        // not making a singleton of ExecuteAsync since i believe that would be clunky af
        if(m_pool == null) { m_pool = Executors.newCachedThreadPool(); }
    }
}
