package net.iceyleagons.frostedengineering.api.exceptions;

/**
 * @author TOTHTOMI
 */
public class NotConstructableException extends Exception {

    public NotConstructableException(Class<?> clazz) {
        super("Class " + clazz.getName() + " has constructors disabled, only static access allowed!");
    }

}
