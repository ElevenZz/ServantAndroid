package servant.servantandroid.internal;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class ApiService {
    // using an async client here as to not stop the gui
    // and allow multiple capabilities to be executed simultaneously
    private static AsyncHttpClient client = new AsyncHttpClient();
    private URL m_remoteHost;

    ApiService(String remoteHost) throws MalformedURLException {
        m_remoteHost = new URL(remoteHost);
    }

    public void getRequest(String apiEndpoint, String fullname, ResponseHandlerInterface handler) {
        // string interpolation would be cool here but eh
        String absoluteUrl = String.format("%s/%s/%s", m_remoteHost, apiEndpoint, fullname);
        client.get(absoluteUrl, handler);
    }

    public void postRequest(String apiEndpoint, String fullname, JSONObject data, ResponseHandlerInterface handler) {
        // string interpolation would be cool here but eh
        String absoluteUrl = String.format("%s/%s/%s", m_remoteHost, apiEndpoint, fullname);

        // put our json data in a RequestParams object and set sum options
        RequestParams params = new RequestParams(data.toString());
        params.setUseJsonStreamer(true);
        params.setContentEncoding(RequestParams.APPLICATION_JSON);

        client.post(absoluteUrl, params, handler);
    }

    public String getRemoteHost() { return m_remoteHost.getHost(); }
}