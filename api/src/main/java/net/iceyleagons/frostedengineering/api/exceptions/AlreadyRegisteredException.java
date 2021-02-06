package net.iceyleagons.frostedengineering.api.exceptions;

/**
 * @author TOTHTOMI
 */
public class AlreadyRegisteredException extends Exception{

    public AlreadyRegisteredException(Object object) {
        super("An instance of " + object.getClass().getName() + " has been already registered!");
    }

}
