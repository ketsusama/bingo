package be.ketsu.bingo.utils;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

/**
 * This NMS class only works for Minecraft 1.17+
 */
public final class NMS {


    private NMS() {
    }

    /**
     * Get the singleton instance
     *
     * @return
     */
    public static NMS getInstance() {
        return NMSHolder.INSTANCE;
    }

    /**
     * Get the CraftPlayer from Player
     *
     * @param player
     * @return
     */
    @SneakyThrows
    public Object getCraftPlayer(@NonNull Player player) {
        return player.getClass().getMethod("getHandle").invoke(player);
    }

    /**
     * Get the EntityPlayer from Player
     *
     * @param player
     * @return
     */
    @SneakyThrows
    public Object getEntityPlayer(@NonNull Player player) {
        return getEntityPlayer(getCraftPlayer(player));
    }

    /**
     * Get the EntityPlayer from CraftPlayer
     *
     * @param craftPlayer
     * @return
     */
    @SneakyThrows
    public Object getEntityPlayer(@NonNull Object craftPlayer) {
        return craftPlayer.getClass().getField("b").get(craftPlayer);
    }

    /**
     * Get the PlayerConnection from Player
     *
     * @param player
     * @return
     */
    @SneakyThrows
    public Object getPlayerConnection(@NonNull Player player) {
        return getPlayerConnection(getEntityPlayer(player));
    }

    /**
     * Get the PlayerConnection from EntityPlayer
     *
     * @param entityPlayer
     * @return
     */
    @SneakyThrows
    public Object getPlayerConnection(@NonNull Object entityPlayer) {
        return entityPlayer.getClass().getField("a").get(entityPlayer);
    }

    /**
     * Send the Minecraft Packet to the Player
     *
     * @param player
     * @param packet
     */
    public void sendPacket(@NonNull Player player, @NonNull Object packet) {
        sendPacket(getPlayerConnection(player), packet);
    }

    /**
     * Send the Minecraft Packet to the PlayerConnection
     *
     * @param playerConnection
     * @param packet
     */
    public void sendPacket(@NonNull Object playerConnection, @NonNull Object packet) {
        try {
            Class<?> classPacket = Class.forName("net.minecraft.network.protocol.Packet");
            playerConnection.getClass().getMethod("a", classPacket).invoke(playerConnection, packet);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Thread safe singleton
     */
    private static class NMSHolder {
        private static final NMS INSTANCE = new NMS();
    }
}
