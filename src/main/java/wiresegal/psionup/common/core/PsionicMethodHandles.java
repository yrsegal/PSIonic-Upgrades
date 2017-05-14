package wiresegal.psionup.common.core;

import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Level;
import vazkii.psi.common.block.tile.container.slot.SlotBullet;
import wiresegal.psionup.common.PsionicUpgrades;
import wiresegal.psionup.common.lib.LibObfuscation;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static java.lang.invoke.MethodHandles.publicLookup;
import static kotlin.text.Typography.bullet;

/**
 * @author WireSegal
 *         Created at 10:43 PM on 7/8/16.
 */
public class PsionicMethodHandles {
    @Nonnull
    private static final MethodHandle socketSlotGetter, calculateInputStrength;

    static {
        try {
            Field f = ReflectionHelper.findField(SlotBullet.class, "socketSlot");
            socketSlotGetter = publicLookup().unreflectGetter(f);
            Method m = ReflectionHelper.findMethod(BlockRedstoneComparator.class, LibObfuscation.BLOCKREDSTONECOMPARATOR_CALCULATE_INPUT_STRENGTH, LibObfuscation.BLOCKREDSTONECOMPARATOR_CALCULATE_INPUT_STRENGTH_OBF, World.class, BlockPos.class, IBlockState.class);
            calculateInputStrength = publicLookup().unreflect(m);
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

    public static int calculateInputStrength(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing) {
        try {
            return (int) calculateInputStrength.invokeExact(Blocks.POWERED_COMPARATOR, world, pos, Blocks.POWERED_COMPARATOR.getDefaultState().withProperty(BlockHorizontal.FACING, facing.getFrontOffsetY() == 0 ? facing : EnumFacing.NORTH));
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
