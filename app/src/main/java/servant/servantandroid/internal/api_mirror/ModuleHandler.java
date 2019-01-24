package servant.servantandroid.internal.api_mirror;

import org.json.JSONException;
import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;

/**
 * provides an api endpoint since different handlers could be implemented in the future
 * sets the fullname, name and id to an empty string
 * since the update request should only contain the endpoint
 */
public class ModuleHandler extends ApiElement<Module> {
    /**
     * the api endpoint to be requested by the base update method
     */
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
