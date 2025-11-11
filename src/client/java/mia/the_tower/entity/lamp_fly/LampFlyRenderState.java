package mia.the_tower.entity.lamp_fly;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.AnimationState;
import net.minecraft.util.math.Direction;

public class LampFlyRenderState extends LivingEntityRenderState{
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState flyAnimationState = new AnimationState();
    public boolean airborne;
    //public VoidMothVariant variant; //for when I introduce a variant
}
