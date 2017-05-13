
package wiresegal.psionup.api;

import kotlin.Pair;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.param.ParamSpecific;

/**
 * @author WireSegal
 *         Created at 4:23 PM on 5/13/17.
 */
public class ParamBlockProperties extends ParamSpecific {
    public ParamBlockProperties(String name, int color, boolean canDisable) {
        super(name, color, canDisable, false);
    }

    @Override
    protected Class<?> getRequiredType() {
        return BlockProperties.class;
    }
}
