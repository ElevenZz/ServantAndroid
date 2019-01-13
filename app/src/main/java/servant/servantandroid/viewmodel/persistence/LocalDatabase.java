package servant.servantandroid.viewmodel.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {InstanceEntity.class}, version = 1)
abstract class LocalDatabase extends RoomDatabase {
    abstract InstanceDao instanceDao();
}
