package servant.servantandroid.internal.api_mirror;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import servant.servantandroid.internal.ApiService;
import servant.servantandroid.internal.Logger;
import servant.servantandroid.internal.api_mirror.parameters.BaseParameter;
import servant.servantandroid.internal.api_mirror.parameters.ParameterFactory;

public class Capability extends ApiElement<BaseParameter> {

    Capability(JSONObject obj, ApiService service) { super(obj, service); }

    public void execute(Consumer<Result> callback) {
        try {
            JSONArray params = new JSONArray();
            for(BaseParameter param : getChilds())
                params.put(param.serialize());

            m_api.postRequest(ModuleHandler.API_ENDPOINT, m_fullname, params.toString(), new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.accept(null);
                    Logger.getInstance().logError(
                        "Failed to execute capability '" + m_fullname + "' " +
                        "because the remote server did not respond ",
                        this, e
                    );
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    ResponseBody body = response.body();
                    Result result = null;
                    try {
                        // maybe i should replace the default JSON lib
                        // with something stream based to make this more efficient
                        // also the api sucks pretty bad overall so i might actually do that
                        result = new Result(new JSONObject(body.string()));
                    } catch (JSONException e) {
                        Logger.getInstance().logError(
                            "Capability " + m_fullname + " return malformed result data",
                            this, e
                        );
                    }
                    finally {
                        body.close();
                        callback.accept(result);
                    }
                }
            });
        } catch (JSONException e) {
            Logger.getInstance().logError(
                "a parameter of capabilty " + m_fullname + " failed to serialize for execution",
                this, e
            );
        }
    }

    @Override
    public void updateValues(JSONObject data) throws JSONException {
        super.updateValues(data);
        notifyUpdate();
    }

    @Override
    protected BaseParameter instanciateChild(JSONObject json) throws JSONException {
        return ParameterFactory.getInstance().constructParameter(
            json.getString("type"), json, m_api
        );
    }
}
