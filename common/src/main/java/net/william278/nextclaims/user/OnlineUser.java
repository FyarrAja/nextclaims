/*
 * This file is part of NextClaims, licensed under the Apache License 2.0.
 */
package net.william278.nextclaims.user;

import net.william278.nextclaims.NextClaims;
import net.william278.nextclaims.position.World;
import org.jetbrains.annotations.NotNull;

public interface OnlineUser {
    @NotNull
    String getName();

    @NotNull
    World getWorld();

    void sendMessage(@NotNull String message);

    boolean hasPermission(@NotNull String node, boolean defaultValue);
}
