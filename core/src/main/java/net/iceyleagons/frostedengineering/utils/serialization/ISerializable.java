package net.iceyleagons.frostedengineering.utils.serialization;

import net.iceyleagons.frostedengineering.api.IFrostedEngineering;

public interface ISerializable {

    void serialize(SerializationBuilder serializationBuilder);
    void deserialize(IFrostedEngineering iFrostedEngineering, SerializationFieldGetter serializationFieldGetter);

}
