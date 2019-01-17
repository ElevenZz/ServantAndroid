package servant.servantandroid.viewmodel;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import servant.servantandroid.R;
import servant.servantandroid.databinding.HeaderLayoutBinding;
import servant.servantandroid.internal.api_mirror.Capability;
import servant.servantandroid.internal.api_mirror.parameters.BaseParameter;

public class CapabilityAdapter extends ApiAdapter<HeaderLayoutBinding, Capability, BaseParameter, BaseParameterAdapter> {

    CapabilityAdapter(ComponentActivity ctx, Capability capability) { super(ctx, capability); }

    @Override
    protected BaseParameterAdapter instanciateChildAdapter(BaseParameter child) {
        return BaseParameterAdapter.getParamtypeHandler(child.getClass()).apply(m_context, child);
    }

    @Override public int getLayout() { return R.layout.header_layout; }

    @Override
    public void bind(@NonNull HeaderLayoutBinding viewBinding, int position) {
        viewBinding.title.setText(m_element.getName());
    }
}
