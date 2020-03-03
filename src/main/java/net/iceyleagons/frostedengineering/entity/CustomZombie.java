package net.iceyleagons.frostedengineering.entity;

import net.minecraft.server.v1_14_R1.DamageSource;
import net.minecraft.server.v1_14_R1.EntityMonster;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EntityZombie;
import net.minecraft.server.v1_14_R1.PathfinderGoalFollowEntity;
import net.minecraft.server.v1_14_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_14_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_14_R1.SoundEffect;
import net.minecraft.server.v1_14_R1.SoundEffects;
import net.minecraft.server.v1_14_R1.World;

public class CustomZombie extends EntityZombie {
	
	 public CustomZombie(EntityTypes<? extends EntityMonster> entitytypes, World world) {
	        super(EntityTypes.ZOMBIE, world);
	    	this.goalSelector.a(1, new PathfinderGoalFollowEntity(this, 10, 10, 10));
	    	this.goalSelector.a(0, new AttackNearestPlayer(this,4.0D));
	    	this.goalSelector.a(2, new PathfinderGoalRandomLookaround(this));
	    	this.goalSelector.a(2, new PathfinderGoalRandomStroll(this,1.0D,2));
	    }
	 
	    @Override
	    protected SoundEffect getSoundAmbient() {
	        return SoundEffects.ENTITY_HUSK_AMBIENT;
	    }
	 
	    @Override
	    protected SoundEffect getSoundHurt(DamageSource damagesource) {
	        return SoundEffects.ENTITY_HUSK_HURT;
	    }
	 
	    @Override
	    protected SoundEffect getSoundDeath() {
	        return SoundEffects.ENTITY_HUSK_DEATH;
	    }
	 
	    @Override
	    protected boolean playStepSound() {
	        return false;
	    }
	 
	    @Override
	    protected void dropDeathLoot(DamageSource damagesource, int i, boolean flag) {
	        return;
	    }
	 
	    @Override
	    protected void initPathfinder() {
	    }

}
