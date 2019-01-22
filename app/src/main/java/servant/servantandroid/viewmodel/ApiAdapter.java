package servant.servantandroid.viewmodel;

import android.graphics.drawable.Animatable;
import android.view.View;

import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.ExpandableItem;
import com.xwray.groupie.databinding.BindableItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import servant.servantandroid.R;
import servant.servantandroid.internal.api_mirror.ApiElement;
import servant.servantandroid.internal.api_mirror.ApiListener;

import java.util.Collection;
import java.util.HashMap;

public abstract class ApiAdapter<
    BindingType      extends ViewDataBinding,
    ApiElementType   extends ApiElement,
    ApiChildType     extends ApiElement,
    AdapterChildType extends ApiAdapter>

    extends BindableItem<BindingType>
    implements ApiListener<ApiChildType>, ExpandableItem {

    ExpandableGroup m_expandToggle;
    FragmentActivity m_context;
    // the thing were holding
    ApiElementType m_element;
    // link api element to adapter
    HashMap<ApiChildType, ExpandableGroup> m_childs;

    ApiAdapter(FragmentActivity ctx, ApiElementType element) {
        m_childs  = new HashMap<>();
        m_context = ctx;
        m_element = element;

        element.addListener(this);
    }

    protected abstract AdapterChildType instanciateChildAdapter(ApiChildType child);

    protected void bindExpandIcon(AppCompatImageView icon) {
        initializeIcon(icon);
        icon.setOnClickListener((view) -> {
            m_expandToggle.onToggleExpanded();
            initializeIcon(icon);
            ((Animatable)icon.getDrawable()).start();
        });
    }

    private void initializeIcon(AppCompatImageView icon) {
        icon.setVisibility(View.VISIBLE);
        icon.setImageResource(
            m_expandToggle.isExpanded() ?
                R.drawable.collapse_animated : R.drawable.expand_animated
        );
    }

    public void update(Runnable callback) { m_element.update(callback); }

    public ExpandableGroup getGroup()   { return m_expandToggle; }
    public ApiElementType  getElement() { return m_element;      }
    public Collection<ExpandableGroup> getChilds() { return m_childs.values(); }

    @Override
    public void onAddChild(ApiChildType item) {
        AdapterChildType adapter = instanciateChildAdapter(item);
        if(adapter != null) {
            ExpandableGroup group = new ExpandableGroup(adapter);
            m_childs.put(item, group);
            m_context.runOnUiThread(() -> m_expandToggle.add(group));

            notifyChanged();
        }
    }

    @Override
    public void setExpandableGroup(@NonNull ExpandableGroup onToggleListener) {
        m_expandToggle = onToggleListener;
        // unchecked cast since its a collection
        for(Object child : m_element.getChilds()) onAddChild((ApiChildType)child);
    }

    @Override
    public void onRemoveChild(ApiChildType item) {
        m_context.runOnUiThread(() -> m_expandToggle.remove(m_childs.remove(item)));
        notifyChanged();
    }

    @Override
    public void onUpdate(ApiElement item) { notifyChanged(); }
}
