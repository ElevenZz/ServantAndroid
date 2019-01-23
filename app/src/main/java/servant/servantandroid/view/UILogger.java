package servant.servantandroid.view;

import androidx.fragment.app.FragmentManager;

import servant.servantandroid.internal.Logger;

/**
 * opens an error dialog if an error is to be logged
 */
public class UILogger extends Logger {
    private FragmentManager m_fragmentMgr;

    UILogger(FragmentManager mgr) { m_fragmentMgr = mgr; }

    @Override
    public void log(Type type, String message, Object origin, Throwable error) {
        super.log(type, message, origin, error);
        if(type == Type.ERROR)
            ErrorFragment.showErrorFragment(
                m_fragmentMgr,
                message,
                error == null? null : error.toString()
            );
    }
}
