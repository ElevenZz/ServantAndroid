package servant.servantandroid.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.content.SharedPreferences;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.util.Map;

import servant.servantandroid.internal.Logger;
import servant.servantandroid.internal.api_mirror.ApiElement;
import servant.servantandroid.internal.api_mirror.Module;
import servant.servantandroid.internal.ServantInstance;
import servant.servantandroid.view.UILogger;

public class MainViewModel extends ViewModel {
    private MutableLiveData<Map<String, ServantInstance>> m_instances = new MutableLiveData<>();

    private MutableLiveData<ServantInstance> m_selectedInstance = new MutableLiveData<>();
    private MutableLiveData<Module>          m_selectedModule   = new MutableLiveData<>();

    private SharedPreferences m_preferences;

    public MutableLiveData<ServantInstance> getSelectedInstance() { return m_selectedInstance; }
    public MutableLiveData<Module>          getSelectedModule()   { return m_selectedModule;   }
    public MutableLiveData<Map<String, ServantInstance>> getInstances() { return m_instances;  }

    public MainViewModel(SharedPreferences prefs, FragmentManager fragmentManager) {
        Logger.setLogger(new UILogger(fragmentManager));
        m_preferences = prefs;
        /*
        // load json from shared preferences
        String instancesString = m_preferences.getString(("instances"), "");
        Type mapType = new TypeToken<Map<String, ServantInstance>>() { }.getType();

        // deserialize json to get my instances
        if(instancesString.isEmpty()) m_instances.setValue(new HashMap<>());
        else m_instances.setValue(new Gson().fromJson(instancesString, mapType));
        */
    }

    public void selectInstance(String name) {
        m_selectedInstance.setValue(m_instances.getValue().get(name));
    }

    public void selectModule(String name) {
        if(m_selectedInstance.getValue() == null) throw new IllegalStateException(
            "your tried to select a module without a selected instance. " +
            "this shouldn't even be possible");

        ApiElement elem = m_selectedInstance
            .getValue()
            .getModuleHandler()
            .getChildByName(name);

        if(elem instanceof Module) m_selectedModule.setValue((Module)elem);
    }

    // passing the exception one more time since i need the main activities SupportFragmentManager
    // to create an error dialog -_-
    public void addInstance(String host, String name) throws MalformedURLException {
        // why is this not the default put behavior god dammit
        if(m_instances.getValue().containsKey(name)) throw new IllegalArgumentException(
            "there already exists a servant instance with that name");

        if(name.isEmpty()) throw new IllegalArgumentException(
            "the instance name cannot be empty");

        m_instances.getValue().put(name, new ServantInstance(host, name));
        // tell LiveData something has changed
        m_instances.setValue(m_instances.getValue());

        // save instances to shared prefs
        m_preferences
            .edit()
            .putString(
                "instances",
                new Gson().toJson(m_instances.getValue())
            )
            .apply();
    }
}
