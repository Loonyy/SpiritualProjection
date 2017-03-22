package com.loony.spiritualprojection.multiability;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.SpiritualAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class AstralAttack extends SpiritualAbility implements AddonAbility {

	ArrayList<Integer> stands = new ArrayList<Integer>();
	private boolean spawnStand;
	private ArmorStand stand;
	private Location standLocation;

	public AstralAttack(Player player) {
		super(player);

		start();

	}

	@Override
	public void progress() {

		Location loc = player.getEyeLocation();
		if (!spawnStand) {
			Stand(loc);
			stand.setVelocity(player.getEyeLocation().getDirection().clone().normalize().multiply(5));

		}
		ParticleEffect.CLOUD.display(standLocation, 0, 0, 0, 0, 10);

	}

	public ArmorStand Stand(Location loc) {
		this.stand = loc.getWorld().spawn(loc, ArmorStand.class);
		stand.setGravity(true);
		stand.setSmall(true);
		ItemStack helm = new ItemStack(Material.LEATHER_HELMET, 1);
		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
		
		LeatherArmorMeta metaHead = (LeatherArmorMeta) helm.getItemMeta();
		LeatherArmorMeta metaChest = (LeatherArmorMeta) chest.getItemMeta();
		LeatherArmorMeta metaLegs = (LeatherArmorMeta) legs.getItemMeta();
		LeatherArmorMeta metaBottom = (LeatherArmorMeta) boots.getItemMeta();

		metaHead.setColor(Color.WHITE);
		metaChest.setColor(Color.WHITE);
		metaLegs.setColor(Color.WHITE);
		metaBottom.setColor(Color.WHITE);
		
		helm.setItemMeta(metaHead);
		chest.setItemMeta(metaChest);
		legs.setItemMeta(metaLegs);
		boots.setItemMeta(metaBottom);
		
		stand.setHelmet(helm);
		stand.setChestplate(chest);
		stand.setLeggings(legs);
		stand.setBoots(boots);
		stand.setSmall(false);
		stand.setVisible(false); 
		stand.setMarker(true);
		// stand.setCustomName("§5Rewards");
		stand.setCustomNameVisible(false);
		stands.add(stand.getEntityId());
		this.spawnStand = true;
		this.standLocation = stand.getLocation();

		return stand;
	}

	public void removeStand() {
		for (World world : Bukkit.getServer().getWorlds()) {
			for (org.bukkit.entity.Entity entity : world.getEntities()) {
				if (stands.contains(entity.getEntityId())) {
					entity.remove();
				}
			}
		}
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

		return "AstralAttack";
	}

	@Override
	public boolean isHarmlessAbility() {

		return false;
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
