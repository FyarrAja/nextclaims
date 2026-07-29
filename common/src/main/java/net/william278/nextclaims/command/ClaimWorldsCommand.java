/*
 * This file is part of NextClaims, licensed under the Apache License 2.0.
 *
 *  Copyright (c) William278 <will27528@gmail.com>
 *  Copyright (c) contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.william278.nextclaims.command;

import net.william278.nextclaims.NextClaims;
import net.william278.nextclaims.user.OnlineUser;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class ClaimWorldsCommand extends Command {

    private static final String PERMISSION = "NextClaims.admin.claimworlds";

    public ClaimWorldsCommand(@NotNull NextClaims plugin) {
        super(plugin, "claimworlds");
        if (plugin.getCommandProvider().map(p -> !p.hasCommand("claimworlds")).orElse(true)) {
            plugin.getCommandProvider().ifPresent(p -> p.provideCommand(this));
        }
    }

    @Override
    public void onExecute(@NotNull OnlineUser user, @NotNull String[] args) {
        if (!user.hasPermission(PERMISSION, true)) {
            plugin.getLocales().getLocale("error_no_permission")
                    .ifPresent(user::sendMessage);
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
            plugin.getLocales().getLocale("error_invalid_syntax", "/claimworlds add <world>")
                    .ifPresent(user::sendMessage);
            return;
        }

        String worldName = args[1];
        if (addUnclaimableWorld(worldName)) {
            plugin.getLocales().getLocale("claimworlds_added", worldName)
                    .ifPresent(user::sendMessage);
        } else {
            plugin.getLocales().getLocale("claimworlds_already_added", worldName)
                    .ifPresent(user::sendMessage);
        }
    }

    private void handleRemove(@NotNull OnlineUser user, @NotNull String[] args) {
        if (args.length < 2) {
            plugin.getLocales().getLocale("error_invalid_syntax", "/claimworlds remove <world>")
                    .ifPresent(user::sendMessage);
            return;
        }

        String worldName = args[1];
        if (removeUnclaimableWorld(worldName)) {
            plugin.getLocales().getLocale("claimworlds_removed", worldName)
                    .ifPresent(user::sendMessage);
        } else {
            plugin.getLocales().getLocale("claimworlds_not_in_list", worldName)
                    .ifPresent(user::sendMessage);
        }
    }

    private void handleList(@NotNull OnlineUser user) {
        List<String> worlds = getUnclaimableWorlds();
        if (worlds.isEmpty()) {
            plugin.getLocales().getLocale("claimworlds_empty")
                    .ifPresent(user::sendMessage);
        } else {
            String list = worlds.stream()
                    .collect(Collectors.joining(", "));
            plugin.getLocales().getLocale("claimworlds_list", list)
                    .ifPresent(user::sendMessage);
        }
    }

    private void handleToggle(@NotNull OnlineUser user, @NotNull String[] args) {
        if (args.length < 2) {
            // Toggle current world
            String currentWorld = getCurrentWorld(user);
            toggleWorld(user, currentWorld);
        } else {
            String worldName = args[1];
            toggleWorld(user, worldName);
        }
    }

    private void toggleWorld(@NotNull OnlineUser user, @NotNull String worldName) {
        List<String> worlds = getUnclaimableWorlds();
        if (worlds.stream().anyMatch(w -> w.equalsIgnoreCase(worldName))) {
            removeUnclaimableWorld(worldName);
            plugin.getLocales().getLocale("claimworlds_toggled_off", worldName)
                    .ifPresent(user::sendMessage);
        } else {
            addUnclaimableWorld(worldName);
            plugin.getLocales().getLocale("claimworlds_toggled_on", worldName)
                    .ifPresent(user::sendMessage);
        }
    }

    private void sendHelp(@NotNull OnlineUser user) {
        user.sendMessage(plugin.getLocales().getRawLocale("claimworlds_help_header")
                .orElse("[NextClaims] Claim Worlds Command:"));
        user.sendMessage(plugin.getLocales().getRawLocale("claimworlds_help_add")
                .orElse("  /claimworlds add <world> - Add world to blacklist"));
        user.sendMessage(plugin.getLocales().getRawLocale("claimworlds_help_remove")
                .orElse("  /claimworlds remove <world> - Remove world from blacklist"));
        user.sendMessage(plugin.getLocales().getRawLocale("claimworlds_help_list")
                .orElse("  /claimworlds list - Show all unclaimable worlds"));
        user.sendMessage(plugin.getLocales().getRawLocale("claimworlds_help_toggle")
                .orElse("  /claimworlds toggle [world] - Toggle current or specified world"));
    }

    @NotNull
    @Override
    public List<String> onTabComplete(@NotNull OnlineUser user, @NotNull String[] args) {
        if (!user.hasPermission(PERMISSION, false)) {
            return List.of();
        }

        if (args.length == 1) {
            return List.of("add", "remove", "list", "toggle");
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("toggle"))) {
            return getUnclaimableWorlds().stream()
                    .filter(w -> w.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
