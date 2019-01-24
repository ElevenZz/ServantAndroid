package servant.servantandroid.internal.api_mirror.parameters;

import org.json.JSONException;
import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;
import servant.servantandroid.internal.api_mirror.Capability;

/**
 * a parameter containing only a string value
 */
public class TextParameter extends BaseParameter {
    public TextParameter(JSONObject parameter, ApiService service) { super(parameter, service); }

    /**
     * the value of this parameter
     * it should be set by the view and returned when serialized
     */
    private String m_value = "";

    /**
     * get a JSON representation of this instance
     * @return JSON representation
     * @throws JSONException if value is in an invalid state
     */
    @Override public JSONObject serialize() throws JSONException {
        return new JSONObject().put("value", m_value);
    }

    @Override public void updateValues(JSONObject data) throws JSONException {
        super.updateValues(data);
        notifyUpdate();
    }

    public String getValue()           { return m_value;  }
    public void setValue(String value) { m_value = value; }
}
