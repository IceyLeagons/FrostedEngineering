package net.iceyleagons.frostedengineering.addons;

public abstract interface IAddon {

    /**
     * Runs when the addon gets enabled
     *
     * @param manager is the handler/manager of this addon
     */
    public void onEnable(AddonManager manager);

    /**
     * Runs when the addon gets disabled
     *
     * @param manager is the handler/manager of this addon
     */
    public void onDisable(AddonManager manager);

    /**
     * Runs when the addon gets loaded
     *
     * @param manager is the handler/manager of this addon
     */
    public void onLoad(AddonManager manager);

}
