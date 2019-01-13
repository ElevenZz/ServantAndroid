package servant.servantandroid.internal.api_mirror.parameters;

import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;
import servant.servantandroid.internal.api_mirror.ApiElement;

public abstract class BaseParameter extends ApiElement {
    private static ParameterRegistry registry = new ParameterRegistry();

    public static ParameterRegistry getRegistry() { return  registry; }

    /**
     * Constructor passthrough
     */
    BaseParameter(JSONObject object, ApiService service) { super(object, service); }
}
