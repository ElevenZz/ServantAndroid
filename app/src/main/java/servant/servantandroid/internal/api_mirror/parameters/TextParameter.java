package servant.servantandroid.internal.api_mirror.parameters;

import org.json.JSONException;
import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;

public class TextParameter extends BaseParameter {
    public TextParameter(JSONObject parameter, ApiService service) { super(parameter, service); }

    @Override
    public void updateValues(JSONObject data) throws JSONException {
        super.updateValues(data);
        notifyUpdate();
    }
}
