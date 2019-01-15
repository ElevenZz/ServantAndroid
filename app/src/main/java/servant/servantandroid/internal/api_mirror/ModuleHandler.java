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
        // lol androids JSONArray does not implement the iterable interface
        // what a shame

        JSONArray modules = data.getJSONArray("modules");
        for(int c = 0; c < modules.length(); c++ /*pun intended*/) {
            JSONObject json_module = modules.getJSONObject(c);
            String id = json_module.getString("id");

            Module module = getChildById(id);

            // module already exists we only need to update it :3
            if (module != null) module.updateValues(json_module);
            // module doesn't exist lets create it
            else addChild(id, new Module(json_module, m_api));
        }

        notifyUpdate();
    }
}
