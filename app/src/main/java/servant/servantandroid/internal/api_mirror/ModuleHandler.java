package servant.servantandroid.internal.api_mirror;

import org.json.JSONException;
import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;
import servant.servantandroid.internal.ServantInstance;

public class ModuleHandler extends ApiElement<Module> {
    static final String API_ENDPOINT = "modules";

    public ModuleHandler(ApiService service) {
        super(service);

        // we don't have that stuff since we are the root element
        // i will prolly change this in the future tho
        m_fullname = "";
        m_name = "";
        m_id = "";

        update();
    }

    @Override
    public void updateValues(JSONObject data) throws JSONException {
        super.updateValues(data);
        notifyUpdate();
    }

    @Override
    protected Module instanciateChild(JSONObject json) {
        return new Module(json, m_api);
    }
}
