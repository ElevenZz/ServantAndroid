package servant.servantandroid.view;

import android.graphics.drawable.Animatable;
import android.view.View;

import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.ExpandableItem;
import com.xwray.groupie.databinding.BindableItem;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;

import servant.servantandroid.R;
import servant.servantandroid.databinding.HeaderLayoutBinding;

public class ExpandableHeaderItem<T>
    extends BindableItem<HeaderLayoutBinding>
    implements ExpandableItem {

    protected T m_item;
    protected ComponentActivity  m_context;

    protected ExpandableGroup m_expandableGroup;

    public ExpandableHeaderItem(ComponentActivity ctx, T item) {
        m_context = ctx;
        m_item = item;
    }

    @Override public void bind(@NonNull final HeaderLayoutBinding viewBinding, int position) {
        viewBinding.icon.setVisibility(View.VISIBLE);
        viewBinding.icon.setImageResource(
            m_expandableGroup.isExpanded() ? R.drawable.collapse_animated : R.drawable.expand_animated
        );

        viewBinding.icon.setOnClickListener((view) -> {
            m_expandableGroup.onToggleExpanded();
            bindIcon(viewBinding);
        });
    }

    private void bindIcon(HeaderLayoutBinding viewBinding) {
        viewBinding.icon.setVisibility(View.VISIBLE);
        viewBinding.icon.setImageResource(
            m_expandableGroup.isExpanded() ? R.drawable.collapse_animated : R.drawable.expand_animated
        );

        Animatable drawable = (Animatable) viewBinding.icon.getDrawable();
        drawable.start();
    }

    @Override
    public void setExpandableGroup(@NonNull ExpandableGroup onToggleListener) {
        this.m_expandableGroup = onToggleListener;
    }

    @Override
    public int getLayout() { return R.layout.header_layout; }
    public ExpandableGroup getExpandableGroup() { return m_expandableGroup; }
}
