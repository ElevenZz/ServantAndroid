package servant.servantandroid.internal.api_mirror;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import servant.servantandroid.internal.ApiService;
import servant.servantandroid.internal.api_mirror.parameters.BaseParameter;

public class Capability extends ApiElement<BaseParameter> {

    Capability(JSONObject obj, ApiService service) { super(obj, service); }

    @Override
    public void updateValues(JSONObject data) throws JSONException {
        super.updateValues(data);

        // lol androids JSONArray does not implement the iterable interface
        // what a shame

        JSONArray parameters = data.getJSONArray("parameters");
        for(int c = 0; c < parameters.length(); c++ /*pun intended*/) {
            JSONObject json_parameter = parameters.getJSONObject(c);
            String id = json_parameter.getString("id");

            BaseParameter parameter = m_childs.get(id);

            // module already exists we only need to update it :3
            if (parameter != null) parameter.updateValues(json_parameter);
                // module doesn't exist lets create it
            else m_childs.put(id,
                BaseParameter.getRegistry().constructParameter(id, json_parameter, m_api));
        }
    }
}
