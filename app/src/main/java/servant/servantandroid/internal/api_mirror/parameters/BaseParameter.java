package servant.servantandroid.internal.api_mirror.parameters;

import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;
import servant.servantandroid.internal.api_mirror.ApiElement;

public abstract class BaseParameter extends ApiElement {
    private static ParameterFactory factory;

    // lazy initialization
    public static ParameterFactory getRegistry() {
        if(factory == null) factory = new ParameterFactory();
        return factory;
    }

    BaseParameter(JSONObject object, ApiService service) { super(object, service); }
}
