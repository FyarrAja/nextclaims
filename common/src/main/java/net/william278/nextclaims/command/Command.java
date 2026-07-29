/*
 * This file is part of NextClaims, licensed under the Apache License 2.0.
 */
package net.william278.nextclaims.command;

import net.william278.nextclaims.NextClaims;
import net.william278.nextclaims.user.OnlineUser;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class Command {

    protected final NextClaims plugin;
    protected final String name;

    protected Command(@NotNull NextClaims plugin, @NotNull String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public abstract void onExecute(@NotNull OnlineUser user, @NotNull String[] args);

    public abstract List<String> onTabComplete(@NotNull OnlineUser user, @NotNull String[] args);

    public void execute(@NotNull OnlineUser user, @NotNull String[] args) {
        onExecute(user, args);
    }
}
