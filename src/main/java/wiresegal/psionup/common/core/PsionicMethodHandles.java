package wiresegal.psionup.common.core;

import com.google.common.base.Throwables;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Level;
import vazkii.psi.common.block.tile.container.slot.SlotBullet;
import wiresegal.psionup.common.PsionicUpgrades;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author WireSegal
 *         Created at 10:43 PM on 7/8/16.
 */
public class PsionicMethodHandles {
    @Nonnull
    private static MethodHandle socketSlotGetter;

    static {
        try {
            Field f = ReflectionHelper.findField(SlotBullet.class, "socketSlot");
            socketSlotGetter = MethodHandles.publicLookup().unreflectGetter(f);
        } catch (Throwable t) {
            PsionicUpgrades.Companion.getLOGGER().log(Level.ERROR, "Couldn't initialize methodhandles! Things will be broken!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    public static int getSocketSlot(@Nonnull SlotBullet bullet) {
        try {
            return (int) socketSlotGetter.invokeExact(bullet);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    private static RuntimeException propagate(Throwable t) {
        PsionicUpgrades.Companion.getLOGGER().log(Level.ERROR, "Methodhandle failed!");
        t.printStackTrace();
        return Throwables.propagate(t);
    }
}
