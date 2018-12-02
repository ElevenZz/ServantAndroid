package servant.servantandroid.internal.ModuleTree;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import servant.servantandroid.internal.ApiService;

public class ModuleHandler extends ApiElement<Module> {
    static final String API_ENDPOINT = "modules";

    public ModuleHandler(ApiService service) { super(service); Update(); }

    @Override
    public void UpdateValues(JSONObject data) throws JSONException {
        // lol androids JSONArray does not implement the iterable interface
        // what a shame

        JSONArray modules = data.getJSONArray("modules");
        for(int c = 0; c < modules.length(); c++ /*pun intended*/) {
            JSONObject json_module = modules.getJSONObject(c);
            String id = json_module.getString("id");

            Module module = m_childs.get(id);

            // module already exists we only need to update it :3
            if (module != null) module.UpdateValues(json_module);
            // module doesn't exist lets create it
            else m_childs.put(id, new Module(json_module, m_api));
        }
    }
}
