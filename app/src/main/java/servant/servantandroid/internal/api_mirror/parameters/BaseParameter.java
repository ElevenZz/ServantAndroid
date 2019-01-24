package servant.servantandroid.internal.api_mirror.parameters;

import org.json.JSONException;
import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;
import servant.servantandroid.internal.api_mirror.ApiElement;

/**
 * base class for all types of parameters.
 * we set our self as the generic parameter since this is the last element in the api hierarchy
 */
public abstract class BaseParameter extends ApiElement<BaseParameter> {
    /**
     * name of parameter type will be kept for use in error messages and displays
     */
    protected String m_type;

    /**
     * return json object representing this instance
     * @return json representing this instance
     * @throws JSONException if a variable contains an invalid value
     */
    public abstract JSONObject serialize() throws JSONException;

    @Override public void updateValues(JSONObject data) throws JSONException {
        super.updateValues(data);
        m_type = data.getString("type");
        notifyUpdate();
    }

    /**
     * throws an exception since parameters can't have childs
     * @param json json representing the element
     * @return nothing
     * @throws JSONException always
     */
    @Override protected BaseParameter instanciateChild(JSONObject json) throws JSONException {
        throw new JSONException(
            "the parameter " + m_fullname + " has child elements " +
            "which is not allowed for a parameter element"
        );
    }

    BaseParameter(JSONObject object, ApiService service) { super(object, service); }
}
