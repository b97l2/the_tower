package mia.the_tower.entity.void_moth;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.AnimationState;
import net.minecraft.util.math.Direction;

public class VoidMothRenderState extends LivingEntityRenderState{
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState flyAnimationState = new AnimationState();
    public boolean airborne;
    public boolean perched;
    public Direction perchFace = Direction.UP;
    //public VoidMothVariant variant; //for when I introduce a variant
}
