package servant.servantandroid.internal.api_mirror.parameters;

import org.json.JSONException;
import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;
import servant.servantandroid.internal.api_mirror.ApiElement;

public abstract class BaseParameter extends ApiElement<BaseParameter> {
    protected String m_type;

    public abstract JSONObject serialize() throws JSONException;

    @Override
    public void updateValues(JSONObject data) throws JSONException {
        super.updateValues(data);
        m_type = data.getString("type");
        notifyUpdate();
    }

    @Override
    protected BaseParameter instanciateChild(JSONObject json) throws JSONException {
        throw new JSONException(
            "the parameter " + m_fullname + " has child elements " +
            "which is not allowed for a parameter element"
        );
    }

    BaseParameter(JSONObject object, ApiService service) { super(object, service); }
}
