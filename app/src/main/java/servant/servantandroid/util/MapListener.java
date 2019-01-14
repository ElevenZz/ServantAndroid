package servant.servantandroid.util;

public interface MapListener<ItemType> {
    void onItemAdd(ItemType item);
    void onItemRemote(ItemType item);
}
