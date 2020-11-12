package dev.lallemand.lcore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minexd.pidgin.Pidgin;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.qrakn.honcho.Honcho;
import dev.lallemand.lcore.cache.RedisCache;
import dev.lallemand.lcore.chat.command.ClearChatCommand;
import dev.lallemand.lcore.chat.command.MuteChatCommand;
import dev.lallemand.lcore.chat.command.SlowChatCommand;
import dev.lallemand.lcore.config.ConfigValidation;
import dev.lallemand.lcore.config.utils.BasicConfigurationFile;
import dev.lallemand.lcore.essentials.Essentials;
import dev.lallemand.lcore.essentials.EssentialsListener;
import dev.lallemand.lcore.essentials.command.*;
import dev.lallemand.lcore.essentials.tasks.AnnouncementTask;
import dev.lallemand.lcore.network.NetworkPacketListener;
import dev.lallemand.lcore.network.command.ReportCommand;
import dev.lallemand.lcore.network.command.RequestCommand;
import dev.lallemand.lcore.network.packet.*;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.profile.ProfileListener;
import dev.lallemand.lcore.profile.ProfileTypeAdapter;
import dev.lallemand.lcore.profile.conversation.command.MessageCommand;
import dev.lallemand.lcore.profile.conversation.command.ReplyCommand;
import dev.lallemand.lcore.profile.conversation.command.ignore.IgnoreAddCommand;
import dev.lallemand.lcore.profile.conversation.command.ignore.IgnoreCommand;
import dev.lallemand.lcore.profile.conversation.command.ignore.IgnoreListCommand;
import dev.lallemand.lcore.profile.conversation.command.ignore.IgnoreRemoveCommand;
import dev.lallemand.lcore.profile.grant.GrantListener;
import dev.lallemand.lcore.profile.grant.command.GrantCommand;
import dev.lallemand.lcore.profile.grant.command.RevokeCommand;
import dev.lallemand.lcore.profile.notes.commands.*;
import dev.lallemand.lcore.profile.option.command.OptionsCommand;
import dev.lallemand.lcore.profile.option.command.ToggleGlobalChatCommand;
import dev.lallemand.lcore.profile.option.command.TogglePrivateMessagesCommand;
import dev.lallemand.lcore.profile.option.command.ToggleSoundsCommand;
import dev.lallemand.lcore.profile.permissions.PermissionsCommand;
import dev.lallemand.lcore.profile.prefix.Prefix;
import dev.lallemand.lcore.profile.prefix.commands.PrefixCommand;
import dev.lallemand.lcore.profile.prefix.commands.SetPrefixCommand;
import dev.lallemand.lcore.profile.prefix.commands.UnSetPrefixCommand;
import dev.lallemand.lcore.profile.punishment.BanReason;
import dev.lallemand.lcore.profile.punishment.command.*;
import dev.lallemand.lcore.profile.punishment.listener.PunishmentListener;
import dev.lallemand.lcore.profile.staff.command.*;
import dev.lallemand.lcore.profile.staff.listeners.StaffModeListener;
import dev.lallemand.lcore.rank.Rank;
import dev.lallemand.lcore.rank.RankTypeAdapter;
import dev.lallemand.lcore.rank.command.*;
import dev.lallemand.lcore.util.adapter.ChatColorTypeAdapter;
import dev.lallemand.lcore.util.duration.Duration;
import dev.lallemand.lcore.util.duration.DurationTypeAdapter;
import dev.lallemand.lcore.util.hotbar.HotbarListener;
import dev.lallemand.lcore.util.item.ItemBuilder;
import dev.lallemand.lcore.util.menu.MenuListener;
import dev.lallemand.lcore.util.string.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import dev.lallemand.lcore.chat.Chat;
import dev.lallemand.lcore.chat.ChatListener;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

@Getter
public class lCore extends JavaPlugin {

    public static final Gson GSON = new Gson();
    public static final Type LIST_STRING_TYPE = new TypeToken<List<String>>() {
    }.getType();

    private static lCore lcore;

    private BasicConfigurationFile mainConfig;

    private Honcho honcho;
    private Pidgin pidgin;

    private MongoDatabase mongoDatabase;
    private JedisPool jedisPool;
    private RedisCache redisCache;

    private Essentials essentials;
    private Chat chat;

    private MongoClient mongoClient;

    public BasicConfigurationFile getMainConfig() {
        return mainConfig;
    }

    @Override
    public void onEnable() {
        lcore = this;

        mainConfig = new BasicConfigurationFile(this, "config");

        new ConfigValidation(mainConfig.getFile(), mainConfig.getConfiguration(), 4).check();

        loadMongo();
        loadRedis();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        redisCache = new RedisCache(this);
        essentials = new Essentials(this);
        chat = new Chat(this);

        honcho = new Honcho(this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Prefix.load();
        BanReason.load();

        Arrays.asList(
            // essentials commands
            new HubCommand(),
            new BroadcastCommand(),
            new ClearCommand(),
            new DayCommand(),
            new FlyCommand(),
            new InvModify(),
            new NoteCommand(),
            new NoteAddCommand(),
            new NoteRemoveCommand(),
            new NoteUpdateCommand(),
            new NotesCommand(),
            new EnchantmentCommand(),
            new GameModeCommand(),
            new GMCCommand(),
            new GMSCommand(),
            new VoteCommand(),
            new VoteClaimCommand(),
            new FeedCommand(),
            new HelpCommand(),
            new HealCommand(),
            new HidePlayerCommand(),
            new InvseeCommand(),
            new ListCommand(),
            new LagCommand(),
            new KillallCommand(),
            new CoreReloadCommand(),
            new LocationCommand(),
            new MoreCommand(),
            new NightCommand(),
            new PingCommand(),
            new RenameCommand(),
            new SetSlotsCommand(),
            new SetSpawnCommand(),
            new ShowAllPlayersCommand(),
            new ShowPlayerCommand(),
            new SkullCommand(),
            new TPHereCommand(),
            new SpawnCommand(),
            new SudoAllCommand(),
            new SudoCommand(),
            new SunsetCommand(),
            new TeleportWorldCommand(),
            new OptionsCommand(),
            new ToggleGlobalChatCommand(),
            new TogglePrivateMessagesCommand(),
            new ToggleSoundsCommand(),
            new ReportCommand(),
            new RequestCommand(),
            new ToggleDonorOnlyCommand(),
            new RestartCommand(),

            // staff commands
            new WhoisCommand(),
            new JoinCommand(),
            new FreezeCommand(),
            new ClearChatCommand(),
            new SlowChatCommand(),
            new AltsCommand(),
            new AuthCommand(),
            new InvalidateAuth(),
            new ResetAuth(),
            new SocialSpyCommand(),
            new BanCommand(),
            new BlacklistCommand(),
            new UnblacklistCommand(),
            new CheckCommand(),
            new KickCommand(),
            new CancelBanCommand(),
            new MuteCommand(),
            new PunishmentCommand(),
            new UnbanCommand(),
            new UnmuteCommand(),
            new WarnCommand(),
            new MuteChatCommand(),
            new StaffChatCommand(),
            new ToggleStaffChat(),
            new VanishCommand(),
            new HideStaffCommand(),
            new BanListCommand(),

            // permissions commands
            new GrantCommand(),
            new RevokeCommand(),
            new RankAddPermissionCommand(),
            new RankCreateCommand(),
            new RankDeleteCommand(),
            new RankHelpCommand(),
            new RankInfoCommand(),
            new RankInheritCommand(),
            new RankRemovePermissionCommand(),
            new RanksCommand(),
            new RankSetColorCommand(),
            new RankSetPrefixCommand(),
            new RankSetWeightCommand(),
            new RankUninheritCommand(),
            new ClearPunishmentsCommand(),
            new PermissionsCommand(),

            // conversation commands
            new MessageCommand(),
            new ReplyCommand(),
            new IgnoreCommand(),
            new IgnoreAddCommand(),
            new IgnoreListCommand(),
            new IgnoreRemoveCommand(),

            new PrefixCommand(),
            new SetPrefixCommand(),
            new UnSetPrefixCommand()
        ).forEach(honcho::registerCommand);

        honcho.registerTypeAdapter(Rank.class, new RankTypeAdapter());
        honcho.registerTypeAdapter(Profile.class, new ProfileTypeAdapter());
        honcho.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        honcho.registerTypeAdapter(ChatColor.class, new ChatColorTypeAdapter());

        pidgin = new Pidgin("lcore",
            mainConfig.getString("REDIS.HOST"),
            mainConfig.getInteger("REDIS.PORT"),
            mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED") ?
                mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD") : null
        );

        Arrays.asList(
            PacketAddGrant.class,
            PacketAnticheatAlert.class,
            PacketBroadcastPunishment.class,
            PacketDeleteGrant.class,
            PacketDeleteRank.class,
            PacketRefreshRank.class,
            PacketStaffAuth.class,
            PacketStaffChat.class,
            PacketStaffJoinNetwork.class,
            PacketStaffLeaveNetwork.class,
            PacketStaffSwitchServer.class,
            PacketStaffReport.class,
            PacketStaffRequest.class,
            PacketClearGrants.class,
            PacketClearPunishments.class
        ).forEach(pidgin::registerPacket);

        pidgin.registerListener(new NetworkPacketListener(this));

        Arrays.asList(
            new ProfileListener(),
            new MenuListener(),
            new EssentialsListener(),
            new ChatListener(),
            new GrantListener(),
            new PunishmentListener(),
            new HotbarListener(),
            new StaffModeListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        if (mainConfig.getBoolean("STAFF.TP_COMMAND")) {
            honcho.registerCommand(new TeleportCommand());
        }

        if (mainConfig.getBoolean("STAFF.MOD_COMMAND")) {
            honcho.registerCommand(new StaffModeCommand());
        }


        Rank.init();
        new AnnouncementTask();
        ItemBuilder.registerGlow();
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            ProfileListener.leavePlayer(player, true);
        });
        try {
            jedisPool.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadMongo() {
        mongoClient = new MongoClient(
                getConfig().getString("Mongo.Host"),
                getConfig().getInt("Mongo.Port")
        );


        String databaseId = getConfig().getString("Mongo.Database");
        mongoDatabase = mongoClient.getDatabase(databaseId);

    }

    private void loadRedis() {
        jedisPool = new JedisPool(mainConfig.getString("REDIS.HOST"), mainConfig.getInteger("REDIS.PORT"));

        if (mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED")) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.auth(mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD"));
            }
        }
    }


    /**
     * Broadcasts a message to all server operators.
     *
     * @param message The message.
     */
    public static void broadcastOps(String message) {
        Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(op -> op.sendMessage(CC.translate(message)));
    }

    public static lCore get() {
        return lcore;
    }

}
