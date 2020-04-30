package net.iceyleagons.frostedengineering.textures.base;

import org.bukkit.Location;

public class SoundData {

    private Location loc;
    private boolean loop;
    private short currentTime;
    private TexturedSound parent;

    public SoundData(TexturedSound parent, Location loc, boolean loop) {
        this.parent = parent;
        this.loc = loc;
        this.loop = loop;
        this.currentTime = 0;
    }

    public TexturedSound getParent() {
        return parent;
    }

    public short getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(short currentTiem) {
        this.currentTime = currentTiem;
    }

    public void setLooping(boolean looping) {
        loop = looping;
    }

    public Location getLoc() {
        return loc;
    }

    public boolean isLooped() {
        return loop;
    }

}
