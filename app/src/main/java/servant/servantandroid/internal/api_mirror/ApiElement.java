package servant.servantandroid.internal.api_mirror;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import servant.servantandroid.internal.ApiService;
import servant.servantandroid.internal.Logger;

public abstract class ApiElement<ChildType extends ApiElement> {

    // empty in case of root element
    String m_fullname = "";
    String m_name     = "";
    String m_id       = "";

    ApiService m_api;

    // map for faster/easier access. String is the element id
    private Map<String, ChildType> m_childs = new HashMap<>();
    private transient List<ApiListener<ChildType>> m_observers;

    // forced lazy initialization to make this serialization safe
    private List<ApiListener<ChildType>> getObservers() {
        if(m_observers == null) m_observers = new ArrayList<>();
        return m_observers;
    }

    ApiElement(ApiService service) { m_api = service; }

    /**
     * create a new api element by parsing the json object
     * @param object the json object representing this element
     */
    public ApiElement(JSONObject object, ApiService service) {
        m_api = service;

        try { updateValues(object); }
        catch (JSONException ex) {
            Logger.getInstance().log(
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

    public void update() {
        m_api.getRequest(ModuleHandler.API_ENDPOINT, m_fullname, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try { updateValues(response); }
                catch (JSONException ex) {
                    Logger.getInstance().log(
                        Logger.Type.ERROR,
                        String.format(
                            "Malformed data received while trying to update an api chain @ %s " +
                            "exception is: %s \n" +
                            "This might indicate that the server is either not running the latest version of servant or " +
                            "is not running servant at all on that port.",
                            m_fullname,
                            response.toString(),
                            ex.toString()
                        ),
                        m_fullname, ex
                    );
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Logger.getInstance().logError(
                    "Failed to update api element '" + m_fullname + "' " +
                    "because the remote server issued an error response in the form of a JSON object. " +
                    "Error code: " + statusCode,
                    this, throwable
                );
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Logger.getInstance().logError(
                    "Failed to update api element '" + m_fullname + "' " +
                    "because the remote server issued an error response in the form of a JSON array. " +
                    "Error code: " + statusCode,
                    this, throwable
                );
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Logger.getInstance().logError(
                    "Failed to update api element '" + m_fullname + "' " +
                    "because the remote server issued an error response in the form of a string. " +
                    "This indicates that servant might not be installed on the remote server. " +
                    "Error code: " + statusCode,
                    this, throwable
                );
            }
        });
    }

    public Collection<ChildType> getChilds() { return m_childs.values(); }
    public ChildType getChildById(String id) { return m_childs.get(id); }

    public ChildType getChildByName(String name) {
        for (ChildType elem : m_childs.values()) {
            if(elem.m_name.equals(name)) return elem;
        }

        return null;
    }

    // this will get called by the public update method
    public void updateValues(JSONObject data) throws JSONException {
        m_fullname = data.getString("fullname");
        m_name     = data.getString("name");
        m_id       = data.getString("id");
    }

    public void addListener(ApiListener listener)    { getObservers().add(listener);    }
    public void removeListener(ApiListener listener) { getObservers().remove(listener); }

    protected void addChild(String id, ChildType child) {
        notifyAdd(child);
        m_childs.put(id, child);
    }

    public void removeChild(String id) {
        notifyRemove(m_childs.remove(id));
    }

    private void notifyAdd(ChildType item) {
        for(ApiListener<ChildType> observer : getObservers()) observer.onAddChild(item);
    }

    private void notifyRemove(ChildType item) {
        for(ApiListener<ChildType> observer : getObservers()) observer.onRemoveChild(item);
    }

    protected final void notifyUpdate() {
        for(ApiListener observer : getObservers()) observer.onUpdate(this);
    }

    public String getFullname() { return m_fullname; }
    public String getName()     { return m_name; }
    public String getId()       { return m_id; }
}
