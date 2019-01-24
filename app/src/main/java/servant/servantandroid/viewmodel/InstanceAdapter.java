package servant.servantandroid.viewmodel;

import android.graphics.drawable.Animatable;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import servant.servantandroid.R;
import servant.servantandroid.databinding.InstanceLayoutBinding;
import servant.servantandroid.internal.ServantInstance;
import servant.servantandroid.internal.api_mirror.Module;
import servant.servantandroid.internal.api_mirror.ModuleHandler;
import servant.servantandroid.viewmodel.persistence.DatabaseService;

/**
 * adapter for the servant instance/the module handler
 */
public class InstanceAdapter extends ApiAdapter<InstanceLayoutBinding, ModuleHandler, Module, ModuleAdapter> {
    /**
     * observable livedata containing the selected module
     */
    private MutableLiveData<ModuleAdapter> m_selectedModule;

    /**
     * the underlying instance
     */
    private ServantInstance m_instance;

    InstanceAdapter(FragmentActivity ctx, ServantInstance instance, MutableLiveData<ModuleAdapter> selected) {
        super(ctx, instance.getModuleHandler());

        m_selectedModule = selected;
        m_instance = instance;
    }

    /**
     * bind the instance title, description and refresh icon to the layout
     * @param viewBinding instance layout binding
     * @param position irrelevant for us
     */
    @Override public void bind(@NonNull InstanceLayoutBinding viewBinding, int position) {
        bindExpandIcon(viewBinding.iconExpand);
        viewBinding.title.setText(m_instance.getName());
        viewBinding.subtitle.setText(m_instance.toString());

        viewBinding.iconRefresh.setImageResource(R.drawable.refresh_animated);
        viewBinding.iconRefresh.setOnClickListener((view) -> {
            ((Animatable)viewBinding.iconRefresh.getDrawable()).start();
            m_element.update(() ->
                ((Animatable)viewBinding.iconRefresh.getDrawable()).stop()
            );
        });
    }

    /**
     * declare that this view can be swiped to the right
     * @return the supported swipe directions
     */
    @Override public int getSwipeDirs() { return ItemTouchHelper.RIGHT;    }

    /**
     * @return instance layout
     */
    @Override public int getLayout()    { return R.layout.instance_layout; }

    public String getName() { return m_instance.getName(); }
    public ServantInstance getInstance() { return m_instance; }

    @Override protected ModuleAdapter instanciateChildAdapter(Module child) {
        return new ModuleAdapter(m_context, child, m_selectedModule);
    }

    /**
     * overrides notify changed to update the local cache
     */
    @Override public void notifyChanged() {
        super.notifyChanged();
        DatabaseService.getInstance().updateServantInstance(m_instance);
    }
}
