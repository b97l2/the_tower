package mia.the_tower.initialisation;

import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class CustomEquipmentAssets {
    //this is for the custom armour/wearable equipment model
    //to make a custom piece of armour/equipment, you need a properly registered item in item_init
    //for some effects you need a custom item class
    //you need a line here
    //you need a json in equipment directory (next to blockstates)
    //lastly you need the texture in the correct entity texture directory

    public static final RegistryKey<EquipmentAsset> CIRCLET_OF_GLUT =
            RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of("the_tower", "circlet_of_glut"));
}
