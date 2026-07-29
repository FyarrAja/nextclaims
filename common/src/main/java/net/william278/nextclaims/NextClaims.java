/*
 * This file is part of NextClaims, licensed under the Apache License 2.0.
 */
package net.william278.nextclaims;

import net.william278.nextclaims.config.Settings;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class NextClaims {

    public abstract Settings getSettings();
    public abstract Optional<String> getLocale(@NotNull String key, @NotNull String... args);
    public abstract Optional<String> getRawLocale(@NotNull String key);
    public abstract void sendMessage(@NotNull String message);
    public abstract boolean hasPermission(@NotNull String node, boolean defaultValue);
}
