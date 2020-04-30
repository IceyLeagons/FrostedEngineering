package net.iceyleagons.frostedengineering.addons;

public class LoadedAddon implements IAddon {

    private IAddon originatingFrom;
    private AddonDescription desc;

    public LoadedAddon(IAddon addon, AddonDescription desc) {
        originatingFrom = addon;
        this.desc = desc;
    }

    @Override
    public void onEnable(AddonManager manager) {
        originatingFrom.onEnable(manager);
    }

    @Override
    public void onDisable(AddonManager manager) {
        originatingFrom.onDisable(manager);
    }

    @Override
    public void onLoad(AddonManager manager) {
        originatingFrom.onLoad(manager);
    }

    public AddonDescription getDescription() {
        return desc;
    }

}
