package spleef.eventhandler;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import spleef.Spleef;
import spleef.arena.Arena;

public class SnowballHandler implements Listener {

	private final Spleef plugin;
	private Arena arena;

	public SnowballHandler(Spleef plugin, Arena arena) {
		this.plugin = plugin;
		this.arena = arena;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Give the snowball an impact effect.
	 *
	 * @param Projectile hit event.
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSnowballHit(ProjectileHitEvent e) {
		Projectile projectile = e.getEntity();
		if (!(projectile instanceof Snowball) || !(projectile.getShooter() instanceof Player)) {
			return;
		}
		Player shooter = (Player) projectile.getShooter();
		Arena arena = plugin.amanager.getPlayerArena(shooter.getName());
		if (arena == null) {
			return;
		}
		if (!arena.getStatusManager().isArenaRunning()) {
			return;
		}
		if (plugin.getConfig().getBoolean("items.snowball.breakblocks")) {
			Block block = e.getHitBlock();
			if (block != null && block.getType() == Material.SNOW_BLOCK) {
				arena.getStructureManager().getGameZone().handleBlockBreak(block);
				block.breakNaturally();
				return;
			}
		}
		if (e.getHitEntity() == null || e.getHitEntity().getType() != EntityType.PLAYER) {
			return;
		}
		Player player = (Player) e.getHitEntity();
		double knockback = plugin.getConfig().getDouble("items.snowball.knockback", 1.5);
		player.damage(0.5, projectile);
		player.setVelocity(projectile.getVelocity().multiply(knockback));
	}

	/**
	 * On launching the snowball, replace the item in the hotbar.
	 *
	 * @param Projectile launch event.
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSnowballThrow(ProjectileLaunchEvent e) {
		if (!(e.getEntity() instanceof Snowball) || !(e.getEntity().getShooter() instanceof Player)) {
			return;
		}

		Player player = (Player) e.getEntity().getShooter();
		Arena arena = plugin.amanager.getPlayerArena(player.getName());
		if (arena == null) {
			return;
		}
		if (!arena.getStatusManager().isArenaRunning()) {
			return;
		}
		if (!plugin.getConfig().getBoolean("items.snowball.use")) {
			return;
		}
		int slot = plugin.getConfig().getInt("items.snowball.slot", 1);
		plugin.getServer().getScheduler().runTaskLater(plugin, () ->
				player.getInventory().setItem(slot, new ItemStack(Material.SNOWBALL)), 1L);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSnowballDrop(ItemSpawnEvent event) {
		if (!(event.getEntity() instanceof Item)) {
			return;
		}
		if (event.getEntity().getItemStack().getType() != Material.SNOWBALL) {
			return;
		}
		if (plugin.getConfig().getBoolean("items.snowball.allowpickup")) {
			return;
		}
		if (arena.getStructureManager().isInArenaBounds(event.getLocation())) {
			event.setCancelled(true);
		}
	}

	/**
	 * If set, limit the number of snowballs a player can hold.
	 *
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSnowballPickup(EntityPickupItemEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		if (event.getItem().getItemStack().getType() != Material.SNOWBALL) {
			return;
		}
		Player player = (Player) event.getEntity();
		Arena arena = plugin.amanager.getPlayerArena(player.getName());
		if (arena == null) {
			return;
		}
		if (!arena.getStatusManager().isArenaRunning()) {
			return;
		}
		int maxpickup = plugin.getConfig().getInt("items.snowball.maxpickup");
		if (maxpickup <= 0) {
			return;
		}
		int holding = 0;
		for(ItemStack is : player.getInventory().all(Material.SNOWBALL).values()) {
			holding += is.getAmount();
		}
		if (holding >= maxpickup) {
			event.setCancelled(true);
			return;
		}

		int drops = event.getItem().getItemStack().getAmount();
		if ((holding + drops)  > maxpickup) {
			event.getItem().getItemStack().setAmount(drops - (maxpickup - holding));
			event.setCancelled(true);
			ItemStack is = new ItemStack(Material.SNOWBALL, maxpickup - holding);
			player.getInventory().addItem(is);
		}
	}
}
