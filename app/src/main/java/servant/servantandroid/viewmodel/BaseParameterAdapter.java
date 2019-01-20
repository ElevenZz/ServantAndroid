package servant.servantandroid.viewmodel;

import java.util.HashMap;

import androidx.activity.ComponentActivity;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import servant.servantandroid.databinding.HeaderLayoutBinding;
import servant.servantandroid.internal.api_mirror.ApiElement;
import servant.servantandroid.internal.api_mirror.parameters.BaseParameter;
import servant.servantandroid.internal.api_mirror.parameters.TextParameter;

// passing myself as child type since this is the last element. maybe a bit dirty
public abstract class BaseParameterAdapter<LayoutBinding extends ViewDataBinding, ParameterType extends ApiElement>
    extends ApiAdapter<LayoutBinding, ParameterType, BaseParameter, BaseParameterAdapter> {

    @FunctionalInterface
    interface ParameterAdapterConstructor {
        BaseParameterAdapter apply(FragmentActivity ctx, BaseParameter param);
    }

    // java doesn't support type switches and if statements are not reasonable / expandable in this case
    private static HashMap<Class<? extends BaseParameter>, ParameterAdapterConstructor> paramMapping;
    public static ParameterAdapterConstructor getParamtypeConstructor(Class<? extends BaseParameter> paramType) {
        if(paramMapping == null) { // initializing map here to prevent a class loading deadlock
            paramMapping = new HashMap<>();

            // sadly can't do TextParameterAdapter::new since the ctor param type differs
            paramMapping.put(
                TextParameter.class,
                (ctx, param) -> new TextParameterAdapter(ctx, (TextParameter) param)
            );
        }
        return paramMapping.get(paramType);
    }

    BaseParameterAdapter(FragmentActivity ctx, ParameterType element) {
        super(ctx, element);
    }
}
