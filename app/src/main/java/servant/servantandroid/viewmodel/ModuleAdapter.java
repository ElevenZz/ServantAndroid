package servant.servantandroid.viewmodel;

import android.view.View;

import com.xwray.groupie.databinding.BindableItem;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import servant.servantandroid.R;
import servant.servantandroid.databinding.ActivityStartBinding;
import servant.servantandroid.databinding.ModuleItemBinding;
import servant.servantandroid.internal.api_mirror.Module;
import servant.servantandroid.view.ClickableItem;

public class ModuleAdapter extends BindableItem<ModuleItemBinding>
    implements ClickableItem {

    private Module m_module;
    private ComponentActivity m_context;
    private MutableLiveData<ModuleAdapter> m_selectedModule;

    ModuleAdapter(ComponentActivity ctx, Module module, MutableLiveData<ModuleAdapter> selected) {
        m_context = ctx;
        m_module = module;
        m_selectedModule = selected;
    }

    @Override
    public void bind(@NonNull ModuleItemBinding viewBinding, int position) {
        viewBinding.name.setText(m_module.getName());
        viewBinding.details.setText(m_module.toString());
        viewBinding.version.setText(m_module.getVersion());
    }

    @Override
    public int getLayout() { return R.layout.module_item; }
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
}
