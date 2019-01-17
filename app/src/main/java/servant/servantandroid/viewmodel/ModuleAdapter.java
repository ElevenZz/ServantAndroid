package servant.servantandroid.viewmodel;

import android.view.View;

import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.databinding.BindableItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import servant.servantandroid.R;
import servant.servantandroid.databinding.ActivityStartBinding;
import servant.servantandroid.databinding.ModuleItemBinding;
import servant.servantandroid.internal.api_mirror.ApiElement;
import servant.servantandroid.internal.api_mirror.ApiListener;
import servant.servantandroid.internal.api_mirror.Category;
import servant.servantandroid.internal.api_mirror.Module;
import servant.servantandroid.view.ClickableItem;

public class ModuleAdapter extends ApiAdapter<ModuleItemBinding, Module, Category, CategoryAdapter>
    implements ClickableItem {

    private MutableLiveData<ModuleAdapter> m_selectedModule;

    ModuleAdapter(ComponentActivity ctx, Module module, MutableLiveData<ModuleAdapter> selected) {
        super(ctx, module);
        m_selectedModule = selected;
    }

    @Override
    protected CategoryAdapter instanciateChildAdapter(Category child) {
        return new CategoryAdapter(m_context, child);
    }

    @Override
    public void bind(@NonNull ModuleItemBinding viewBinding, int position) {
        viewBinding.name.setText(m_element.getName());
        viewBinding.details.setText(m_element.toString());
        viewBinding.version.setText(m_element.getVersion());
    }

    @Override public int getLayout()       { return R.layout.module_item; }
    @Override public boolean isClickable() { return true; }

    @Override
    public void onClick(View view) {
        m_selectedModule.setValue(this);
        // should be fast enuff since we always search for one of the first items
        ActivityStartBinding start = DataBindingUtil.getBinding(
            view.getRootView().findViewById(R.id.drawer_layout)
        );
        start.drawerLayout.closeDrawer(GravityCompat.START);
    }

    public Collection<ExpandableGroup> getCategories() { return m_childs.values(); }

    // overriding since the base implementation adds the child to the ui group which we don't want
    @Override
    public void onAddChild(Category item) {
        m_childs.put(item,  new ExpandableGroup(new CategoryAdapter(m_context, item)));
        notifyChanged();
    }

    @Override
    public void onRemoveChild(Category item) {
        m_childs.remove(item);
        notifyChanged();
    }
}
