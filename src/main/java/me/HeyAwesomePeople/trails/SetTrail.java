package me.HeyAwesomePeople.trails;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

public class SetTrail {
	private SimpleTrails plugin = SimpleTrails.instance;
	
	public void setTrail(Player p, String trail) {
		if (trail.equalsIgnoreCase("clear")) {
			removeTrails(p);
		} else {
			trail(p, trail);
		}
	}

	public void trail(Player p, String trail) {
		plugin.playerTrails.put(p.getUniqueId(), trail);
		p.sendMessage(ChatColor.RED + "[SmokeTrails] " + ChatColor.DARK_AQUA
				+ "You now have a " + ChatColor.RED + trail
				+ ChatColor.DARK_AQUA + " trial!");
	}
	
	
	public void removeTrails(Player p) {
		plugin.playerTrails.remove(p.getUniqueId());
		p.sendMessage(ChatColor.RED + "[SmokeTrails] " + ChatColor.DARK_AQUA
				+ "Trails cleared!");
		plugin.getConfig().set("players." + p.getUniqueId().toString(), null);
		plugin.saveConfig();
		return;
	}

}
