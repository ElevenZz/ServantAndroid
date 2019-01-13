package servant.servantandroid.internal.api_mirror;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;

public class Category extends ApiElement<Capability> {

    Category(JSONObject obj, ApiService service) { super(obj, service); }

    @Override
    public void updateValues(JSONObject data) throws JSONException {
        super.updateValues(data);

        // lol androids JSONArray does not implement the iterable interface
        // what a shame

        JSONArray capabilities = data.getJSONArray("capabilities");
        for(int c = 0; c < capabilities.length(); c++ /*pun intended*/) {
            JSONObject json_capability = capabilities.getJSONObject(c);
            String id = json_capability.getString("id");

            Capability capability = m_childs.get(id);

            // module already exists we only need to update it :3
            if (capability != null) capability.updateValues(json_capability);
                // module doesn't exist lets create it
            else m_childs.put(id, new Capability(json_capability, m_api));
        }
    }
}
