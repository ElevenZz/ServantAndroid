package servant.servantandroid.viewmodel;

import com.xwray.groupie.databinding.BindableItem;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import servant.servantandroid.R;
import servant.servantandroid.databinding.ModuleItemBinding;
import servant.servantandroid.internal.api_mirror.Module;

public class ModuleAdapter extends BindableItem<ModuleItemBinding> {

    private Module m_module;
    private ComponentActivity m_context;

    ModuleAdapter(ComponentActivity ctx, Module module) {
        m_context = ctx;
        m_module = module;
    }

    @Override
    public void bind(@NonNull ModuleItemBinding viewBinding, int position) {
        viewBinding.name.setText(m_module.getName());
        viewBinding.details.setText(m_module.toString());
        viewBinding.version.setText(m_module.getVersion());
    }

    @Override
    public int getLayout() { return R.layout.module_item; }
}
