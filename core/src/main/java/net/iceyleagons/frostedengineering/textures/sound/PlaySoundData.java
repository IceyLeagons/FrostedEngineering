package net.iceyleagons.frostedengineering.textures.sound;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.frostedengineering.api.textures.abstracts.TexturedSound;
import org.bukkit.Location;

@Data
@RequiredArgsConstructor
public class PlaySoundData {
    @NonNull
    TexturedSound parent;
    @NonNull
    Location loc;
    @NonNull
    boolean looped;

    int currentTime = 0;
}