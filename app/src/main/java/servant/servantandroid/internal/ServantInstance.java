package servant.servantandroid.internal;

import servant.servantandroid.internal.ModuleTree.ModuleHandler;

public class ServantInstance {
    private ModuleHandler m_modules;
    private ApiService    m_api;

    public ServantInstance(String remoteHost) {
        m_api     = new ApiService(remoteHost);
        m_modules = new ModuleHandler(m_api);
    }
}
