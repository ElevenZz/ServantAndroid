package servant.servantandroid.internal.ModuleTree;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import servant.servantandroid.internal.ApiService;
import servant.servantandroid.internal.Logger;

public abstract class ApiElement<ChildType> {
    private String m_fullname;
    private String m_name;
    private String m_id;

    ApiService m_api;

    // map for faster/easier access. String is the element id
    Map<String, ChildType> m_childs = new HashMap<>();

    ApiElement(ApiService service) { m_api = service; }

    /**
     * create a new api element by parsing the json object
     * @param object the json object representing this element
     */
    public ApiElement(JSONObject object, ApiService service) {
        m_api = service;

        try { UpdateValues(object); }
        catch (JSONException ex) {
            Logger.getInstance().Log(
                Logger.Type.ERROR,
                String.format(
                    "Malformed data received while trying to create an api chain entry. " +
                    "malformed data is: %s \n" +
                    "exception is: %s \n" +
                    "if you have the latest version of the servant app please report this at the github repository",
                    object.toString(),
                    ex.toString()
                ),
                m_fullname
            );
        }
    }

    void Update() {
        m_api.GetRequest(ModuleHandler.API_ENDPOINT, m_fullname, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try { UpdateValues(response); }
                catch (JSONException ex) {
                    Logger.getInstance().Log(
                        Logger.Type.ERROR,
                        String.format(
                            "Malformed data received while trying to update an api chain @ %s " +
                            "malformed data is: %s \n" +
                            "exception is: %s \n" +
                            "if you have the latest version of the servant app please report this at the github repository",
                            m_fullname,
                            response.toString(),
                            ex.toString()
                        ),
                        m_fullname
                    );
                }
            }
        });
    }

    // this will get called by the public update method
    public abstract void UpdateValues(JSONObject data) throws JSONException;
}
