package servant.servantandroid.viewmodel.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
class InstanceEntity {
    @PrimaryKey @NonNull
    public String name;

    @ColumnInfo(name = "serialized_instance")
    public String serialzedInstance;
}
