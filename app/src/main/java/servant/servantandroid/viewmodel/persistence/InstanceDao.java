package servant.servantandroid.viewmodel.persistence;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * room queries to interact with the local instance cache
 */
@Dao interface InstanceDao {
    /**
     * query getting all instances
     * @return
     */
    @Query("SELECT * FROM InstanceEntity")
    List<InstanceEntity> getAllInstances();

    /**
     * query getting a specific instance by name
     * @param name name of the instance
     * @return found instance or null if not found
     */
    @Query("SELECT * FROM InstanceEntity WHERE name = :name LIMIT 1")
    InstanceEntity getInstance(String name);

    /**
     * add new instance to the local db
     * @param instances instances to add
     */
    @Insert void insert(InstanceEntity... instances);

    /**
     * update one or more instances
     * @param instances instances to update
     */
    @Update void update(InstanceEntity... instances);

    /**
     * delete one or more instances
     * @param entity instances to delete
     */
    @Delete void delete(InstanceEntity... entity);
}
