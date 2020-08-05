package net.iceyleagons.frostedengineering.addons;

public interface IAddon {

    /**
     * Runs when the addon gets enabled
     *
     * @param manager is the handler/manager of this addon
     */
    void onEnable(AddonManager manager);

    /**
     * Runs when the addon gets disabled
     *
     * @param manager is the handler/manager of this addon
     */
    void onDisable(AddonManager manager);

    /**
     * Runs when the addon gets loaded
     */
    void onLoad();

}
