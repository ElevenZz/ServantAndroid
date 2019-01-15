package servant.servantandroid.viewmodel;

import android.view.View;

import com.xwray.groupie.ExpandableGroup;

import java.util.HashMap;
import java.util.Map;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import servant.servantandroid.databinding.HeaderLayoutBinding;
import servant.servantandroid.internal.ServantInstance;
import servant.servantandroid.internal.api_mirror.ApiElement;
import servant.servantandroid.internal.api_mirror.ApiListener;
import servant.servantandroid.internal.api_mirror.Module;
import servant.servantandroid.view.ClickableItem;
import servant.servantandroid.view.ExpandableHeaderItem;
import servant.servantandroid.viewmodel.persistence.DatabaseService;

public class InstanceAdapter extends ExpandableHeaderItem
    implements ApiListener<Module>, ClickableItem {

    private Map<Module, ModuleAdapter> m_modules;
    private ComponentActivity m_context;
    private ServantInstance m_instance;
    private MutableLiveData<ModuleAdapter> m_selectedModule;

    InstanceAdapter(ComponentActivity ctx, ServantInstance instance, MutableLiveData<ModuleAdapter> selected) {
        m_context = ctx;
        m_instance = instance;
        m_selectedModule = selected;

        instance.getModuleHandler().addListener(this);
    }

    @Override
    public void bind(@NonNull HeaderLayoutBinding viewBinding, int position) {
        super.bind(viewBinding, position);

        viewBinding.title.setText(getName());
        viewBinding.subtitle.setText(m_instance.toString());
    }

    @Override public int getSwipeDirs()      { return ItemTouchHelper.RIGHT; }
    @Override public void onClick(View view) { m_instance.getModuleHandler().update(); }
    @Override public boolean isClickable()   { return true; }

    public String getName() { return m_instance.getName(); }
    public ServantInstance getInstance() { return m_instance; }

    @Override
    public void setExpandableGroup(@NonNull ExpandableGroup onToggleListener) {
        super.setExpandableGroup(onToggleListener);
        m_modules = new HashMap<>();
        for(Module mod : m_instance.getModuleHandler().getChilds()) {
            ModuleAdapter adapter = new ModuleAdapter(m_context, mod, m_selectedModule);
            m_modules.put(mod, adapter);
            onToggleListener.add(adapter);
        }
    }

    @Override
    public void onAddChild(Module item) {
        ModuleAdapter adapter = new ModuleAdapter(m_context, item, m_selectedModule);
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
        DatabaseService.getInstance().updateServantInstance(m_instance);
    }
}
