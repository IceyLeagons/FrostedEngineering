package net.iceyleagons.frostedengineering.api.exceptions;

/**
 * @author TOTHTOMI
 */
public class NotRegisteredException extends Exception{

    public NotRegisteredException(Object object) {
        super("An instance of " + object.getClass().getName() + " has not been registered!");
    }

}

