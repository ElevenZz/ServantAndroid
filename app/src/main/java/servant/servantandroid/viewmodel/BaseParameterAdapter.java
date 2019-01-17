package servant.servantandroid.viewmodel;

import java.util.HashMap;

import androidx.activity.ComponentActivity;
import servant.servantandroid.databinding.HeaderLayoutBinding;
import servant.servantandroid.internal.api_mirror.parameters.BaseParameter;
import servant.servantandroid.internal.api_mirror.parameters.TextParameter;

// passing myself as child type since this is the last element. maybe a bit dirty
public abstract class BaseParameterAdapter extends ApiAdapter<HeaderLayoutBinding, BaseParameter, BaseParameter, BaseParameterAdapter> {
    @FunctionalInterface
    interface ParameterAdapterConstructor {
        BaseParameterAdapter apply(ComponentActivity ctx, BaseParameter param);
    }

    // java doesn't support type switches and if statements are not reasonable / expandable
    private static HashMap<Class<? extends BaseParameter>, ParameterAdapterConstructor> paramMapping;
    public static ParameterAdapterConstructor getParamtypeHandler(Class<? extends BaseParameter> paramType) {
        return paramMapping.get(paramType);
    }

    static {
        paramMapping = new HashMap<>();
        paramMapping.put(TextParameter.class, TextParameterAdapter::new);
    }

    BaseParameterAdapter(ComponentActivity ctx, BaseParameter element) {
        super(ctx, element);
    }
}
