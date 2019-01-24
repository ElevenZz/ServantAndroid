package servant.servantandroid.viewmodel.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * table definition for an instance
 */
@Entity class InstanceEntity {
    /**
     * local display name of the instance
     */
    @PrimaryKey @NonNull public String name = "N/A";

    /**
     * JSON serialized version of the instance
     */
    @ColumnInfo(name = "serialized_instance")
    String serialzedInstance;
}
