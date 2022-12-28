package dev.shylisaa.firepunish.api.json.creator;

import dev.shylisaa.firepunish.api.json.JsonFile;
import dev.shylisaa.firepunish.api.json.builder.JsonArrayBuilder;
import dev.shylisaa.firepunish.api.json.builder.JsonObjectBuilder;

import java.nio.file.Path;

public class FileCreator {

    public static JsonFile createMessageConfiguration() {
        return new JsonFile()
                .addIfNotExists("prefix", "<dark_gray>[<#e60027>Guardian<dark_gray>]")
                .addIfNotExists("no-permissions", "%PREFIX% <red>You are missing the permission for this command.")
                .addIfNotExists("id-list-message", "<dark_gray>» <gray>%ID% <dark_gray>| <red>%REASON% <dark_gray>● <gray><italic>%TYPE%</italic>")
                .addIfNotExists("kick-team-message", JsonArrayBuilder.builder()
                        .put("<dark_gray><st>----------------------</st>")
                        .put(" ")
                        .put("%PREFIX% <gray>The player <red>%PLAYER% <gray>has been <red>kicked<dark_gray>.")
                        .put("%PREFIX% <gray>Reason <dark_gray>● <red>%REASON%")
                        .put("%PREFIX% <gray>Kicked from <dark_gray>● <red>%OPERATOR%")
                        .put(" ")
                        .put("<dark_gray><st>----------------------</st>")
                        .build())
                .addIfNotExists("ban-team-message", JsonArrayBuilder.builder()
                        .put("<dark_gray><st>----------------------</st>")
                        .put(" ")
                        .put("%PREFIX% <gray>The player <red>%PLAYER% <gray>has been <red>banned<dark_gray>.")
                        .put("%PREFIX% <gray>Reason <dark_gray>● <red>%REASON%")
                        .put("%PREFIX% <gray>Banned from <dark_gray>● <red>%OPERATOR%")
                        .put("%PREFIX% <gray>Ban expires <dark_gray>● <red>%TIME%")
                        .put(" ")
                        .put("<dark_gray><st>----------------------</st>")
                        .build())
                .addIfNotExists("kick-screen", JsonArrayBuilder.builder()
                        .put("     <red>ʏᴏᴜʀsᴇʀᴠᴇʀ.ɴᴇᴛ - ᴋɪᴄᴋ     ")
                        .put(" ")
                        .put("<gray>You have been <red>kicked<dark_gray>.")
                        .put("<gray>Reason <dark_gray>● <red>%REASON%")
                        .put(" ")
                        .put("<gray><italic>ᴘʟᴇᴀsᴇ ғᴏʟʟᴏᴡ ᴏᴜʀ ʀᴜʟᴇs ᴛᴏ ᴀᴠᴏɪᴅ ғᴜʀᴛʜᴇʀ ᴘᴜɴɪsʜᴍᴇɴᴛ.</italic>")
                        .build())
                .addIfNotExists("ban-screen", JsonArrayBuilder.builder()
                        .put("     <red>ʏᴏᴜʀsᴇʀᴠᴇʀ.ɴᴇᴛ - ʙᴀɴ     ")
                        .put(" ")
                        .put("<gray>You have been <red>banned<dark_gray>.")
                        .put("<gray>Reason <dark_gray>● <red>%REASON%")
                        .put("<gray>Ban expires <dark_gray>● <red>%DATE%")
                        .put(" ")
                        .put("<gray><italic>ᴘʟᴇᴀsᴇ ғᴏʟʟᴏᴡ ᴏᴜʀ ʀᴜʟᴇs ᴛᴏ ᴀᴠᴏɪᴅ ғᴜʀᴛʜᴇʀ ᴘᴜɴɪsʜᴍᴇɴᴛ.</italic>")
                        .build()).writeToFile(Path.of("plugins/FirePunish", "messages.json"));
    }

    public static JsonFile createReasonsConfiguration() {
        return new JsonFile()
                .addIfNotExists("reasons", JsonArrayBuilder.builder()
                        .put(JsonObjectBuilder.builder()
                                .put("id", 1)
                                .put("reason", "HACKING/CLIENTMODS")
                                .put("duration", -1)
                                .put("permission", "firepunish.reason.hacking")
                                .put("punishType", "BAN")
                                .build())
                        .put(JsonObjectBuilder.builder()
                                .put("id", 2)
                                .put("reason", "ADVERTISEMENT")
                                .put("duration", -1)
                                .put("permission", "firepunish.reason.advertisement")
                                .put("punishType", "MUTE")
                                .build())
                        .build()).writeToFile(Path.of("plugins/FirePunish", "punishReasons.json"));
    }

    public static JsonFile crateDatabaseConfiguration() {
        return new JsonFile()
                .addIfNotExists("host", "127.0.0.1")
                .addIfNotExists("port", 3306)
                .addIfNotExists("username", "root")
                .addIfNotExists("password", "")
                .addIfNotExists("database", "fire_punish")
                .writeToFile(Path.of("plugins/FirePunish", "sql.json"));
    }
}
