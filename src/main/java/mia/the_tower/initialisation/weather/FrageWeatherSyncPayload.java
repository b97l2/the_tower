package mia.the_tower.initialisation.weather;

import mia.the_tower.The_Tower;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record FrageWeatherSyncPayload(FrageWeatherPattern pattern, float overcastStrength) implements CustomPayload {
    public static final Id<FrageWeatherSyncPayload> ID = new Id<>(The_Tower.id("frage_weather"));
    public static final PacketCodec<RegistryByteBuf, FrageWeatherSyncPayload> CODEC =
            PacketCodec.of(FrageWeatherSyncPayload::write, FrageWeatherSyncPayload::new);

    private FrageWeatherSyncPayload(RegistryByteBuf buf) {
        this(buf.readEnumConstant(FrageWeatherPattern.class), buf.readFloat());
    }

    private void write(RegistryByteBuf buf) {
        buf.writeEnumConstant(this.pattern);
        buf.writeFloat(this.overcastStrength);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
