package servant.servantandroid.internal.ModuleTree.Parameters;

import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;
import servant.servantandroid.internal.ModuleTree.ApiElement;

public abstract class BaseParameter extends ApiElement {
    private static ParameterRegistry registry = new ParameterRegistry();

    public static ParameterRegistry GetRegistry() { return  registry; }

    /**
     * Constructor passthrough
     */
    BaseParameter(JSONObject object, ApiService service) { super(object, service); }
}
