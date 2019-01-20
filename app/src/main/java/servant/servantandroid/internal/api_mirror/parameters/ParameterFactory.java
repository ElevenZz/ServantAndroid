package servant.servantandroid.internal.api_mirror.parameters;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import servant.servantandroid.internal.ApiService;
import servant.servantandroid.internal.api_mirror.Capability;

// TODO: do this in a less csharpy way
public class ParameterFactory {
    // https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
    private static class LazyHolder {
        private static final ParameterFactory instance = new ParameterFactory();
    }

    public static ParameterFactory getInstance() {
        return LazyHolder.instance;
    }

    // Nahem says: "Don't do JavaScript Kids"
    private Map<String, Class<? extends BaseParameter>> m_parameterTypes = new HashMap<>();

    public Class<? extends BaseParameter> resolve(String parameterName) {
        return m_parameterTypes.get(parameterName);
    }

    public BaseParameter constructParameter(String parameterName, JSONObject parameter, ApiService service)
        throws IllegalArgumentException {

        Class<? extends BaseParameter> parameterClass = resolve(parameterName);
        BaseParameter instance;

        if(parameterClass == null) { throw new IllegalArgumentException(
            "parameter of type: " + parameterName + " does not exist in this version of the application. " +
            "if the problem persists after updating the app, please report this to the servant authors"
        );}

        // create new instance of the specific type
        try { instance = parameterClass.getConstructor(JSONObject.class, ApiService.class).newInstance(parameter, service); }
        catch (NoSuchMethodException|IllegalAccessException|InvocationTargetException|InstantiationException ex) {
            // rethrow with more specific information
            throw new IllegalArgumentException(
                "parameter of type " + parameterName + " failed to deserialize. " +
                "if the problem persists with the newest version of the application please report this error to the servant authors. " +
                "Details: " + ex.toString()
            );
        }

        return instance;
    }

    ParameterFactory() {
        // register parameters
        m_parameterTypes.put("text", TextParameter.class);
    }
}
