package servant.servantandroid.viewmodel;

import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.ExpandableItem;
import com.xwray.groupie.databinding.BindableItem;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import servant.servantandroid.internal.api_mirror.ApiElement;
import servant.servantandroid.internal.api_mirror.ApiListener;

import java.util.Collection;
import java.util.HashMap;

public abstract class ApiAdapter<
    BindingType      extends ViewDataBinding,
    ApiElementType   extends ApiElement,
    ApiChildType     extends ApiElementType,
    AdapterChildType extends ApiAdapter>

    extends BindableItem<BindingType>
    implements ApiListener<ApiChildType>, ExpandableItem {

    ExpandableGroup m_expandToggle;
    ComponentActivity m_context;
    // the thing were holding
    ApiElementType m_element;
    // link api element to adapter
    HashMap<ApiChildType, ExpandableGroup> m_childs;

    ApiAdapter(ComponentActivity ctx, ApiElementType element) {
        m_childs  = new HashMap<>();
        m_context = ctx;
        m_element = element;

        element.addListener(this);
    }

    protected abstract AdapterChildType instanciateChildAdapter(ApiChildType child);
    ExpandableGroup getGroup() { return m_expandToggle; }
    Collection<ExpandableGroup> getChilds() { return m_childs.values(); }

    @Override
    public void onAddChild(ApiChildType item) {
        ExpandableGroup group = new ExpandableGroup(instanciateChildAdapter(item));
        m_childs.put(item, group);
        m_context.runOnUiThread(() -> m_expandToggle.add(group));

        notifyChanged();
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
