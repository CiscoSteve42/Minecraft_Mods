package corgimod.entity;

import corgimod.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CorgiEntity extends TameableEntity {
    public CorgiEntity(EntityType<? extends TameableEntity> type, World world) {
        super(type, world);
    }

    @Override
    public World method_48926() {
        return getWorld();
    }

    public static DefaultAttributeContainer.Builder createCorgiAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected void initGoals() {
        goalSelector.add(1, new SwimGoal(this));
        goalSelector.add(2, new SitGoal(this));
        goalSelector.add(5, new MeleeAttackGoal(this, 1.0, true));
        goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0f, 2.0f, false));
        goalSelector.add(7, new AnimalMateGoal(this, 1.0));
        goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        goalSelector.add(10, new LookAroundGoal(this));

        targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        targetSelector.add(2, new AttackWithOwnerGoal(this));
        targetSelector.add(3, new RevengeGoal(this).setGroupRevenge());
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(tamed ? 20.0 : 8.0);
        getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(tamed ? 4.0 : 2.0);
        if (tamed) setHealth(20.0f);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        Item item = stack.getItem();
        return item.isFood() && item.getFoodComponent().isMeat();
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (getWorld().isClient) {
            boolean useful = isTamed()
                    ? isOwner(player) || isBreedingItem(stack)
                    : stack.isOf(Items.BONE);
            return useful ? ActionResult.CONSUME : ActionResult.PASS;
        }

        if (isTamed()) {
            // Heal with meat
            if (isBreedingItem(stack) && getHealth() < getMaxHealth()) {
                if (!player.getAbilities().creativeMode) stack.decrement(1);
                heal(stack.getItem().getFoodComponent().getHunger());
                return ActionResult.SUCCESS;
            }
            // Vanilla handles feeding for breeding (love mode)
            ActionResult result = super.interactMob(player, hand);
            if (!result.isAccepted() && isOwner(player)) {
                setSitting(!isSitting());
                jumping = false;
                navigation.stop();
                setTarget(null);
                return ActionResult.SUCCESS;
            }
            return result;
        }

        // Taming with bones
        if (stack.isOf(Items.BONE)) {
            if (!player.getAbilities().creativeMode) stack.decrement(1);
            if (random.nextInt(3) == 0) {
                setOwner(player);
                navigation.stop();
                setTarget(null);
                setSitting(true);
                getWorld().sendEntityStatus(this, (byte) 7); // hearts
            } else {
                getWorld().sendEntityStatus(this, (byte) 6); // smoke
            }
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        if (other == this || !isTamed() || isInSittingPose()) return false;
        if (!(other instanceof CorgiEntity corgi) || !corgi.isTamed()) return false;
        if (corgi.isInSittingPose()) return false;
        return isInLove() && corgi.isInLove();
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity mate) {
        CorgiEntity baby = ModEntities.CORGI.create(world);
        if (baby != null && getOwnerUuid() != null) {
            baby.setOwnerUuid(getOwnerUuid());
            baby.setTamed(true);
        }
        return baby;
    }

    // Borrowing wolf sounds for now
    @Override protected SoundEvent getAmbientSound() { return SoundEvents.ENTITY_WOLF_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ENTITY_WOLF_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_WOLF_DEATH; }
}
