package net.iceyleagons.frostedengineering.api.other;

import net.iceyleagons.frostedengineering.api.exceptions.NotConstructableException;

/**
 * All constant values and calculations regarding electricity are contained here.
 *
 * @author TOTHTOMI
 */
public strictfp class PowerCalculation {

    /**
     * Calculates the amperage with Ohm's law
     *
     * @param resistance the resistance of the object
     * @param voltage the voltage given
     * @return the amperage
     */
    public static double calculateAmperage(double resistance, double voltage) {
        return voltage/resistance;
    }

    /**
     * Calculates the required amperage for a machine from the voltage supplied and the
     * power of the machine
     *
     * @param power the power of the machine
     * @param voltage the voltage supplied
     * @return the amperage
     */
    public static double calculateRequiredAmperage(double power, double voltage) {
        return  power/voltage;
    }

    public PowerCalculation() throws NotConstructableException {
        throw new NotConstructableException(getClass());
    }
}
