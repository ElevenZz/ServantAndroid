package servant.servantandroid.viewmodel;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import servant.servantandroid.R;
import servant.servantandroid.databinding.TextParameterLayoutBinding;
import servant.servantandroid.internal.api_mirror.parameters.BaseParameter;
import servant.servantandroid.internal.api_mirror.parameters.TextParameter;

public class TextParameterAdapter extends BaseParameterAdapter<TextParameterLayoutBinding, TextParameter> {

    TextParameterAdapter(FragmentActivity ctx, TextParameter element) {
        super(ctx, element);
    }

    @Override
    protected BaseParameterAdapter instanciateChildAdapter(BaseParameter child) { return null; }

    @Override
    public int getLayout() {
        return R.layout.text_parameter_layout;
    }

    @Override
    public void bind(@NonNull TextParameterLayoutBinding viewBinding, int position) {
        viewBinding.name.setText(m_element.getName());
        viewBinding.value.setText(m_element.getValue());
        viewBinding.value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) { m_element.setValue(s.toString()); }
        });
    }
}
