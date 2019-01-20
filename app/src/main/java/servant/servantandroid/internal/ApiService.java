package servant.servantandroid.internal;

import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiService {

    private static final MediaType JSON  = MediaType.get("application/json; charset=utf-8");
    private URL m_remoteHost;

    ApiService(String remoteHost) throws MalformedURLException {
        m_remoteHost = new URL(remoteHost);
    }

    public void getRequest(String apiEndpoint, String fullname, Callback callback) {
        // string interpolation would be cool here but eh
        String absoluteUrl = String.format("%s/%s/%s", m_remoteHost, apiEndpoint, fullname);
        Request req = new Request.Builder()
            .url(absoluteUrl)
            .build();

        OkSingleton.getInstance().newCall(req).enqueue(callback);
    }

    public void postRequest(String apiEndpoint, String fullname, String data, Callback callback) {
        // string interpolation would be cool here but eh
        String absoluteUrl = String.format("%s/%s/%s", m_remoteHost, apiEndpoint, fullname);

        RequestBody body = RequestBody.create(JSON, data);
        Request request = new Request.Builder()
                .url(absoluteUrl)
                .post(body)
                .build();

        OkSingleton.getInstance().newCall(request).enqueue(callback);
    }

    public String getRemoteHost() { return m_remoteHost.getHost(); }
}

// https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
class OkSingleton extends OkHttpClient {
    private static class LazyHolder {
        private static final OkSingleton instance = new OkSingleton();
    }

    public static OkSingleton getInstance() {
        return LazyHolder.instance;
    }
}