package net.karen.mccourse.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
import java.util.function.*;

public class ModNetworks {
    // START and END of user code block mod methods
    private static final String NAME = "mccourse";
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER =
            NetworkRegistry.newSimpleChannel(new ResourceLocation(NAME, NAME),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    // 1. CLASS (MESSAGE TYPE) ; 2. ENCODER -> BUFFER; 3. DECODER -> ::new; 4. CONSUMER -> ::handler (MESSAGE CONSUMER)
    public static <T> void addNetworkMessage(Class<T> classType,
                                             BiConsumer<T, FriendlyByteBuf> encode, Function<FriendlyByteBuf, T> decode,
                                             BiConsumer<T, Supplier<NetworkEvent.Context>> consumer) {
        PACKET_HANDLER.registerMessage(messageID, classType, encode, decode, consumer);
        messageID++;
    }
}