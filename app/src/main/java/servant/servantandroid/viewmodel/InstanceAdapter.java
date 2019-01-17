package servant.servantandroid.viewmodel;

import android.view.View;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import servant.servantandroid.R;
import servant.servantandroid.databinding.HeaderLayoutBinding;
import servant.servantandroid.internal.ServantInstance;
import servant.servantandroid.internal.api_mirror.Module;
import servant.servantandroid.internal.api_mirror.ModuleHandler;
import servant.servantandroid.view.ClickableItem;
import servant.servantandroid.viewmodel.persistence.DatabaseService;

public class InstanceAdapter extends ApiAdapter<HeaderLayoutBinding, ModuleHandler, Module, ModuleAdapter>
    implements ClickableItem {

    //private Map<Module, ModuleAdapter> m_modules;
    //private ComponentActivity m_context;
    //private ServantInstance m_instance;
    private MutableLiveData<ModuleAdapter> m_selectedModule;
    private ServantInstance m_instance;

    InstanceAdapter(ComponentActivity ctx, ServantInstance instance, MutableLiveData<ModuleAdapter> selected) {
        super(ctx, instance.getModuleHandler());

        m_selectedModule = selected;
        m_instance = instance;
    }

    @Override
    public void bind(@NonNull HeaderLayoutBinding viewBinding, int position) {
        bindExpandIcon(viewBinding.icon);
        viewBinding.title.setText(m_instance.getName());
        viewBinding.subtitle.setText(m_instance.toString());
    }

    @Override public int getSwipeDirs() { return ItemTouchHelper.RIGHT; }

    @Override
    public int getLayout() {
        return R.layout.header_layout;
    }

    @Override public void onClick(View view) { m_element.update(); }
    @Override public boolean isClickable()   { return true; }

    public String getName() { return m_instance.getName(); }
    public ServantInstance getInstance() { return m_instance; }
/*
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
*/
    @Override
    protected ModuleAdapter instanciateChildAdapter(Module child) {
        return new ModuleAdapter(m_context, child, m_selectedModule);
    }

    /*
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
    */
    @Override
    public void notifyChanged() {
        super.notifyChanged();
        DatabaseService.getInstance().updateServantInstance(m_instance);
    }
}
