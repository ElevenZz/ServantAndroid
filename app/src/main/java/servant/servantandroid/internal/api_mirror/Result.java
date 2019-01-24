package servant.servantandroid.internal.api_mirror;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * representing the result of a capability execution
 */
public class Result {
    public enum Type { SUCCESS, ERROR }

    /**
     * message the capability returned
     */
    private String m_message;

    /**
     * type of the result
     * see Result.Type for possible types
     */
    private Type m_type;

    /**
     * construct result instance from json response
     * @param json JSON representing this object
     * @throws JSONException if provided json is malformed
     */
    Result(JSONObject json) throws JSONException {
        m_message = json.getString("message");
        m_type    = Type.values()[json.getInt("type")];
    }

    public String getMessage()  { return m_message; }
    public Type getType() { return m_type;    }
}
