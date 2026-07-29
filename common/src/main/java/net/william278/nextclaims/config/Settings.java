/*
 * This file is part of NextClaims, licensed under the Apache License 2.0.
 */
package net.william278.nextclaims.config;

import net.william278.nextclaims.position.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Settings {

    private static Settings instance;

    private ClaimSettings claims = new ClaimSettings();

    public Settings() {}

    public ClaimSettings getClaims() {
        return claims;
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public static class ClaimSettings {

        private List<String> unclaimableWorlds = new ArrayList<>();

        public ClaimSettings() {}

        public List<String> getUnclaimableWorlds() {
            return unclaimableWorlds;
        }

        public void setUnclaimableWorlds(List<String> worlds) {
            this.unclaimableWorlds = worlds;
        }

        public boolean isWorldUnclaimable(@NotNull World world) {
            return unclaimableWorlds.stream().anyMatch(world.getName()::equalsIgnoreCase);
        }
    }
}
