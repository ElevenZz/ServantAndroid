package servant.servantandroid.viewmodel;

import android.view.View;

import com.xwray.groupie.ExpandableGroup;

import java.util.HashMap;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import servant.servantandroid.databinding.HeaderLayoutBinding;
import servant.servantandroid.internal.api_mirror.ApiElement;
import servant.servantandroid.internal.api_mirror.ApiListener;
import servant.servantandroid.internal.api_mirror.Capability;
import servant.servantandroid.internal.api_mirror.Category;
import servant.servantandroid.view.ExpandableHeaderItem;

public class CategoryAdapter extends ExpandableHeaderItem
    implements ApiListener<Capability> {

    private HashMap<Capability, CapabilityAdapter> m_capabilities;
    private ComponentActivity m_context;
    private Category m_category;

    CategoryAdapter(ComponentActivity ctx, Category category) {
        m_context = ctx;
        m_category = category;

        category.addListener(this);
    }

    @Override
    public void bind(@NonNull HeaderLayoutBinding viewBinding, int position) {
        super.bind(viewBinding, position);
        viewBinding.title.setText(m_category.getName());
    }

    @Override
    public void setExpandableGroup(@NonNull ExpandableGroup onToggleListener) {
        super.setExpandableGroup(onToggleListener);
        m_capabilities = new HashMap<>();
        for(Capability cap : m_category.getChilds()) {
            CapabilityAdapter adapter = new CapabilityAdapter(m_context, cap);
            m_capabilities.put(cap, adapter);
            onToggleListener.add(adapter);
        }
    }

    @Override
    public void onAddChild(Capability item) {
        CapabilityAdapter adapter = new CapabilityAdapter(m_context, item);
        m_capabilities.put(item, adapter);
        notifyChanged();

        m_context.runOnUiThread(() -> m_expandableGroup.add(adapter));
    }

    @Override
    public void onRemoveChild(Capability item) {
        notifyChanged();
        m_context.runOnUiThread(() -> m_expandableGroup.remove(m_capabilities.remove(item)));
    }

    @Override
    public void onUpdate(ApiElement item) { notifyChanged(); }
}
