package servant.servantandroid.viewmodel.persistence;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import androidx.core.util.Consumer;
import androidx.room.Room;
import servant.servantandroid.internal.ServantInstance;
import servant.servantandroid.internal.api_mirror.parameters.BaseParameter;
import servant.servantandroid.util.ExecuteAsync;

// singleton pattern like advised by android:
// "Note: If your app runs in a single process, you should follow the singleton design pattern when instantiating an AppDatabase object"
public class DatabaseService {
    private static DatabaseService instance;

    public static DatabaseService getInstance() { return instance; }
    public static void initialize(Context ctx) { instance = new DatabaseService(ctx); }

    private LocalDatabase m_db;

    private DatabaseService(Context ctx) {
        // Context prevents me from doing sum noice lazy initialization :(
        m_db = Room.databaseBuilder(
            ctx,
            LocalDatabase.class,
            LocalDatabase.class.getName()
        ).build();
    }

    private InstanceEntity[] instancesToEntities(ServantInstance... instances) {
        InstanceEntity[] entities = new InstanceEntity[instances.length];

        for (int c = 0; c < instances.length; c++ /* GOT EM */) {
            InstanceEntity entity = new InstanceEntity();

            entity.name = instances[c].getName();
            entity.serialzedInstance = new Gson().toJson(instances[c]);

            entities[c] = entity;
        }

        return entities;
    }

    public void close() { m_db.close(); }

    public void insertServantInstance(ServantInstance... instances) {
        ExecuteAsync.execute(() -> m_db.instanceDao().insert(instancesToEntities(instances)));
    }

    public void updateServantInstance(ServantInstance... instances) {
        ExecuteAsync.execute(() -> m_db.instanceDao().update(instancesToEntities(instances)));
    }

    public void removeServantInstance(ServantInstance... instances) {
        ExecuteAsync.execute(() -> m_db.instanceDao().delete(instancesToEntities(instances)));
    }

    public void getInstances(Consumer<ServantInstance> callback) {
        ExecuteAsync.execute(() -> {
            for(InstanceEntity entity : m_db.instanceDao().getAllInstances()) {
                callback.accept(
                    new GsonBuilder()
                        .registerTypeAdapter(BaseParameter.class, new JsonDeserializer<BaseParameter>() {
                            @Override
                            public BaseParameter deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                return context.deserialize(json, BaseParameter.getRegistry().resolve(
                                    json.getAsJsonObject().get("m_id").getAsString()
                                ));
                            }
                        }).create().fromJson(entity.serialzedInstance, ServantInstance.class)
                );
            }
        });
    }
}
