package corgimod;

import corgimod.entity.CorgiEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

public class ModEntities {
    public static final EntityType<CorgiEntity> CORGI = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(CorgiMod.MOD_ID, "corgi"),
            EntityType.Builder.create(CorgiEntity::new, SpawnGroup.CREATURE)
                    .setDimensions(0.5f, 0.6f)
                    .maxTrackingRange(10)
                    .build("corgi"));

    public static final Item CORGI_SPAWN_EGG = Registry.register(
            Registries.ITEM,
            new Identifier(CorgiMod.MOD_ID, "corgi_spawn_egg"),
            new SpawnEggItem(CORGI, 0xD98B3A, 0xFFFFFF, new FabricItemSettings()));

    public static void register() {
        FabricDefaultAttributeRegistry.register(CORGI, CorgiEntity.createCorgiAttributes());

        SpawnRestriction.register(CORGI, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);

        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.PLAINS, BiomeKeys.MEADOW, BiomeKeys.FOREST),
                SpawnGroup.CREATURE, CORGI, 8, 2, 4);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS)
                .register(entries -> entries.add(CORGI_SPAWN_EGG));
    }
}
