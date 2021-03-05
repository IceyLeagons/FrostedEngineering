package net.iceyleagons.frostedengineering.utils.serialization;

import net.iceyleagons.frostedengineering.api.IFrostedEngineering;

import java.io.File;

public interface SerializationHandler {

    void serialize(IFrostedEngineering iFrostedEngineering, ISerializable iSerializable, File file);
    void deserialize(IFrostedEngineering iFrostedEngineering, File file);

}
