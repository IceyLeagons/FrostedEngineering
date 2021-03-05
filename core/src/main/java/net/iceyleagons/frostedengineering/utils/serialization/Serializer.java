package net.iceyleagons.frostedengineering.utils.serialization;

import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.icicle.jnbt.CompoundTag;
import net.iceyleagons.icicle.jnbt.NBTInputStream;
import net.iceyleagons.icicle.jnbt.StringTag;
import net.iceyleagons.icicle.jnbt.Tag;
import net.iceyleagons.icicle.misc.NBTUtils;

import java.io.File;
import java.io.FileInputStream;

public class Serializer implements SerializationHandler {

    @Override
    public void serialize(IFrostedEngineering iFrostedEngineering, ISerializable iSerializable, File file) {
        SerializationBuilder serializationBuilder = new SerializationBuilder();

        try {
            iSerializable.getClass().getDeclaredConstructor();
        } catch (NoSuchMethodException ignored) {
            iFrostedEngineering.getLogger().severe("Could not serialize an object because it does not have an empty constructor!");
            return;
        }

        iSerializable.serialize(serializationBuilder);
        int result = serializationBuilder.serializeIntoFile(file, iSerializable.getClass().getName());

        if (result == 0) {
            iFrostedEngineering.getLogger().warning("Tried to serialize an object, but it did not specify a value.");
        } else if (result == -1) {
            iFrostedEngineering.getLogger().severe("Could not serialize an object because of an exception!");
        }
    }

    @Override
    public void deserialize(IFrostedEngineering iFrostedEngineering, File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            try (NBTInputStream nbtInputStream = new NBTInputStream(fileInputStream)) {
                Tag tag = nbtInputStream.readTag();
                if (!(tag instanceof CompoundTag)) throw new IllegalArgumentException("Not valid serialization file!");

                CompoundTag compoundTag = (CompoundTag) tag;
                StringTag classTag = NBTUtils.getChildTag(compoundTag.getValue(), "className", StringTag.class);

                if (classTag == null) throw new IllegalArgumentException("Not valid serialization file!");
                Class<?> clazz = Class.forName(classTag.getValue());

                if (!ISerializable.class.isAssignableFrom(clazz)) throw new IllegalArgumentException("Not valid serialization file!");
                ISerializable iSerializable = (ISerializable) clazz.newInstance();

                iSerializable.deserialize(iFrostedEngineering, new SerializationFieldGetter(compoundTag));
            }
        } catch (Exception e) {
            iFrostedEngineering.getLogger().severe("Could not deserialize an object because of an exception!");
            e.printStackTrace();
        }
    }
}
