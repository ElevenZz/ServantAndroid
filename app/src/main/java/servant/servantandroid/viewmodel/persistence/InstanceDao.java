package servant.servantandroid.viewmodel.persistence;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
interface InstanceDao {
    @Query("SELECT * FROM InstanceEntity")
    List<InstanceEntity> getAllInstances();

    @Query("SELECT * FROM InstanceEntity WHERE name = :name LIMIT 1")
    InstanceEntity getInstance(String name);

    @Insert
    void insert(InstanceEntity... instances);

    @Update
    void update(InstanceEntity... instances);

    @Delete
    void delete(InstanceEntity entity);
}
