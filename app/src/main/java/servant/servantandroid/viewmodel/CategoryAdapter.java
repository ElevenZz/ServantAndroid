package servant.servantandroid.viewmodel;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import servant.servantandroid.R;
import servant.servantandroid.databinding.CategoryLayoutBinding;
import servant.servantandroid.internal.api_mirror.Capability;
import servant.servantandroid.internal.api_mirror.Category;

/**
 * adapter for a category api element
 * only has a name and the child capabilities
 */
public class CategoryAdapter extends ApiAdapter<CategoryLayoutBinding, Category, Capability, CapabilityAdapter> {
    CategoryAdapter(FragmentActivity ctx, Category category) {
        super(ctx, category);
    }

    /**
     * bind the name of the category
     * @param viewBinding the category layout binding
     * @param position irrelevant to uss
     */
    @Override public void bind(@NonNull CategoryLayoutBinding viewBinding, int position) {
        bindExpandIcon(viewBinding.iconExpand);
        viewBinding.title.setText(m_element.getName());
    }

    @Override
    protected CapabilityAdapter instanciateChildAdapter(Capability child) {
        return new CapabilityAdapter(m_context, child);
    }

    /**
     * @return category layout
     */
    @Override public int getLayout() { return R.layout.category_layout; }
}
