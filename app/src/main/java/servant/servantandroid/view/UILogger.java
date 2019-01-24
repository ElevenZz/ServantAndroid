package servant.servantandroid.view;

import androidx.fragment.app.FragmentManager;

import servant.servantandroid.internal.Logger;

/**
 * overrides the model's singleton logger
 * opens an error dialog if an error is to be logged
 * lets the base logger handle logging otherwise
 */
public class UILogger extends Logger {
    /**
     * the fragment manager used to open an error dialog
     */
    private FragmentManager m_fragmentMgr;

    UILogger(FragmentManager mgr) { m_fragmentMgr = mgr; }

    /**
     * logs a message. displays a dialog on error, calls base logger otherwise
     * @param type the type of the message. see Logger.Type for available types
     * @param message the actual string to log
     * @param origin from where did this log entry come from?
     * @param error an optional exception in case of an error
     *              will be used as the detailed error in the dialog
     */
    @Override public void log(Type type, String message, Object origin, Throwable error) {
        super.log(type, message, origin, error);
        if(type == Type.ERROR)
            ErrorFragment.showErrorFragment(
                m_fragmentMgr,
                message,
                error == null? null : error.toString()
            );
    }
}
