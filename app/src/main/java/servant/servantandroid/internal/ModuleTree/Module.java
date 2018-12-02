package servant.servantandroid.internal.ModuleTree;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;

public class Module extends ApiElement<Category> {
    private String m_version;
    private String m_author;

    Module(JSONObject obj, ApiService service) { super(obj, service); }

    @Override
    public void UpdateValues(JSONObject data) throws JSONException {
        // lol androids JSONArray does not implement the iterable interface
        // what a shame

        JSONArray categories = data.getJSONArray("categories");
        for(int c = 0; c < categories.length(); c++ /*pun intended*/) {
            JSONObject json_category = categories.getJSONObject(c);
            String id = json_category.getString("id");

            Category category = m_childs.get(id);

            // module already exists we only need to update it :3
            if (category != null) category.UpdateValues(json_category);
                // module doesn't exist lets create it
            else m_childs.put(id, new Category(json_category, m_api));
        }
    }
}
