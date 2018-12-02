package servant.servantandroid.internal.ModuleTree.Parameters;

import org.json.JSONException;
import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;

class TextParameter extends BaseParameter {
    TextParameter(JSONObject parameter, ApiService service) { super(parameter, service); }

    @Override
    public void UpdateValues(JSONObject data) throws JSONException {

    }
}
