package servant.servantandroid.internal.api_mirror;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;

public class Module extends ApiElement<Category> {
    private String m_version;
    private String m_author;

    Module(JSONObject obj, ApiService service) { super(obj, service); }

    @Override
    public void updateValues(JSONObject data) throws JSONException {
        super.updateValues(data);

        m_author  = data.getString("author");
        m_version = data.getString("version");

        // lol androids JSONArray does not implement the iterable interface
        // what a shame
        JSONArray categories = data.getJSONArray("categories");
        for(int c = 0; c < categories.length(); c++ /*pun intended*/) {
            JSONObject json_category = categories.getJSONObject(c);
            String id = json_category.getString("id");

            Category category = getChildById(id);

            // module already exists we only need to update it :3
            if (category != null) category.updateValues(json_category);
                // module doesn't exist lets create it
            else addChild(id, new Category(json_category, m_api));
        }

        notifyUpdate();
    }

    @Override
    public String toString()   { return m_id;      }
    public String getVersion() { return m_version; }
}
