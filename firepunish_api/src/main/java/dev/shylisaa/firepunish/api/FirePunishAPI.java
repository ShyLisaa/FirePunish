package dev.shylisaa.firepunish.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.shylisaa.firepunish.api.database.DatabaseExecutor;
import dev.shylisaa.firepunish.api.database.type.DatabaseType;
import dev.shylisaa.firepunish.api.json.JsonFile;
import dev.shylisaa.firepunish.api.json.creator.FileCreator;
import dev.shylisaa.firepunish.api.punish.PunishHandler;
import dev.shylisaa.firepunish.api.punish.cache.MessageCache;
import dev.shylisaa.firepunish.api.punish.cache.ReasonCache;
import dev.shylisaa.firepunish.api.punish.reason.PunishReason;
import dev.shylisaa.firepunish.api.punish.type.PunishType;
import net.md_5.bungee.api.ProxyServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FirePunishAPI {

    private static FirePunishAPI instance;
    private JsonFile messageConfiguration;
    private JsonFile punishReasonsConfiguration;
    private JsonFile databaseConfiguration;
    private final ReasonCache reasonCache;
    private final MessageCache messageCache;
    private final DatabaseExecutor executor;
    private final PunishHandler punishHandler;
    private final ScheduledExecutorService executorService;

    public FirePunishAPI() {
        instance = this;

        executorService = Executors.newScheduledThreadPool(1);

        try {
            initConfigurations();
        } catch (IOException exception) {
            System.err.println("Could not create Files needed for Plugin.");
            exception.printStackTrace();
        }

        this.executor = new DatabaseExecutor(
                databaseConfiguration.get("host", String.class),
                databaseConfiguration.get("port", Integer.class),
                databaseConfiguration.get("username", String.class),
                databaseConfiguration.get("password", String.class),
                databaseConfiguration.get("database", String.class)
        );
        this.executor.connect();
        this.executor.createTable("firepunish_storage",
                this.executor.getTableInformation(
                        new String[]{"uniqueId", "banStorage", "kickStorage"},
                        new DatabaseType[]{DatabaseType.VARCHAR, DatabaseType.JSON, DatabaseType.JSON}));

        this.reasonCache = new ReasonCache();
        cacheReasons();

        this.messageCache = new MessageCache();
        cacheMessages();

        this.punishHandler = new PunishHandler();

        startKeepAliveScheduler();
    }

    public static FirePunishAPI getInstance() {
        return instance;
    }

    private void initConfigurations() throws IOException {
        final Path pluginPath = Path.of("plugins/FirePunish");
        final Path messagePath = Path.of("plugins/FirePunish", "messages.json");
        final Path reasonsPath = Path.of("plugins/FirePunish", "punishReasons.json");
        final Path databasePath = Path.of("plugins/FirePunish", "sql.json");

        if (Files.notExists(pluginPath)) Files.createDirectory(pluginPath);

        if (Files.notExists(messagePath)) {
            this.messageConfiguration = FileCreator.createMessageConfiguration();
        } else {
            this.messageConfiguration = new JsonFile(messagePath);
        }

        if (Files.notExists(reasonsPath)) {
            this.punishReasonsConfiguration = FileCreator.createReasonsConfiguration();
        } else {
            this.punishReasonsConfiguration = new JsonFile(reasonsPath);
        }

        if (Files.notExists(databasePath)) {
            this.databaseConfiguration = FileCreator.crateDatabaseConfiguration();
        } else {
            this.databaseConfiguration = new JsonFile(databasePath);
        }
    }

    private void cacheReasons() {
        JsonArray array = this.punishReasonsConfiguration.get("reasons", JsonArray.class);
        array.forEach(jsonElement -> {
            JsonObject object = jsonElement.getAsJsonObject();
            PunishReason reason = new PunishReason(
                    object.get("id").getAsInt(),
                    object.get("reason").getAsString(),
                    object.get("duration").getAsInt(),
                    object.get("permission").getAsString(),
                    PunishType.getByIdentifier(object.get("punishType").getAsString()));
            this.reasonCache.addReason(reason);
        });
        ProxyServer.getInstance().getLogger().info(String.format("Registered %s PunishReasons.", this.reasonCache.getCacheValueCount()));
    }

    private void cacheMessages() {
        this.messageConfiguration.getJsonObject().asMap().forEach(this.messageCache::cacheMessage);
        ProxyServer.getInstance().getLogger().info(String.format("Registered %s Messages.", this.messageCache.getCacheValueCount()));
    }

    private void startKeepAliveScheduler() {
        this.executorService.scheduleAtFixedRate(executor::sendKeepAlive, 0, 3, TimeUnit.HOURS);
    }

    public JsonFile getMessageConfiguration() {
        return messageConfiguration;
    }

    public JsonFile getPunishReasonsConfiguration() {
        return punishReasonsConfiguration;
    }

    public JsonFile getDatabaseConfiguration() {
        return databaseConfiguration;
    }

    public DatabaseExecutor getDatabaseExecutor() {
        return executor;
    }

    public MessageCache getMessageCache() {
        return messageCache;
    }

    public ReasonCache getReasonCache() {
        return reasonCache;
    }

    public PunishHandler getPunishHandler() {
        return punishHandler;
    }
}
