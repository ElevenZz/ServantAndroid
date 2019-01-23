package servant.servantandroid.internal;

import androidx.annotation.NonNull;

import java.net.MalformedURLException;

import servant.servantandroid.internal.api_mirror.ModuleHandler;

/**
 * a wrapper around the modules handler for local information like the display name
 */
public class ServantInstance {
    private ModuleHandler m_modules;
    private ApiService    m_api;
    private String        m_name;

    public ServantInstance(String remoteHost, String name) throws MalformedURLException {
        // for making api requests
        m_api     = new ApiService(remoteHost);
        m_modules = new ModuleHandler(m_api);
        m_name = name;
    }


    @Override public @NonNull String toString() { return m_api.getRemoteHost(); }

    public String getName()            { return m_name; }
    public void   setName(String name) { m_name = name; }

    public ModuleHandler getModuleHandler() { return m_modules; }
}
