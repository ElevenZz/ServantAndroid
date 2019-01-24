package servant.servantandroid.viewmodel;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import servant.servantandroid.R;
import servant.servantandroid.databinding.TextParameterLayoutBinding;
import servant.servantandroid.internal.api_mirror.parameters.BaseParameter;
import servant.servantandroid.internal.api_mirror.parameters.TextParameter;

/**
 * an adapter for a text parameter consisting of a single textbox
 * which is automatically synchronized with the underlying text parameters value field
 */
public class TextParameterAdapter extends BaseParameterAdapter<TextParameterLayoutBinding, TextParameter> {
    TextParameterAdapter(FragmentActivity ctx, TextParameter element) {
        super(ctx, element);
    }

    /**
     * does nothing since this adapter cant have child's
     * @param child the underlying api element
     * @return always returns null
     */
    @Override
    protected BaseParameterAdapter instanciateChildAdapter(BaseParameter child) { return null; }

    /**
     * @return text parameter layout
     */
    @Override public int getLayout() { return R.layout.text_parameter_layout; }

    /**
     * binds the parameter name and value
     * sets up a text changed listener to update the value field when the textbox content is changed
     * @param viewBinding the binding for the layout
     * @param position this parameter is irrelevant for us since we are only a single element
     */
    @Override public void bind(@NonNull TextParameterLayoutBinding viewBinding, int position) {
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
