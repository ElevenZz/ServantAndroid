package servant.servantandroid.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ObservableMap<Key, Item> implements Map<Key, Item> {
    private HashMap<Key, Item> m_map = new HashMap<>();
    private List<MapListener<Item>> m_observers = new ArrayList<>();

    private void notifyItemAdd(Item item) {
        for(MapListener<Item> observer : m_observers) { observer.onItemAdd(item); }
    }

    private void notifyItemRemove(Item item) {
        for(MapListener<Item> observer : m_observers) { observer.onItemRemote(item); }
    }

    public void addObserver(MapListener<Item> observer)    { m_observers.add(observer);    }
    public void removeObserver(MapListener<Item> observer) { m_observers.remove(observer); }

    public ObservableMap() {}
    public ObservableMap(MapListener<Item> listener) { m_observers.add(listener); }

    @Override
    public int size() { return m_map.size(); }

    @Override
    public boolean isEmpty() { return m_map.isEmpty(); }

    @Override
    public boolean containsKey(@Nullable Object key) { return m_map.containsKey(key); }

    @Override
    public boolean containsValue(@Nullable Object value) { return m_map.containsValue(value); }

    @Nullable
    @Override
    public Item get(@Nullable Object key) { return m_map.get(key); }

    @Nullable
    @Override
    public Item put(@NonNull Key key, @NonNull Item value) {
        notifyItemAdd(value);
        return m_map.put(key, value);
    }

    @Nullable
    @Override
    public Item remove(@Nullable Object key) {
        Item item = m_map.remove(key);
        notifyItemRemove(item);
        return item;
    }

    @Override
    public void putAll(@NonNull Map<? extends Key, ? extends Item> m) {
        for(Key key : m.keySet()) {
            Item item = m.get(key);
            notifyItemAdd(item);
            m_map.put(key, item);
        }
    }

    @Override
    public void clear() {
        // inefficient af but currently irrelevant
        for(Key key : m_map.keySet()) notifyItemRemove(m_map.remove(key));
    }

    @NonNull
    @Override
    public Set<Key> keySet() { return m_map.keySet(); }

    @NonNull
    @Override
    public Collection<Item> values() { return m_map.values(); }

    @NonNull
    @Override
    public Set<Entry<Key, Item>> entrySet() { return m_map.entrySet(); }
}
