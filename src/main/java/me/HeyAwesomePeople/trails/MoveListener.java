package me.HeyAwesomePeople.trails;

import java.util.UUID;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class MoveListener implements Listener {
	private SimpleTrails plugin = SimpleTrails.instance;

	@EventHandler
	public void onJoinEvent(PlayerJoinEvent e) {
		if (plugin.getConfig().contains("players." + e.getPlayer().getUniqueId())) {
			plugin.loadPlayerTrail(e.getPlayer());
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		final Player p = e.getPlayer();
		final UUID id = p.getUniqueId();
		if (!plugin.playerTrails.containsKey(id)) return;
		plugin.trailmanager.displayTrail(p, plugin.playerTrails.get(id));
		return;
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Item newitem = event.getItem();
		if (newitem.hasMetadata("nopickup")) {
			event.setCancelled(true);
		}
	}

}
