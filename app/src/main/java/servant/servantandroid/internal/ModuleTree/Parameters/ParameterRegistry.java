package servant.servantandroid.internal.ModuleTree.Parameters;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import servant.servantandroid.internal.Logger;

/**
 * Singleton registry for all types of parameters
 */
public class ParameterRegistry {
    // Nahem says: "Don't do JavaScript Kids"
    private Map<String, Class<? extends BaseParameter>> m_parameterTypes = new HashMap<>();

    public BaseParameter ConstructParameter(String parameterName, JSONObject parameter) throws IllegalArgumentException {
        Class<? extends BaseParameter> parameterClass = m_parameterTypes.get(parameterName);
        BaseParameter instance;

        if(parameterClass == null) { throw new IllegalArgumentException(
            "parameter of type: " + parameterName + " does not exist in this version of the application. " +
            "if the problem persists after updating the app, please report this to the servant authors"
        );}

        // create new instance of the specific type
        try { instance = parameterClass.getDeclaredConstructor(parameterClass).newInstance(parameter); }
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


    ParameterRegistry() {
        // register parameters
        m_parameterTypes.put("text", TextParameter.class);
    }
}
