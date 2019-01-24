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

    /**
     * creates an api service based on the given remote host url
     * @param remoteHost remote host url also containing the port
     * @param name a local display name for the instance
     * @throws MalformedURLException thrown if the remote host url is not parsable
     */
    public ServantInstance(String remoteHost, String name) throws MalformedURLException {
        // for making api requests
        m_api     = new ApiService(remoteHost);
        m_modules = new ModuleHandler(m_api);
        m_name = name;
    }

    /**
     * a string representation of this instance containing the remote host
     * @return remote host as string
     */
    @Override public @NonNull String toString() { return m_api.getRemoteHost(); }

    public String getName()            { return m_name; }
    public void   setName(String name) { m_name = name; }

    public ModuleHandler getModuleHandler() { return m_modules; }
}
