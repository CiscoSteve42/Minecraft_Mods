package corgimod.client;

import corgimod.entity.CorgiEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;

public class CorgiModel extends QuadrupedEntityModel<CorgiEntity> {
    public CorgiModel(ModelPart root) {
        super(root, false, 4.0f, 4.0f, 2.0f, 2.0f, 24);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        // Long body, with the tail attached to it
        ModelPartData body = root.addChild("body",
                ModelPartBuilder.create().uv(0, 10).cuboid(-3f, -2.5f, -6f, 6f, 5f, 12f),
                ModelTransform.pivot(0f, 18.5f, 0f));
        body.addChild("tail",
                ModelPartBuilder.create().uv(0, 28).cuboid(-1f, -1f, 0f, 2f, 2f, 2f),
                ModelTransform.pivot(0f, -2f, 6f));

        // Head with snout and big ears
        ModelPartData head = root.addChild("head",
                ModelPartBuilder.create().uv(0, 0).cuboid(-2.5f, -2.5f, -4f, 5f, 5f, 4f),
                ModelTransform.pivot(0f, 17f, -6f));
        head.addChild("snout",
                ModelPartBuilder.create().uv(20, 0).cuboid(-1.5f, 0f, -7f, 3f, 2f, 3f),
                ModelTransform.NONE);
        head.addChild("right_ear",
                ModelPartBuilder.create().uv(34, 0).cuboid(-2.5f, -5f, -1f, 2f, 3f, 1f),
                ModelTransform.NONE);
        head.addChild("left_ear",
                ModelPartBuilder.create().uv(34, 5).cuboid(0.5f, -5f, -1f, 2f, 3f, 1f),
                ModelTransform.NONE);

        // Short legs
        ModelPartBuilder leg = ModelPartBuilder.create().uv(0, 34).cuboid(-1f, 0f, -1f, 2f, 3f, 2f);
        root.addChild("right_front_leg", leg, ModelTransform.pivot(-2f, 21f, -4f));
        root.addChild("left_front_leg",  leg, ModelTransform.pivot( 2f, 21f, -4f));
        root.addChild("right_hind_leg",  leg, ModelTransform.pivot(-2f, 21f,  4f));
        root.addChild("left_hind_leg",   leg, ModelTransform.pivot( 2f, 21f,  4f));

        return TexturedModelData.of(data, 64, 64);
    }
}
