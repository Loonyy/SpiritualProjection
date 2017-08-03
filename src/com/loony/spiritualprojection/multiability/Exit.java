package com.loony.spiritualprojection.multiability;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.ability.util.MultiAbilityManager;

public class Exit extends AirAbility implements AddonAbility {

	public Exit(Player player) {
		super(player);
		start();
	}

	@Override
	public void progress() {

		MultiAbilityManager.unbindMultiAbility(player);
		remove();

	}

	@Override
	public long getCooldown() {

		return 0;
	}

	@Override
	public Location getLocation() {

		return null;
	}

	@Override
	public String getName() {

		return "Exit";
	}

	@Override
	public void remove() {
		SpiritualProjection.bar.remove(player.getName());
		SpiritualProjection.powerAmount.remove(player.getName());
		SpiritualProjection.bossBar.removePlayer(player);
		if (Spirit.getAbility(player, Spirit.class) != null) {
			Spirit.getAbility(player, Spirit.class).remove();
		}
		super.remove();
	}

	@Override
	public boolean isHarmlessAbility() {

		return true;
	}

	@Override
	public boolean isSneakAbility() {

		return false;
	}

	@Override
	public boolean isHiddenAbility() {
		return true;
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
