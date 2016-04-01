package wiresegal.psionup.api.enabling;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceTrick;

import java.util.Collection;
import java.util.List;


public abstract class PieceComponentTrick extends PieceTrick {

    public PieceComponentTrick(Spell spell) {
        super(spell);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {

        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        if (cad == null || !(cad.getItem() instanceof ICAD))
            throw new SpellRuntimeException(SpellRuntimeException.NO_CAD);
        ICAD item = (ICAD) cad.getItem();

        boolean flag = false;

        for (EnumCADComponent type : EnumCADComponent.values()) {
            ItemStack component = item.getComponentInSlot(cad, type);
            if (component.getItem() instanceof ITrickEnablerComponent) {
                ITrickEnablerComponent compItem = (ITrickEnablerComponent) component.getItem();
                flag = true;
                if (compItem.enablePiece(context.caster, component, cad, context, spell, x, y))
                    return executeIfAllowed(context);
            }
        }

        if (flag)
            throw new SpellRuntimeException("psionup.spellerror.trickdisabled");
        throw new SpellRuntimeException("psionup.spellerror.tricknotenabled");
    }

    public abstract Object executeIfAllowed(SpellContext context) throws SpellRuntimeException;

    public abstract String[] requiredObjects();

    @Override
    public void addToTooltipAfterShift(List<String> tooltip) {
        TooltipHelper.addToTooltip(tooltip, TextFormatting.GRAY + "%s", this.getUnlocalizedDesc());

        for (String obj : requiredObjects()) TooltipHelper.addToTooltip(tooltip, "psionup.spelldesc.requires", obj);

        TooltipHelper.addToTooltip(tooltip, "");
        String eval = this.getEvaluationTypeString();
        TooltipHelper.addToTooltip(tooltip, "<- " + TextFormatting.GOLD + eval);

        for (SpellParam param : this.paramSides.keySet()) {
            String pName = I18n.translateToLocal(param.name);
            String pEval = param.getRequiredTypeString();
            TooltipHelper.addToTooltip(tooltip, (param.canDisable ? "[->] " : " ->  ") + TextFormatting.YELLOW + pName + " [" + pEval + "]");
        }
    }
}
