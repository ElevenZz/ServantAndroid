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
import servant.servantandroid.internal.api_mirror.ApiElement;
import servant.servantandroid.internal.api_mirror.ApiListener;
import servant.servantandroid.internal.api_mirror.parameters.BaseParameter;
import servant.servantandroid.internal.api_mirror.parameters.ParameterFactory;
import servant.servantandroid.util.ExecuteAsync;

// singleton pattern like advised by android:
// "Note: If your app runs in a single process, you should follow the singleton design pattern when instantiating an AppDatabase object"
public class DatabaseService {
    private static DatabaseService instance;

    public static DatabaseService getInstance() { return instance; }
    public static void initialize(Context ctx)  { instance = new DatabaseService(ctx); }

    private LocalDatabase m_db;

    private DatabaseService(Context ctx) {
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

    /**
     * loads all instances from the cache and calls the callback for every single one
     * @param callback the callback to be called for every instance
     */
    public void getServantInstances(Consumer<ServantInstance> callback) {
        ExecuteAsync.execute(() -> {
            for(InstanceEntity entity : m_db.instanceDao().getAllInstances()) {
                callback.accept(
                    // i need to help Gson out since it isn't able to distinguish my abstract parameter types
                    new GsonBuilder()
                        .registerTypeAdapter(
                            BaseParameter.class,
                            (JsonDeserializer<BaseParameter>)(json, type, ctx) ->
                                ctx.deserialize(json, ParameterFactory.getInstance().resolve(
                                    json.getAsJsonObject().get("m_type").getAsString()
                                ))
                        )
                        .create()
                        .fromJson(entity.serialzedInstance, ServantInstance.class)
                );
            }
        });
    }
}
