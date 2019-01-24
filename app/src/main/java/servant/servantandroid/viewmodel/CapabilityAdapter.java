package servant.servantandroid.viewmodel;

import android.graphics.drawable.Animatable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import servant.servantandroid.R;
import servant.servantandroid.databinding.CapabilityLayoutBinding;
import servant.servantandroid.internal.api_mirror.Capability;
import servant.servantandroid.internal.api_mirror.parameters.BaseParameter;
import servant.servantandroid.view.ResultFragment;

public class CapabilityAdapter
    extends ApiAdapter<CapabilityLayoutBinding, Capability, BaseParameter, BaseParameterAdapter> {

    CapabilityAdapter(FragmentActivity ctx, Capability capability) { super(ctx, capability); }

    @Override
    protected BaseParameterAdapter instanciateChildAdapter(BaseParameter child) {
        return BaseParameterAdapter.getParamtypeConstructor(child.getClass()).apply(m_context, child);
    }

    @Override public int getLayout() { return R.layout.capability_layout; }

    @Override
    public void bind(@NonNull CapabilityLayoutBinding viewBinding, int position) {
        if(! m_childs.isEmpty()) bindExpandIcon(viewBinding.iconExpand);
        else viewBinding.iconExpand.setVisibility(View.INVISIBLE);
        viewBinding.title.setText(m_element.getName());
        viewBinding.iconExecute.setImageResource(R.drawable.execute_animated);
        viewBinding.iconExecute.setOnClickListener((view) -> {
            ((Animatable)viewBinding.iconExecute.getDrawable()).start();
            m_element.execute((res) -> {
                ((Animatable)viewBinding.iconExecute.getDrawable()).stop();
                if(res != null) ResultFragment.showResultFragment(
                    m_context.getSupportFragmentManager(), res, m_element
                );
            });
        });
    }
}
