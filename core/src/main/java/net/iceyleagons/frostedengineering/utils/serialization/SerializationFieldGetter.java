package net.iceyleagons.frostedengineering.utils.serialization;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import net.iceyleagons.icicle.jnbt.*;
import net.iceyleagons.icicle.misc.NBTUtils;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public class SerializationFieldGetter {

    private final CompoundTag data;

    /**
     * Used to retrieve data that has been set with SerializationBuilder#set.
     *
     * @param key  the key of the data
     * @param type the wanted type
     * @param <T>  the wanted type
     * @return the value in the given type or null.
     */
    @Nullable
    public <T> T get(String key, Class<T> type) {
        Class<? extends Tag> required = getRequiredTagType(type);
        Preconditions.checkNotNull(required, "Given type is not supported.");

        Object value = NBTUtils.getChildTag(data.getValue(), key, required).getValue();
        return type.isInstance(value) ? type.cast(value) : null;
    }

    /**
     * Used for retrieving the appropriate Tag type for a specified Java type.
     * Supported types are the following: String, int, short, byte, byte[], double, float, int[], long
     *
     * @param clazz the Java type
     * @return the appropriate Tag type or null
     */
    @Nullable
    public static Class<? extends Tag> getRequiredTagType(Class<?> clazz) {
        if (clazz == String.class) {
            return StringTag.class;
        } else if (clazz == int.class) {
            return IntTag.class;
        } else if (clazz == short.class) {
            return ShortTag.class;
        } else if (clazz == byte.class) {
            return ByteTag.class;
        } else if (clazz == byte[].class) {
            return ByteArrayTag.class;
        } else if (clazz == double.class) {
            return DoubleTag.class;
        } else if (clazz == float.class) {
            return FloatTag.class;
        } else if (clazz == int[].class) {
            return IntArrayTag.class;
        } else if (clazz == long.class) {
            return LongTag.class;
        }
        return null;
    }

}
