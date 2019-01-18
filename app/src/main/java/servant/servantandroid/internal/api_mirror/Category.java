package servant.servantandroid.internal.api_mirror;

import org.json.JSONException;
import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;

public class Category extends ApiElement<Capability> {

    Category(JSONObject obj, ApiService service) { super(obj, service); }

    @Override
    public void updateValues(JSONObject data) throws JSONException {
        super.updateValues(data);
        notifyUpdate();
    }

    @Override
    Capability instanciateChild(JSONObject json) {
        return new Capability(json, m_api);
    }
}
