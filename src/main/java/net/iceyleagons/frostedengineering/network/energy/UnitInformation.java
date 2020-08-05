package net.iceyleagons.frostedengineering.network.energy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UnitInformation {

    public String name() default "Energy Unit";
    public String author() default "IceyLeagons";
    public String description() default "";
    public String usedFor() default "";

}
