package corgimod.client;

import corgimod.CorgiMod;
import corgimod.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class CorgiModClient implements ClientModInitializer {
    public static final EntityModelLayer CORGI_LAYER =
            new EntityModelLayer(new Identifier(CorgiMod.MOD_ID, "corgi"), "main");

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(CORGI_LAYER, CorgiModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.CORGI, CorgiRenderer::new);
    }
}
