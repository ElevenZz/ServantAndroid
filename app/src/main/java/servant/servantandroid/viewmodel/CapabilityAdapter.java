package servant.servantandroid.viewmodel;

import com.xwray.groupie.Section;

import androidx.activity.ComponentActivity;
import servant.servantandroid.internal.api_mirror.Capability;

public class CapabilityAdapter extends Section {

    private Capability m_capability;
    private ComponentActivity m_context;

    CapabilityAdapter(ComponentActivity ctx, Capability capability) {
        m_context = ctx;
        m_capability = capability;


    }
}
