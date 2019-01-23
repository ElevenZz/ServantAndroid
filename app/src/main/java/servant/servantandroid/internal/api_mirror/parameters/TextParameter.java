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

    private String m_value = "";

    @Override
    public JSONObject serialize() throws JSONException {
        return new JSONObject().put("value", m_value);
    }

    @Override
    public void updateValues(JSONObject data) throws JSONException {
        super.updateValues(data);
        notifyUpdate();
    }

    public String getValue()           { return m_value;  }
    public void setValue(String value) { m_value = value; }
}
