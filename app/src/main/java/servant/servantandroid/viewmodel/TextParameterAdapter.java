package servant.servantandroid.viewmodel;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import servant.servantandroid.R;
import servant.servantandroid.databinding.HeaderLayoutBinding;
import servant.servantandroid.internal.api_mirror.parameters.BaseParameter;

public class TextParameterAdapter extends BaseParameterAdapter {

    TextParameterAdapter(ComponentActivity ctx, BaseParameter element) {
        super(ctx, element);
    }

    @Override
    protected BaseParameterAdapter instanciateChildAdapter(BaseParameter child) { return null; }

    @Override
    public int getLayout() {
        return R.layout.header_layout;
    }

    @Override
    public void bind(@NonNull HeaderLayoutBinding viewBinding, int position) {
        viewBinding.title.setText(m_element.getName());
    }
}
