package servant.servantandroid.viewmodel;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import servant.servantandroid.databinding.HeaderLayoutBinding;
import servant.servantandroid.internal.ServantInstance;
import servant.servantandroid.internal.api_mirror.Module;
import servant.servantandroid.view.ExpandableHeaderItem;

public class InstanceAdapter extends ExpandableHeaderItem<ServantInstance> {

    private List<ModuleAdapter> m_modules;

    InstanceAdapter(ComponentActivity ctx, ServantInstance instance) {
        super(ctx, instance);

        m_modules = new ArrayList<>();
        for(Module mod : instance.getModuleHandler().getChilds()) {
            ModuleAdapter adapter = new ModuleAdapter(m_context, mod);
            m_modules.add(adapter);
            m_expandableGroup.add(adapter);
        }
    }

    @Override
    public void bind(@NonNull HeaderLayoutBinding viewBinding, int position) {
        super.bind(viewBinding, position);

        viewBinding.title.setText(getName());
        viewBinding.subtitle.setText(m_livedata.getValue().toString());
    }

    public void update() {
        m_livedata.getValue().getModuleHandler().update();
        for(Module mod : m_livedata.getValue().getModuleHandler().getChilds()) {
            ModuleAdapter adapter = new ModuleAdapter(m_context, mod);
            m_modules.add(adapter);
            m_expandableGroup.add(adapter);
        }
    }

    @Override
    public boolean isClickable() { return true; }

    public String getName() { return m_livedata.getValue().getName(); }
    public ServantInstance getInstance() { return m_livedata.getValue(); }
}
