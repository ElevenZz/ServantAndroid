package servant.servantandroid.internal.api_mirror;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;
import servant.servantandroid.internal.ApiService;
import servant.servantandroid.internal.Logger;

/**
 * the base type of all api elements
 * @param <ChildType> the type of the child elements
 */
public abstract class ApiElement<ChildType extends ApiElement> {
    // empty in case of root element
    protected String m_fullname;
    protected String m_name;
    protected String m_id;

    ApiService m_api;

    // map for faster/easier access. String is the element id
    private Map<String, ChildType> m_childs = new LinkedHashMap<>();
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

    /**
     * overload acting as optional parameter
     */
    public void update() { update(null); }
    public void update(Runnable updateCallback) {
        m_api.getRequest(ModuleHandler.API_ENDPOINT, m_fullname, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (updateCallback != null) updateCallback.run();
                Logger.getInstance().logError(
                    "Failed to update api element '" + m_fullname + "' " +
                    "because the remote server did not respond ",
                    this, e
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (updateCallback != null) updateCallback.run();
                try {
                    // TODO: do more safety checks
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    updateValues(jsonResponse);
                } catch (JSONException ex) {
                    Logger.getInstance().logError(
                        String.format(
                            "Malformed data received while trying to update an api chain @ %s " +
                            "exception is: %s \n" +
                            "This might indicate that the server is either not running the latest version of servant or " +
                            "is not running servant at all on that port.",
                            m_fullname,
                            ex.toString()
                        ),
                        m_fullname, ex
                    );
                }
            }
        });
    }

    public Collection<ChildType> getChilds() { return m_childs.values(); }
    public ChildType getChildById(String id) { return m_childs.get(id);  }

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

        // copy the id set from the map
        Set<String> currentIds = new HashSet<>(m_childs.keySet());
        JSONArray childs = data.optJSONArray("childs");
        if(childs != null) {
            // seems like androids JSONArray does not implement the iterable interface
            // what a shame
            for(int c = 0; c < childs.length(); c++ /* pun intended */) {
                JSONObject jsonChild = childs.getJSONObject(c);
                String     childId   = jsonChild.getString("id");

                ChildType child = getChildById(childId);
                if(child != null) child.updateValues(jsonChild);
                else child = addChild(childId, instanciateChild(jsonChild));

                // remove the child we interacted with
                // we basically remove all child's that exist in the json
                currentIds.remove(child.getId());
            }
        }

        // now the remaining child's are the ones not present in the json but present locally
        // we remove all of them since they were removed on the server
        removeAll(currentIds);
    }

    /**
     * instantiate new child. used to give more control over initialization
     * @param json json representing element
     * @return the new element
     * @throws JSONException if json is invalid
     */
    protected abstract ChildType instanciateChild(JSONObject json) throws JSONException;

    /**
     * add observer to listen for changes in this instance
     * @param listener object implementing ApiListener interface
     */
    public void addListener(ApiListener listener)    { getObservers().add(listener);    }
    public void removeListener(ApiListener listener) { getObservers().remove(listener); }

    // these methods need to be synchronized since they get called by the async http handler,
    // modify the map and i im not a big fan of race conditions
    synchronized ChildType addChild(String id, ChildType child) {
        notifyAdd(child);
        m_childs.put(id, child);
        return child;
    }

    /**
     * convenience method to remove all childs
     * @param ids list of ids to remove from the map
     */
    synchronized void removeAll(Collection<String> ids) {
        for (String id : ids) notifyRemove(m_childs.remove(id));
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
