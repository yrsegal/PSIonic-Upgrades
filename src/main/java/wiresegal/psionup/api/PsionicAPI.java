package wiresegal.psionup.api;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class PsionicAPI {
    private static ArrayList<TrickRecipe> trickRecipes = new ArrayList<>();

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
