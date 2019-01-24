package servant.servantandroid.internal.api_mirror;

import org.json.JSONException;
import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;

/**
 * a module element holding a version and an author
 */
public class Module extends ApiElement<Category> {
    private String m_version;
    private String m_author;

    Module(JSONObject obj, ApiService service) { super(obj, service); }

    /**
     * sets the author and version of this instance from the json
     * @param data the json data containing author and name
     * @throws JSONException if the json is malformed
     */
    @Override public void updateValues(JSONObject data) throws JSONException {
        super.updateValues(data);

        m_author  = data.getString("author");
        m_version = data.getString("version");

        notifyUpdate();
    }

    @Override
    protected Category instanciateChild(JSONObject json) {
        return new Category(json, m_api);
    }

    @Override
    public String toString()   { return m_id;      }
    public String getVersion() { return m_version; }
}
