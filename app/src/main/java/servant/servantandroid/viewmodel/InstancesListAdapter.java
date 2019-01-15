package servant.servantandroid.viewmodel;

import android.app.AlertDialog;

import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.Section;
import com.xwray.groupie.TouchCallback;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import servant.servantandroid.R;
import servant.servantandroid.internal.ServantInstance;
import servant.servantandroid.viewmodel.persistence.DatabaseService;

public class InstancesListAdapter extends Section {
    // BiMap would be cool here but i don't want more deps
    private Map<InstanceAdapter, ServantInstance> m_instances;
    private TouchCallback m_touchCallback;
    private ComponentActivity m_context;
    private MutableLiveData m_selectedModule;

    public InstancesListAdapter(ComponentActivity ctx, MutableLiveData<ModuleAdapter> selectedModule) {
        m_instances = new HashMap<>();
        m_context = ctx;

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

    public void removeInstance(InstanceAdapter instance) {
        new AlertDialog.Builder(m_context)
            .setMessage(String.format(m_context.getString(R.string.remove_approval), instance.getName()))
            .setPositiveButton(R.string.ok, (d, w) -> {
                // remove
                DatabaseService.getInstance().removeServantInstance(m_instances.remove(instance));
                remove(instance.getExpandableGroup());
            })
            .setNegativeButton(R.string.cancel, (d, w) -> {
                notifyChanged(); // let the item come back
                d.cancel();
            })
            .create()
            .show();
    }

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

    private void addInstance(ServantInstance instance) {
        InstanceAdapter adapter  = new InstanceAdapter(m_context, instance, m_selectedModule);
        ExpandableGroup group    = new ExpandableGroup(adapter);

        m_instances.put(adapter, instance);
        add(group);
    }

    public TouchCallback getTouchCallback() { return m_touchCallback; }
}
