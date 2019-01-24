package servant.servantandroid.viewmodel;

import android.app.AlertDialog;

import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.Section;
import com.xwray.groupie.TouchCallback;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import servant.servantandroid.R;
import servant.servantandroid.internal.ServantInstance;
import servant.servantandroid.viewmodel.persistence.DatabaseService;

/**
 * the drawer content displaying the saved instances
 */
public class InstancesListAdapter extends Section {

    /**
     * mapping the vm adapters to the model instances
     */
    private Map<InstanceAdapter, ServantInstance> m_instances;

    /**
     * the touch callback to implement swipe to delete functionality
     */
    private TouchCallback m_touchCallback;

    /**
     * reference to the main activity
     */
    private FragmentActivity m_context;

    /**
     * the currently selected module
     */
    private MutableLiveData m_selectedModule;

    /**
     * loads the cached servant instances via DatabaseService
     * implements the onSwiped callback to delete instances
     * @param ctx reference to the main activity
     * @param selectedModule selected module livedata
     */
    public InstancesListAdapter(FragmentActivity ctx, MutableLiveData<ModuleAdapter> selectedModule) {
        m_instances = new HashMap<>();
        m_context = ctx;
        m_selectedModule = selectedModule;

        // 50 shades of lambda
        DatabaseService.getInstance().getServantInstances((ServantInstance instance) ->
            m_context.runOnUiThread(() -> addInstance(instance))
        );

        m_touchCallback = new TouchCallback() {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeInstance((InstanceAdapter) getItem(viewHolder.getAdapterPosition()));
            }
        };
    }

    /**
     * remove the instance from the UI and delete it from the local cache
     * opens a dialog box asking you if you really want to delete the instance
     * @param instance the instance to delete
     */
    public void removeInstance(InstanceAdapter instance) {
        new AlertDialog.Builder(m_context)
            .setMessage(String.format(m_context.getString(R.string.remove_approval), instance.getName()))
            .setPositiveButton(R.string.ok, (d, w) -> {
                // remove
                DatabaseService.getInstance().removeServantInstance(m_instances.remove(instance));
                remove(instance.getGroup());
            })
            .setNegativeButton(R.string.cancel, (d, w) -> {
                notifyChanged(); // let the item come back
                d.cancel();
            })
            .create()
            .show();
    }

    /**
     * adds an instance to the local cache and then calls
     * a local method to add the instance to the ui
     * @param host the url of the remote server containing the port
     * @param name the local display name of the new instance
     * @throws MalformedURLException if the url is in an invalid format
     */
    public void addInstance(String host, String name) throws MalformedURLException {
        for(InstanceAdapter instance : m_instances.keySet()) {
            if(instance.getName().equalsIgnoreCase(name))
                throw new IllegalArgumentException(
                    "there already exists a servant instance with that name");
        }

        if(name.isEmpty()) throw new IllegalArgumentException(
            "the instance name cannot be empty");

        ServantInstance instance = new ServantInstance(host, name);
        addInstance(instance);

        // add new instance to local db
        DatabaseService.getInstance().insertServantInstance(instance);
    }

    /**
     * update all current instances in the local cache
     */
    public void saveInstances() {
        for(ServantInstance inst : m_instances.values())
            DatabaseService.getInstance().updateServantInstance(inst);
    }

    /**
     * add the instance to the UI
     * @param instance instance to add
     */
    private void addInstance(ServantInstance instance) {
        InstanceAdapter adapter  = new InstanceAdapter(m_context, instance, m_selectedModule);
        ExpandableGroup group    = new ExpandableGroup(adapter);

        m_instances.put(adapter, instance);
        add(group);
    }

    /**
     * get the touch callback used for swipe to delete functionality
     * @return the touch callback
     */
    public TouchCallback getTouchCallback() { return m_touchCallback; }
}
