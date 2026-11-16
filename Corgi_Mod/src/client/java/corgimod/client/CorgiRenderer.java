package corgimod.client;

import corgimod.CorgiMod;
import corgimod.entity.CorgiEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class CorgiRenderer extends MobEntityRenderer<CorgiEntity, CorgiModel> {
    private static final Identifier TEXTURE =
            new Identifier(CorgiMod.MOD_ID, "textures/entity/corgi.png");

    public CorgiRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CorgiModel(ctx.getPart(CorgiModClient.CORGI_LAYER)), 0.35f);
    }

    @Override
    public Identifier getTexture(CorgiEntity entity) {
        return TEXTURE;
    }
}
