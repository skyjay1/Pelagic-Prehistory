package pelagic_prehistory.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import pelagic_prehistory.entity.goal.BreachGoal;
import pelagic_prehistory.entity.goal.FloppingGoal;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Plesiosaurus extends WaterAnimal implements IAnimatable {

    // GECKOLIB //
    protected AnimationFactory instanceCache = GeckoLibUtil.createFactory(this);
    protected static final AnimationBuilder ANIM_SWIM = new AnimationBuilder().addAnimation("swim");

    public Plesiosaurus(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 30, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 15);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.30D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    //// METHODS ////

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new FloppingGoal(this, 0.4F, 2));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 0.9D, 80));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new BreachGoal(this, 10));
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        final WaterBoundPathNavigation nav = new WaterBoundPathNavigation(this, level);
        nav.allowBreaching = true;
        ((SwimNodeEvaluator) nav.getNodeEvaluator()).allowBreaching = true;
        return nav;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.485F;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().inflate(1.25F, 0.25F, 1.25F);
    }

    @Override
    public int getMaxHeadXRot() {
        return 20;
    }

    @Override
    public int getMaxHeadYRot() {
        return 20;
    }

    //// SOUNDS ////

    @Override
    public int getAmbientSoundInterval() {
        return 160;
    }

    @Override
    protected float getSoundVolume() {
        final float factor = isInWaterOrBubble() ? 0.5F : 0.32F;
        return super.getSoundVolume() * factor;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return super.getAmbientSound(); //PPRegistry.SoundReg.PLESIOSAURUS_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return super.getHurtSound(pDamageSource); // PPRegistry.SoundReg.PLESIOSAURUS_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return super.getDeathSound(); //PPRegistry.SoundReg.PLESIOSAURUS_DEATH.get();
    }

    //// NBT ////

    @Override
    public void readAdditionalSaveData(final CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }

    @Override
    public void addAdditionalSaveData(final CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    //// GECKOLIB ////

    private PlayState handleAnimation(AnimationEvent<Plesiosaurus> event) {
        event.getController().setAnimation(ANIM_SWIM);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 2F, this::handleAnimation));
    }

    @Override
    public AnimationFactory getFactory() {
        return instanceCache;
    }

    //// GOALS ////

}
