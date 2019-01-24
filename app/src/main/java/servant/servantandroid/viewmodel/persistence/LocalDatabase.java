package servant.servantandroid.viewmodel.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * local cache containing serialized servant instances
 * in the form of a room database
 */
@Database(entities = {InstanceEntity.class}, version = 1)
abstract class LocalDatabase extends RoomDatabase {
    abstract InstanceDao instanceDao();
}
