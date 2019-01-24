package servant.servantandroid.internal.api_mirror;

/**
 * listener for observing changes in the api element
 * a ui adapter should implement this
 * @param <Child> type of the child elements for more type safety
 */
public interface ApiListener<Child extends ApiElement> {
    void onAddChild(Child item);
    void onRemoveChild(Child item);
    void onUpdate(ApiElement item);
}
