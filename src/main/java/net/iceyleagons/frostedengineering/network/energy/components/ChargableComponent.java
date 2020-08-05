package net.iceyleagons.frostedengineering.network.energy.components;

public interface ChargableComponent {

    float getMaxStorage();
    float getStored();
    float addEnergy(float fp);
    float consumeEnergy(float fp);
    boolean isFull();

}
