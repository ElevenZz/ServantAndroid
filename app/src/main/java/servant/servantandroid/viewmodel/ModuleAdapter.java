package servant.servantandroid.viewmodel;

import android.view.View;

import com.xwray.groupie.ExpandableGroup;

import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import servant.servantandroid.R;
import servant.servantandroid.databinding.ActivityStartBinding;
import servant.servantandroid.databinding.ModuleItemBinding;
import servant.servantandroid.internal.api_mirror.Category;
import servant.servantandroid.internal.api_mirror.Module;
import servant.servantandroid.view.ClickableItem;

/**
 * the adapter for the module api element
 * clicking this item causes the selected module to be updated
 * which in turn causes the observer in the main activity to update the main view
 */
public class ModuleAdapter extends ApiAdapter<ModuleItemBinding, Module, Category, CategoryAdapter>
    implements ClickableItem {

    /**
     * the observable livedata holding the selected module
     */
    private MutableLiveData<ModuleAdapter> m_selectedModule;

    ModuleAdapter(FragmentActivity ctx, Module module, MutableLiveData<ModuleAdapter> selected) {
        super(ctx, module);
        m_selectedModule = selected;
    }

    @Override protected CategoryAdapter instanciateChildAdapter(Category child) {
        return new CategoryAdapter(m_context, child);
    }

    /**
     * sets the name, details and version of the view
     * @param viewBinding the binding for the module layout
     * @param position irrelevant for us since we are representing a single view
     */
    @Override public void bind(@NonNull ModuleItemBinding viewBinding, int position) {
        viewBinding.name.setText(m_element.getName());
        viewBinding.details.setText(m_element.toString());
        viewBinding.version.setText(m_element.getVersion());
    }

    /**
     * @return returns the module item layout
     */
    @Override public int getLayout()       { return R.layout.module_item; }

    /**
     * @return this item should be clickable
     */
    @Override public boolean isClickable() { return true; }

    /**
     * provided by the clickable interface
     * sets the currently selected module to this instance
     * @param view
     */
    @Override public void onClick(View view) {
        m_selectedModule.setValue(this);
        // should be fast enuff since we always search for one of the first items
        ActivityStartBinding start = DataBindingUtil.getBinding(
            view.getRootView().findViewById(R.id.drawer_layout)
        );
        start.drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * overriding since the base implementation adds the child to the ui group which we don't want
     * since the the childs of this instance are displayed in the main view
     * @param item the item that was added
     */
    @Override public void onAddChild(Category item) {
        m_childs.put(item, new ExpandableGroup(new CategoryAdapter(m_context, item)));
        notifyChanged();
    }

    /**
     * overriding since the base implementation removes the child from the ui group which we don't want
     * since the the childs of this instance are displayed in the main view
     * @param item the item that was removed
     */
    @Override public void onRemoveChild(Category item) {
        m_childs.remove(item);
        notifyChanged();
    }
}
