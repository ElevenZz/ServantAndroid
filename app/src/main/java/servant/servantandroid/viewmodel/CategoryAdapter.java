package servant.servantandroid.viewmodel;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import servant.servantandroid.R;
import servant.servantandroid.databinding.HeaderLayoutBinding;
import servant.servantandroid.internal.api_mirror.Capability;
import servant.servantandroid.internal.api_mirror.Category;

public class CategoryAdapter extends ApiAdapter<HeaderLayoutBinding, Category, Capability, CapabilityAdapter> {

    CategoryAdapter(ComponentActivity ctx, Category category) {
        super(ctx, category);
    }

    @Override
    public void bind(@NonNull HeaderLayoutBinding viewBinding, int position) {
        bindExpandIcon(viewBinding.icon);
        viewBinding.title.setText(m_element.getName());
    }

    @Override
    protected CapabilityAdapter instanciateChildAdapter(Capability child) {
        return new CapabilityAdapter(m_context, child);
    }

    @Override public int getLayout() { return R.layout.header_layout; }
}
