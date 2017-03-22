package com.loony.spiritualprojection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.loony.spiritualprojection.multiability.Meditate;
import com.loony.spiritualprojection.multiability.SpiritualProjection;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.util.MultiAbilityManager;

public class SpiritualProjectionListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerSlotChange(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();

		SpiritualProjection spiritualProjection = CoreAbility.getAbility(player, SpiritualProjection.class);
		if (spiritualProjection != null) {
			spiritualProjection.displayBoundMsg(event.getNewSlot() + 1);
			return;
		}
	}

	// @EventHandler
	// public void onSwing(PlayerAnimationEvent event) {
	// Player player = event.getPlayer();
	// BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
	// String abil = bPlayer.getBoundAbilityName();
	//
	// if (MultiAbilityManager.hasMultiAbilityBound(player)) {
	// abil = MultiAbilityManager.getBoundMultiAbility(player);
	// if (abil.equalsIgnoreCase("SpiritualProjection")) {
	//
	// if (abil.equalsIgnoreCase("SpiritualProjection")) {
	// new SpiritualProjection(player);
	// }
	//
	// }
	// }
	//
	// }

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		String abil = bPlayer.getBoundAbilityName();

		if (bPlayer != null && bPlayer.canBend(CoreAbility.getAbility("SpiritualProjection"))) {
			new SpiritualProjection(player);

		}

		if (MultiAbilityManager.hasMultiAbilityBound(player)) {
			abil = MultiAbilityManager.getBoundMultiAbility(player);
			if (abil.equalsIgnoreCase("SpiritualProjection")) {

				if (abil.equalsIgnoreCase("SpiritualProjection")) {
					new SpiritualProjection(player);
				}

			}
		}
	}

}
