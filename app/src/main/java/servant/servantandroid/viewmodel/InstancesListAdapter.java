package servant.servantandroid.viewmodel;

import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.Section;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import androidx.activity.ComponentActivity;
import servant.servantandroid.internal.ServantInstance;
import servant.servantandroid.viewmodel.persistence.DatabaseService;

public class InstancesListAdapter extends Section {
    private List<InstanceAdapter> m_instances;

    private ComponentActivity m_context;

    public InstancesListAdapter(ComponentActivity ctx) {
        m_instances = new ArrayList<>();
        m_context = ctx;

        // 50 shades of lambda
        DatabaseService.getInstance().getInstances((ServantInstance instance) ->
            m_context.runOnUiThread(() -> addInstance(instance))
        );
    }

    public void addInstance(String host, String name) throws MalformedURLException {
        for(InstanceAdapter instance : m_instances) {
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
        InstanceAdapter adapter  = new InstanceAdapter(m_context, instance);
        ExpandableGroup group    = new ExpandableGroup(adapter);

        m_instances.add(adapter);
        add(group);
    }
}
