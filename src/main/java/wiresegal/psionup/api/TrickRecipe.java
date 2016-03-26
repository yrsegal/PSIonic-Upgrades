package wiresegal.psionup.api;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellPiece;

/**
 * @author WireSegal
 *         Created at 10:38 AM on 3/26/16.
 */
public class TrickRecipe {

    private String piece;
    private ItemStack input;
    private ItemStack output;
    private ItemStack cad;

    public TrickRecipe(String trick, ItemStack input, ItemStack output, ItemStack CAD) {
        piece = trick;
        this.input = input;
        this.output = output;
        this.cad = CAD;
    }

    public String getPiece() {
        return piece;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getCAD() {
        return cad;
    }
}
