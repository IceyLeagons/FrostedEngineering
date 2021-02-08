package net.iceyleagons.computercraft.lua.robot.components;

import lombok.SneakyThrows;
import net.iceyleagons.computercraft.lua.library.LibraryBuilder;
import net.iceyleagons.computercraft.lua.robot.Component;
import net.iceyleagons.computercraft.lua.robot.Robot;
import net.iceyleagons.icicle.location.RayCast;
import org.bukkit.entity.LivingEntity;
import org.luaj.vm2.LuaValue;

import java.util.concurrent.CompletableFuture;

/**
 * @author TOTHTOMI
 */
public class MeleeComponent extends Component {

    public MeleeComponent(Robot robot) {
        super(robot);
    }

    @Override
    public void addLibraryFields(LibraryBuilder libraryBuilder) {
        libraryBuilder.addFunction("attack", varargs -> generateReturn(attack()));
    }

    @SneakyThrows
    public boolean attack() {
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        doSync(() -> {
            LivingEntity livingEntity = RayCast.shootRay(getEntity(), 0,3);

            if (livingEntity == null) result.complete(false);
            else {
                getEntity().swingMainHand();
                getEntity().attack(livingEntity);
                result.complete(true);
            }
        });
        Thread.sleep(Robot.ACTION_TIMEOUT);
        Boolean res = result.join();
        return res != null ? res : false;
    }

}
