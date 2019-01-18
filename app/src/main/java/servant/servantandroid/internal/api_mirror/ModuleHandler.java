package servant.servantandroid.internal.api_mirror;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import servant.servantandroid.internal.ApiService;

public class ModuleHandler extends ApiElement<Module> {
    static final String API_ENDPOINT = "modules";

    public ModuleHandler(ApiService service) { super(service); update(); }

    @Override
    public void updateValues(JSONObject data) throws JSONException {
        super.updateValues(data);
        notifyUpdate();
    }

    @Override
    Module instanciateChild(JSONObject json) {
        return new Module(json, m_api);
    }
}
