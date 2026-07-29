/*
 * This file is part of NextClaims, licensed under the Apache License 2.0.
 */
package net.william278.nextclaims.command;

import net.william278.nextclaims.NextClaims;
import net.william278.nextclaims.user.OnlineUser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClaimWorldsCommand extends Command {

    private static final String PERMISSION = "NextClaims.admin.claimworlds";

    public ClaimWorldsCommand(@NotNull NextClaims plugin) {
        super(plugin, "claimworlds");
    }

    @Override
    public void onExecute(@NotNull OnlineUser user, @NotNull String[] args) {
        if (!user.hasPermission(PERMISSION, true)) {
            plugin.sendMessage("[Error] You do not have permission to use this command.");
            return;
        }

        if (args.length == 0) {
            sendHelp(user);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "add" -> handleAdd(user, args);
            case "remove", "delete" -> handleRemove(user, args);
            case "list" -> handleList(user);
            case "toggle" -> handleToggle(user, args);
            default -> sendHelp(user);
        }
    }

    private void handleAdd(@NotNull OnlineUser user, @NotNull String[] args) {
        if (args.length < 2) {
            plugin.sendMessage("[Error] Invalid syntax. Usage: /claimworlds add <world>");
            return;
        }

        String worldName = args[1];
        List<String> worlds = new ArrayList<>(plugin.getSettings().getClaims().getUnclaimableWorlds());

        if (worlds.stream().anyMatch(w -> w.equalsIgnoreCase(worldName))) {
            plugin.sendMessage("[Error] World " + worldName + " is already in the unclaimable worlds list.");
            return;
        }

        worlds.add(worldName);
        plugin.getSettings().getClaims().setUnclaimableWorlds(worlds);
        plugin.sendMessage("[Success] Added " + worldName + " to unclaimable worlds list.");
    }

    private void handleRemove(@NotNull OnlineUser user, @NotNull String[] args) {
        if (args.length < 2) {
            plugin.sendMessage("[Error] Invalid syntax. Usage: /claimworlds remove <world>");
            return;
        }

        String worldName = args[1];
        List<String> worlds = new ArrayList<>(plugin.getSettings().getClaims().getUnclaimableWorlds());

        boolean removed = worlds.removeIf(w -> w.equalsIgnoreCase(worldName));
        if (removed) {
            plugin.getSettings().getClaims().setUnclaimableWorlds(worlds);
            plugin.sendMessage("[Success] Removed " + worldName + " from unclaimable worlds list.");
        } else {
            plugin.sendMessage("[Error] World " + worldName + " is not in the unclaimable worlds list.");
        }
    }

    private void handleList(@NotNull OnlineUser user) {
        List<String> worlds = plugin.getSettings().getClaims().getUnclaimableWorlds();
        if (worlds.isEmpty()) {
            plugin.sendMessage("[Claim Worlds] No worlds are currently blacklisted from claiming.");
        } else {
            String list = worlds.stream().collect(Collectors.joining(", "));
            plugin.sendMessage("[Unclaimable Worlds]: " + list);
        }
    }

    private void handleToggle(@NotNull OnlineUser user, @NotNull String[] args) {
        String worldName;
        if (args.length < 2) {
            worldName = user.getWorld().getName();
        } else {
            worldName = args[1];
        }

        List<String> worlds = new ArrayList<>(plugin.getSettings().getClaims().getUnclaimableWorlds());

        if (worlds.stream().anyMatch(w -> w.equalsIgnoreCase(worldName))) {
            worlds.removeIf(w -> w.equalsIgnoreCase(worldName));
            plugin.getSettings().getClaims().setUnclaimableWorlds(worlds);
            plugin.sendMessage("[Success] " + worldName + " is now claimable.");
        } else {
            worlds.add(worldName);
            plugin.getSettings().getClaims().setUnclaimableWorlds(worlds);
            plugin.sendMessage("[Success] " + worldName + " is now unclaimable.");
        }
    }

    private void sendHelp(@NotNull OnlineUser user) {
        plugin.sendMessage("[NextClaims Claim Worlds Command:]");
        plugin.sendMessage("  /claimworlds add <world> - Add world to blacklist");
        plugin.sendMessage("  /claimworlds remove <world> - Remove world from blacklist");
        plugin.sendMessage("  /claimworlds list - Show all unclaimable worlds");
        plugin.sendMessage("  /claimworlds toggle [world] - Toggle current or specified world");
    }

    @Override
    public List<String> onTabComplete(@NotNull OnlineUser user, @NotNull String[] args) {
        if (!user.hasPermission(PERMISSION, false)) {
            return List.of();
        }

        if (args.length == 1) {
            return List.of("add", "remove", "list", "toggle");
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("toggle"))) {
            return plugin.getSettings().getClaims().getUnclaimableWorlds().stream()
                    .filter(w -> w.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
