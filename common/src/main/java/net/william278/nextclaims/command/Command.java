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
import net.william278.nextclaims.config.Settings;
import net.william278.nextclaims.position.World;
import net.william278.nextclaims.user.OnlineUser;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Command extends net.william278.nextclaims.command.Command {

    protected Command(@NotNull NextClaims plugin, @NotNull String name) {
        super(plugin, name);
    }

    @NotNull
    protected List<String> getUnclaimableWorlds() {
        return plugin.getSettings().getClaims().getUnclaimableWorlds();
    }

    protected boolean addUnclaimableWorld(@NotNull String worldName) {
        Settings.ClaimSettings claims = plugin.getSettings().getClaims();
        List<String> worlds = new java.util.ArrayList<>(claims.getUnclaimableWorlds());
        if (worlds.stream().anyMatch(w -> w.equalsIgnoreCase(worldName))) {
            return false;
        }
        worlds.add(worldName);
        claims.setUnclaimableWorlds(worlds);
        return true;
    }

    protected boolean removeUnclaimableWorld(@NotNull String worldName) {
        Settings.ClaimSettings claims = plugin.getSettings().getClaims();
        List<String> worlds = new java.util.ArrayList<>(claims.getUnclaimableWorlds());
        boolean removed = worlds.removeIf(w -> w.equalsIgnoreCase(worldName));
        if (removed) {
            claims.setUnclaimableWorlds(worlds);
        }
        return removed;
    }

    @NotNull
    protected String getCurrentWorld(@NotNull OnlineUser user) {
        return user.getWorld().getName();
    }

    protected abstract void execute(@NotNull OnlineUser user, @NotNull String[] args);
}
