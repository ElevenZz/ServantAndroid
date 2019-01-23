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

/**
 * the base class for all api adapters.
 * most of the logic is centralized here for more maintainability
 * @param <BindingType> the layout binding type
 * @param <ApiElementType> actual api element this is the adapter for
 * @param <ApiChildType> the child type of the api element
 * @param <AdapterChildType> the child type of the adapter
 */
public abstract class ApiAdapter<
    BindingType      extends ViewDataBinding,
    ApiElementType   extends ApiElement,
    ApiChildType     extends ApiElement,
    AdapterChildType extends ApiAdapter>

    extends BindableItem<BindingType>
    implements ApiListener<ApiChildType>, ExpandableItem {

    // the actual UI element
    ExpandableGroup m_expandToggle;
    // the activity
    FragmentActivity m_context;
    // the thing were holding
    ApiElementType m_element;
    // map api element to adapter
    HashMap<ApiChildType, ExpandableGroup> m_childs;

    ApiAdapter(FragmentActivity ctx, ApiElementType element) {
        m_childs  = new HashMap<>();
        m_context = ctx;
        m_element = element;

        // we observe any changes in the api element
        element.addListener(this);
    }

    /**
     * in case the child needs special constructor params
     * @param child the underlying api element
     * @return an api adapter for the provided api element
     */
    protected abstract AdapterChildType instanciateChildAdapter(ApiChildType child);

    /**
     * binds the expand icon and sets adds the animations
     * @param icon icon view to bind
     */
    void bindExpandIcon(AppCompatImageView icon) {
        initializeIcon(icon);
        icon.setOnClickListener((view) -> {
            m_expandToggle.onToggleExpanded();
            initializeIcon(icon);
            ((Animatable)icon.getDrawable()).start();
        });
    }

    /**
     * set the icon to visible and sets the corresponding resource on it
     * @param icon the icon view
     */
    private void initializeIcon(AppCompatImageView icon) {
        icon.setVisibility(View.VISIBLE);
        icon.setImageResource(
            m_expandToggle.isExpanded() ?
                R.drawable.collapse_animated : R.drawable.expand_animated
        );
    }

    /**
     * a wrapper for the api elements update method with a UI callback
     * @param callback callback used to interact with the ui once the update is done
     */
    public void update(Runnable callback) { m_element.update(callback); }

    public ExpandableGroup getGroup()   { return m_expandToggle; }
    public ApiElementType  getElement() { return m_element;      }
    public Collection<ExpandableGroup> getChilds() { return m_childs.values(); }

    /**
     * get automatically called after the constructor
     * when this is instantiated with 'new ExpandableGroup(adapter)'
     * @param onToggleListener the expandable group instance
     */
    @Override
    public void setExpandableGroup(@NonNull ExpandableGroup onToggleListener) {
        m_expandToggle = onToggleListener;
        // unchecked cast since we can't access the type parameter of ApiElement here
        for(Object child : m_element.getChilds()) onAddChild((ApiChildType)child);
    }

    /**
     * implemented by the api elements listener
     * called when a new child is added to the api element
     * @param item the item that was added
     */
    @Override public void onAddChild(ApiChildType item) {
        AdapterChildType adapter = instanciateChildAdapter(item);
        if(adapter != null) {
            ExpandableGroup group = new ExpandableGroup(adapter);
            m_childs.put(item, group);
            m_context.runOnUiThread(() -> m_expandToggle.add(group));

            notifyChanged();
        }
    }

    /**
     * implemented by the api elements listener
     * called when a child is removed from the api element
     * @param item the item that was removed
     */
    @Override public void onRemoveChild(ApiChildType item) {
        // can't be null
        m_context.runOnUiThread(() -> m_expandToggle.remove(m_childs.remove(item)));
        notifyChanged();
    }

    /**
     * implemented by the api elements listener
     * called when the attributes of a child have changed
     * notifies the view of an update and update the local database cache
     * @param item the item that was changed
     */
    @Override public void onUpdate(ApiElement item) { notifyChanged(); }
}
