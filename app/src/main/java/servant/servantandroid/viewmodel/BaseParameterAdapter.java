package servant.servantandroid.viewmodel;

import java.util.HashMap;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import servant.servantandroid.internal.api_mirror.ApiElement;
import servant.servantandroid.internal.api_mirror.parameters.BaseParameter;
import servant.servantandroid.internal.api_mirror.parameters.TextParameter;

/**
 * the base class for parameter adapters
 * passing this class as the child type since this is the last element in the tree structure
 * @param <LayoutBinding> the binding to the parameters layout
 * @param <ParameterType> the models parameter type this adapter wraps
 */
public abstract class BaseParameterAdapter<LayoutBinding extends ViewDataBinding, ParameterType extends ApiElement>
    extends ApiAdapter<LayoutBinding, ParameterType, BaseParameter, BaseParameterAdapter> {

    /**
     * function interface representing the constructor of a parameter adapter
     */
    @FunctionalInterface interface ParameterAdapterConstructor {
        BaseParameterAdapter apply(FragmentActivity ctx, BaseParameter param);
    }

    // java doesn't support type switches and if statements are not reasonable / expandable in this case
    /**
     * maps the constructor of a parameter adapter to the type of an api parameter
     * this allows us to automatically create an instance of the correct parameter adapter in the capability class
     */
    private static HashMap<Class<? extends BaseParameter>, ParameterAdapterConstructor> paramMapping;

    /**
     * returns the constructor of a parameter adapter given the class of the underlying parameter
     * @param paramType the type of the underlying parameter
     * @return constructor of the matching parameter adapter
     */
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
