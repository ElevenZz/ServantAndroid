package servant.servantandroid.internal.api_mirror;

// implement this to subscribe to changes on an api element
public interface ApiListener<Child extends ApiElement> {
    void onAddChild(Child item);
    void onRemoveChild(Child item);
    void onUpdate(ApiElement item);
}
