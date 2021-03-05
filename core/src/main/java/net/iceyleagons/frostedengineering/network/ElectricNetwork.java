package net.iceyleagons.frostedengineering.network;

import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.frostedengineering.api.network.Network;
import net.iceyleagons.frostedengineering.api.network.Unit;
import net.iceyleagons.frostedengineering.api.network.UnitManager;
import net.iceyleagons.frostedengineering.api.other.registry.Registry;
import net.iceyleagons.frostedengineering.utils.serialization.ISerializable;
import net.iceyleagons.frostedengineering.utils.serialization.SerializationBuilder;
import net.iceyleagons.frostedengineering.utils.serialization.SerializationFieldGetter;
import org.json.JSONObject;

import java.util.UUID;

public class ElectricNetwork implements Network, ISerializable {

    private UUID uuid;
    private IFrostedEngineering iFrostedEngineering;
    private UnitManager unitManager;
    private final Registry<Unit> unitRegistry;

    public ElectricNetwork(UUID uuid, IFrostedEngineering iFrostedEngineering) {
        this.uuid = uuid;
        this.iFrostedEngineering = iFrostedEngineering;
        this.unitManager = iFrostedEngineering.getUnitManager();
        this.unitRegistry = new Registry<>();
    }

    public ElectricNetwork() {
        this.unitRegistry = new Registry<>();
    }

    @Override
    public Registry<Unit> getUnits() {
        return this.unitRegistry;
    }

    @Override
    public UnitManager getUnitManager() {
        return this.unitManager;
    }

    @Override
    public IFrostedEngineering getFrostedEngineering() {
        return iFrostedEngineering;
    }

    @Override
    public UUID getId() {
        return this.uuid;
    }

    @Override
    public JSONObject getMetadata() {
        return null;
    }

    @Override
    public void serialize(SerializationBuilder serializationBuilder) {
        serializationBuilder.set("uuid", uuid.toString());
    }

    @Override
    public void deserialize(IFrostedEngineering iFrostedEngineering, SerializationFieldGetter serializationFieldGetter) {
        this.iFrostedEngineering = iFrostedEngineering;
        this.unitManager = iFrostedEngineering.getUnitManager();

        this.uuid = UUID.fromString(serializationFieldGetter.get("uuid", String.class));
    }
}
