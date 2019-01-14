package servant.servantandroid.viewmodel;

import java.util.HashMap;
import java.util.Map;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import servant.servantandroid.databinding.HeaderLayoutBinding;
import servant.servantandroid.internal.ServantInstance;
import servant.servantandroid.internal.api_mirror.ApiElement;
import servant.servantandroid.internal.api_mirror.ApiListener;
import servant.servantandroid.internal.api_mirror.Module;
import servant.servantandroid.view.ExpandableHeaderItem;
import servant.servantandroid.viewmodel.persistence.DatabaseService;

public class InstanceAdapter extends ExpandableHeaderItem<ServantInstance>
    implements ApiListener<Module> {

    private Map<Module, ModuleAdapter> m_modules;

    InstanceAdapter(ComponentActivity ctx, ServantInstance instance) {
        super(ctx, instance);

        m_modules = new HashMap<>();
        for(Module mod : instance.getModuleHandler().getChilds()) {
            ModuleAdapter adapter = new ModuleAdapter(m_context, mod);
            m_modules.put(mod, adapter);
            m_expandableGroup.add(adapter);
        }

        instance.getModuleHandler().addListener(this);
    }

    @Override
    public void bind(@NonNull HeaderLayoutBinding viewBinding, int position) {
        super.bind(viewBinding, position);

        viewBinding.title.setText(getName());
        viewBinding.subtitle.setText(m_item.toString());
    }

    public void update() { m_item.getModuleHandler().update(); }

    @Override
    public boolean isClickable() { return true; }

    public String getName() { return m_item.getName(); }
    public ServantInstance getInstance() { return m_item; }

    @Override public int getSwipeDirs() { return ItemTouchHelper.RIGHT; }

    @Override
    public void onAddChild(Module item) {
        ModuleAdapter adapter = new ModuleAdapter(m_context, item);
        m_modules.put(item, adapter);
        notifyChanged();

        m_context.runOnUiThread(() -> m_expandableGroup.add(adapter));
    }

    @Override
    public void onRemoveChild(Module item) {
        notifyChanged();
        m_context.runOnUiThread(() -> m_expandableGroup.remove(m_modules.remove(item)));
    }

    @Override
    public void onUpdate(ApiElement me) { notifyChanged(); }

    @Override
    public void notifyChanged() {
        super.notifyChanged();
        DatabaseService.getInstance().updateServantInstance(m_item);
    }
}
