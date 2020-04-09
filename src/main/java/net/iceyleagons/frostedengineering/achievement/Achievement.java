/**
 * 
 */
package net.iceyleagons.frostedengineering.achievement;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
/**
 * @author TOTHT
 *
 */
public @interface Achievement {
	
	String name();
	String description();
	AchievementType type();

}
