package net.iceyleagons.frostedengineering.utils.serialization;

import net.iceyleagons.icicle.jnbt.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SerializationBuilder {

    private final Map<String, Tag> tags = new HashMap<>();

    /**
     * Sets the data with the specified value.
     *
     * @param key the data identifier (You will use this to retrieve the serialized data)
     * @param value the value to set
     * @return this builder for chaining
     */
    public SerializationBuilder set(String key, byte value) {
        tags.put(key, new ByteTag(key, value));
        return this;
    }

    /**
     * Sets the data with the specified value.
     *
     * @param key the data identifier (You will use this to retrieve the serialized data)
     * @param value the value to set
     * @return this builder for chaining
     */
    public SerializationBuilder set(String key, byte[] value) {
        tags.put(key, new ByteArrayTag(key, value));
        return this;
    }

    /**
     * Sets the data with the specified value.
     *
     * @param key the data identifier (You will use this to retrieve the serialized data)
     * @param value the value to set
     * @return this builder for chaining
     */
    public SerializationBuilder set(String key, double value) {
        tags.put(key, new DoubleTag(key, value));
        return this;
    }

    /**
     * Sets the data with the specified value.
     *
     * @param key the data identifier (You will use this to retrieve the serialized data)
     * @param value the value to set
     * @return this builder for chaining
     */
    public SerializationBuilder set(String key, float value) {
        tags.put(key, new FloatTag(key, value));
        return this;
    }

    /**
     * Sets the data with the specified value.
     *
     * @param key the data identifier (You will use this to retrieve the serialized data)
     * @param value the value to set
     * @return this builder for chaining
     */
    public SerializationBuilder set(String key, int[] value) {
        tags.put(key, new IntArrayTag(key, value));
        return this;
    }

    /**
     * Sets the data with the specified value.
     *
     * @param key the data identifier (You will use this to retrieve the serialized data)
     * @param value the value to set
     * @return this builder for chaining
     */
    public SerializationBuilder set(String key, long value) {
        tags.put(key, new LongTag(key, value));
        return this;
    }

    /**
     * Sets the data with the specified value.
     *
     * @param key the data identifier (You will use this to retrieve the serialized data)
     * @param value the value to set
     * @return this builder for chaining
     */
    public SerializationBuilder set(String key, short value) {
        tags.put(key, new ShortTag(key, value));
        return this;
    }

    /**
     * Sets the data with the specified value.
     *
     * @param key the data identifier (You will use this to retrieve the serialized data)
     * @param value the value to set
     * @return this builder for chaining
     */
    public SerializationBuilder set(String key, String value) {
        tags.put(key, new StringTag(key, value));
        return this;
    }

    /**
     * Builds the data and writes it to the specified file.
     *
     * @param file the {@link File} to write to
     * @return  an int representing outcome:
     *          -1 : Exception occurred
     *          0  : Nothing to serialize (no data has been set)
     *          1  : successful outcome
     */
    public int serializeIntoFile(File file, String className) {
        if (tags.isEmpty()) return 0;
        set("className", className);

        CompoundTag data = new CompoundTag("data", tags);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            try (NBTOutputStream nbtOutputStream = new NBTOutputStream(fileOutputStream)) {
                nbtOutputStream.writeTag(data);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
