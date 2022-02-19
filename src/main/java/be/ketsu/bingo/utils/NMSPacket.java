package be.ketsu.bingo.utils;

import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.logging.Logger;

public abstract class NMSPacket {

    protected final Class<?> classPacket;
    protected final Class<?> classIChatBaseComponent;
    protected final Class<?> classChatSerializer;
    private final Logger logger = Logger.getLogger(NMS.class.getName());

    public NMSPacket() {
        try {
            classPacket = Class.forName("net.minecraft.network.protocol.Packet");
            classIChatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
            classChatSerializer = Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    protected static JSONObject convert(String text) {
        JSONObject json = new JSONObject();
        json.put("text", text);
        return json;
    }

    protected abstract void onSend(@NonNull Player player);

    public final void send(@NonNull Player player) {
        try {
            onSend(player);
        } catch (Throwable e) {
            logger.severe("An error occurs while sending the NMSPacket (" + this.getClass().getSimpleName() + ":" + e.getStackTrace()[0].getLineNumber() + ")");
            throw new RuntimeException(e);
        }
    }

    public final void sendAll() {
        Bukkit.getOnlinePlayers().forEach(this::send);
    }

    @SneakyThrows
    protected Object convertJsonToIChatBaseComponent(String json) {
        return classChatSerializer.getMethod("a", String.class).invoke(null, json);
    }

    @Data
    public static class Title extends NMSPacket {

        private JSONObject title, subtitle;
        private int fadeIn, fadeOut, stay;

        public Title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
            this(convert(title), convert(subtitle), fadeIn, stay, fadeOut);
        }

        public Title(JSONObject title, JSONObject subtitle, int fadeIn, int fadeOut, int stay) {
            this.title = title;
            this.subtitle = subtitle;
            this.fadeIn = fadeIn;
            this.fadeOut = fadeOut;
            this.stay = stay;
        }

        public void setTitle(String title) {
            this.title = convert(title);
        }

        public void setSubTitle(String subtitle) {
            this.subtitle = convert(subtitle);
        }

        @Override
        @SneakyThrows
        protected void onSend(@NonNull Player player) {
            final Class<?> ClientboundSetTitlesAnimationPacket = Class.forName("net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket");
            final Object animationTitlePacket = ClientboundSetTitlesAnimationPacket
                .getConstructor(int.class, int.class, int.class)
                .newInstance(fadeIn, stay, fadeOut);
            NMS.getInstance().sendPacket(player, animationTitlePacket);

            if (title != null && !title.isEmpty()) {
                final Class<?> clientboundSetTitleTextPacket = Class.forName("net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket");
                final Object titlePacket = clientboundSetTitleTextPacket
                    .getConstructor(classIChatBaseComponent)
                    .newInstance(convertJsonToIChatBaseComponent(title.toString()));
                NMS.getInstance().sendPacket(player, titlePacket);
            }

            if (subtitle != null && !subtitle.isEmpty()) {
                final Class<?> clientboundSetSubtitleTextPacket = Class.forName("net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket");
                final Object subtitlePacket = clientboundSetSubtitleTextPacket
                    .getConstructor(classIChatBaseComponent)
                    .newInstance(convertJsonToIChatBaseComponent(subtitle.toString()));
                NMS.getInstance().sendPacket(player, subtitlePacket);
            }
        }
    }


    @Data
    public static class ResourcePack extends NMSPacket {

        private JSONObject message;
        private String url;
        private boolean force;

        public ResourcePack(String url, String message, boolean force) {
            this(url, convert(message), force);
        }

        public ResourcePack(String url, JSONObject message, boolean force) {
            this.url = url;
            this.message = message;
            this.force = force;
        }

        public void setMessage(String message) {
            this.message = convert(message);
        }

        @Override
        @SneakyThrows
        protected void onSend(@NonNull Player player) {
            Class<?> packetPlayOutResourcePackSend = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutResourcePackSend");
            Object resourcePackPacket = packetPlayOutResourcePackSend
                .getConstructor(String.class, String.class, boolean.class, classIChatBaseComponent)
                .newInstance(url,
                    DataUtils.sha1HashFromUrl(url),
                    force,
                    convertJsonToIChatBaseComponent(message.toString()));
            NMS.getInstance().sendPacket(player, resourcePackPacket);
        }
    }

    @Data
    public static class ActionBar extends NMSPacket {

        private JSONObject message;

        public ActionBar(String message) {
            this(convert(message));
        }

        public ActionBar(JSONObject message) {
            this.message = message;
        }

        public void setMessage(String message) {
            this.message = convert(message);
        }

        @Override
        @SneakyThrows
        protected void onSend(@NonNull Player player) {
            Class<?> classClientboundSetActionBarTextPacket = Class.forName("net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket");
            Object actionBarPacket = classClientboundSetActionBarTextPacket
                .getConstructor(classIChatBaseComponent)
                .newInstance(convertJsonToIChatBaseComponent(message.toString()));
            NMS.getInstance().sendPacket(player, actionBarPacket);
        }
    }

    @Data
    public static class HeaderAndFooter extends NMSPacket {

        private JSONObject header, footer;

        public HeaderAndFooter(String header, String footer) {
            this(convert(header), convert(footer));
        }

        public HeaderAndFooter(JSONObject header, JSONObject footer) {
            this.header = header;
            this.footer = footer;
        }

        public void setHeader(String header) {
            this.header = convert(header);
        }

        public void setFooter(String footer) {
            this.footer = convert(footer);
        }

        @Override
        @SneakyThrows
        protected void onSend(@NonNull Player player) {
            Class<?> classPacketPlayOutPlayerListHeaderFooter = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter");
            Object headerAndFooterPacket = classPacketPlayOutPlayerListHeaderFooter
                .getConstructor(classIChatBaseComponent, classIChatBaseComponent)
                .newInstance(convertJsonToIChatBaseComponent(header.toString()),
                    convertJsonToIChatBaseComponent(footer.toString()));
            NMS.getInstance().sendPacket(player, headerAndFooterPacket);
        }
    }


}
