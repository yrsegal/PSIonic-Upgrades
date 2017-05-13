package wiresegal.psionup.api;

import kotlin.Pair;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 *         Created at 4:23 PM on 5/13/17.
 */
public class BlockProperties {
    @NotNull
    private final IBlockState state;
    @NotNull
    private final BlockPos pos;
    @NotNull
    private final World world;

    @NotNull
    public IBlockState getState() {
        return state;
    }

    @NotNull
    public BlockPos getPos() {
        return pos;
    }

    @NotNull
    public World getWorld() {
        return world;
    }

    public BlockProperties(@NotNull IBlockState state, @NotNull BlockPos pos, @NotNull World world) {
        this.state = state;
        this.pos = pos;
        this.world = world;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BlockProperties && ((BlockProperties) obj).state.equals(state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s\n@%d %d %d", state.toString(), pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean sideSolid(@NotNull EnumFacing facing) {
        return state.isSideSolid(world, pos, facing);
    }

    public int comparatorOutput(@NotNull EnumFacing facing) {
        return PsionicAPI.internalPropertyComparator.apply(new Pair<>(this, facing));
    }

    public float getHardness() {
        return state.getBlockHardness(world, pos);
    }

    public float getLight() {
        return state.getLightValue(world, pos);
    }
}
