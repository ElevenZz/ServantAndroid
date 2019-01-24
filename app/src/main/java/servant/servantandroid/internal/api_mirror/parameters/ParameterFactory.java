package servant.servantandroid.internal.api_mirror.parameters;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import servant.servantandroid.internal.ApiService;

/**
 * parameter types get mapped with their type string here.
 * this could be improved by building an annotation processor
 * and letting the annotation provide the type name
 * this is basically the preparation until i have time to do that
 */
public class ParameterFactory {
    /**
     * https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
     */
    private static class LazyHolder {
        private static final ParameterFactory instance = new ParameterFactory();
    }

    public static ParameterFactory getInstance() {
        return LazyHolder.instance;
    }

    // Nahem says: "Don't do JavaScript Kids"
    /**
     * mapping the specific parameter class to a name
     */
    private Map<String, Class<? extends BaseParameter>> m_parameterTypes = new HashMap<>();

    /**
     * get concrete parameter class by type name
     * @param parameterName the name of parameter type
     * @return corresponding parameter
     */
    public Class<? extends BaseParameter> resolve(String parameterName) {
        return m_parameterTypes.get(parameterName);
    }

    /**
     * construct parameter given its type
     * @param parameterName parameter type
     * @param parameter json object representing the parameter
     * @param service api service to pass to the param constructor
     * @return parameter instance
     * @throws IllegalArgumentException if the parameter does not exist
     */
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

    /**
     * initializes the parameter hash map
     */
    private ParameterFactory() {
        // register parameters
        m_parameterTypes.put("text", TextParameter.class);
    }
}
