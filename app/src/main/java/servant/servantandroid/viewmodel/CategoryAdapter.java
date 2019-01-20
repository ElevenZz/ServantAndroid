package servant.servantandroid.viewmodel;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import servant.servantandroid.R;
import servant.servantandroid.databinding.CategoryLayoutBinding;
import servant.servantandroid.internal.api_mirror.Capability;
import servant.servantandroid.internal.api_mirror.Category;

public class CategoryAdapter extends ApiAdapter<CategoryLayoutBinding, Category, Capability, CapabilityAdapter> {

    CategoryAdapter(FragmentActivity ctx, Category category) {
        super(ctx, category);
    }

    @Override
    public void bind(@NonNull CategoryLayoutBinding viewBinding, int position) {
        bindExpandIcon(viewBinding.iconExpand);
        viewBinding.title.setText(m_element.getName());
    }

    @Override
    protected CapabilityAdapter instanciateChildAdapter(Capability child) {
        return new CapabilityAdapter(m_context, child);
    }

    @Override public int getLayout() { return R.layout.category_layout; }
}
