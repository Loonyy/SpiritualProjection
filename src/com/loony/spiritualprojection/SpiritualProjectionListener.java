package com.loony.spiritualprojection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.loony.spiritualprojection.multiability.SpiritualDrain;
import com.loony.spiritualprojection.multiability.SpiritualProjection;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.util.MultiAbilityManager;
import com.projectkorra.projectkorra.event.AbilityProgressEvent;
import com.projectkorra.projectkorra.event.AbilityStartEvent;

public class SpiritualProjectionListener implements Listener {

	// This doesn't work because MultiAbilities are shit
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerSlotChange(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();

		SpiritualProjection spiritualProjection = CoreAbility.getAbility(player, SpiritualProjection.class);

		if (spiritualProjection != null) {
			spiritualProjection.displayBoundMsg(event.getNewSlot() + 1);
			return;

		}

	}

	// Checks if player is spiritually drained
	@EventHandler
	public void onAbility(AbilityProgressEvent event) {
		Player player = event.getAbility().getPlayer();

		if (SpiritualDrain.drainedEntities.contains(player)) {
			event.getAbility().remove();
		}
	}

	// Checks if player is spiritually drained
	@EventHandler
	public void onAbilityStart(AbilityStartEvent event) {
		Player player = event.getAbility().getPlayer();

		if (SpiritualDrain.drainedEntities.contains(player)) {

			event.setCancelled(true);
		}

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		String abil = bPlayer.getBoundAbilityName();

		if (MultiAbilityManager.hasMultiAbilityBound(player)) {
			abil = MultiAbilityManager.getBoundMultiAbility(player);
			if (abil.equalsIgnoreCase("SpiritualProjection")) {
				SpiritualProjection.bar.remove(player.getName());
				SpiritualProjection.powerAmount.remove(player.getName());
				SpiritualProjection.bossBar.removePlayer(player);
			}
		}
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		if (event.getAction() == Action.LEFT_CLICK_AIR) {
			if (bPlayer != null && bPlayer.canBend(CoreAbility.getAbility("SpiritualProjection"))) {
				new SpiritualProjection(player);

			}

		}

	}

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Player player = event.getPlayer();
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		String abil = bPlayer.getBoundAbilityName();

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
