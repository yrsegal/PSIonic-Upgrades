package wiresegal.psionup.api;

import kotlin.Pair;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.function.Function;

public class PsionicAPI {
    private static ArrayList<TrickRecipe> trickRecipes = new ArrayList<>();

    static Function<Pair<BlockProperties, EnumFacing>, Integer> internalPropertyComparator;

    public static void setInternalPropertyComparator(Function<Pair<BlockProperties, EnumFacing>, Integer> f) {
        internalPropertyComparator = f;
    }

    public static TrickRecipe addTrickRecipe(TrickRecipe trickRecipe) {
        trickRecipes.add(trickRecipe);
        return trickRecipe;
    }

    public static TrickRecipe addTrickRecipe(String trickName, ItemStack input, ItemStack output, ItemStack cad) {
        return addTrickRecipe(new TrickRecipe(trickName, input, output, cad));
    }

    public static ArrayList<TrickRecipe> getTrickRecipes() {
        return trickRecipes;
    }
}
