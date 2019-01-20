package servant.servantandroid.internal.api_mirror;

import org.json.JSONException;
import org.json.JSONObject;

public class Result {
    public enum Type { SUCCESS, ERROR }

    private String m_message;
    private Type m_type;

    Result(JSONObject json) throws JSONException {
        m_message = json.getString("message");
        m_type    = Type.values()[json.getInt("type")];
    }

    public String getMessage()  { return m_message; }
    public Type getType() { return m_type;    }
}
