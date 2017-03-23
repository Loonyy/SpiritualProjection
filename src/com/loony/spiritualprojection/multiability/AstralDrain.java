package com.loony.spiritualprojection.multiability;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;

public class AstralDrain extends AirAbility implements AddonAbility {
	
	private long cooldown;
	private Location location;

	public AstralDrain(Player player) {
		super(player);
		
		setFields();
		
	}
	
	public void setFields() {
		this.cooldown = 0;
		this.location = player.getLocation();
	}

	@Override
	public long getCooldown() {
		
		return cooldown;
	}

	@Override
	public Location getLocation() {
		
		return location;
	}

	@Override
	public String getName() {
		
		return "AstralDrain";
	}

	@Override
	public boolean isHarmlessAbility() {
		
		return false;
	}

	@Override
	public boolean isSneakAbility() {
		
		return true;
	}

	@Override
	public void progress() {
	}

	@Override
	public String getAuthor() {
		
		return null;
	}

	@Override
	public String getVersion() {
		
		return null;
	}

	@Override
	public void load() {
	}

	@Override
	public void stop() {
	}

}
