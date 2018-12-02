package servant.servantandroid.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import servant.servantandroid.internal.ModuleTree.Module;
import servant.servantandroid.internal.ServantInstance;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ServantInstance> m_selectedInstance = new MutableLiveData<>();
    private MutableLiveData<Module>          m_selectedModule   = new MutableLiveData<>();

    public MutableLiveData<ServantInstance> GetSelectedInstance() { return m_selectedInstance; }
    public MutableLiveData<Module>          GetSelectedModule()   { return m_selectedModule;   }
}
