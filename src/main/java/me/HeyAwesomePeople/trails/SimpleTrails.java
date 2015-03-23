package me.HeyAwesomePeople.trails;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleTrails extends JavaPlugin implements CommandExecutor {
	public static SimpleTrails instance;

	// Removed but still have code: fireworks
	public HashMap<UUID, String> playerTrails = new HashMap<UUID, String>();

	public String stRed = ChatColor.RED + "[SimpleTrails] ";
	public String stBlue = ChatColor.BLUE + "[SimpleTrails] ";

	public TrailManager trailmanager;
	public SetTrail settrail;
	public TrailCreator trailcreator;
	public Logs log;

	@Override
	public void onEnable() {
		instance = this;

		if (!new File(this.getDataFolder() + File.separator + "config.yml").exists()) {
			saveDefaultConfig();
		}

		log = new Logs();
		trailmanager = new TrailManager();
		settrail = new SetTrail();
		trailcreator = new TrailCreator();

		getServer().getPluginManager().registerEvents(new MoveListener(), this);

		for (Player p : Bukkit.getOnlinePlayers()) {
			this.loadPlayerTrail(p);
		}
	}

	@Override
	public void onDisable() {
		reloadConfig();
		savePlayerTrails();
	}

	//TODO add permissions for commands
	public boolean onCommand(final CommandSender d, Command cmd,
			String commandLabel, final String[] args) {
		if (commandLabel.equalsIgnoreCase("simpletrails") || commandLabel.equalsIgnoreCase("st")) {
			if (args.length == 0) {
				d.sendMessage(ChatColor.DARK_AQUA + "=== " + ChatColor.RED + "SmokeTrails" + ChatColor.DARK_AQUA + " ===");
				d.sendMessage(ChatColor.AQUA + "/st <trailname> - Select a trail");
				d.sendMessage(ChatColor.AQUA + "/st trails - List trails");
				d.sendMessage(ChatColor.AQUA + "/stc - Clear trails");
				return false;
			} else if (args.length == 1) {
				if (d instanceof Player) {
					Player p = (Player) d;
					if (args[0].equalsIgnoreCase("trails")) {
						p.sendMessage(stRed + ChatColor.DARK_AQUA + "Trails: " + Arrays.toString(trailmanager.trails.keySet().toArray()));
						return false;
					} else {
						if (trailmanager.doesTrailExist(args[0])) {
							if (p.hasPermission(trailmanager.trailPerm.get(args[0])) || p.hasPermission("smoketrail.use.all")) {
								settrail.setTrail(p, args[0].toLowerCase());
								savePlayerTrail(p);
								return false;
							} else {
								p.sendMessage(stRed + "No permission to use this trail! Donate for more trails");
								return false;
							}
						} else {
							p.sendMessage(stRed + "Trail does not exist!");
							return false;
						}
					}
				} else {
					d.sendMessage(stRed + "You cannot preform this command!");
					return false;
				}
			}
		}
		if (commandLabel.equalsIgnoreCase("stc")) {
			if (d instanceof Player) {
				Player p = (Player) d;
				settrail.removeTrails(p);
				return false;
			} else {
				d.sendMessage(stRed + "You cannot preform this command!");
				return false;
			}
		}

		if (commandLabel.equalsIgnoreCase("sta")) {
			if (args.length == 0) {
				d.sendMessage(ChatColor.DARK_AQUA + "=== " + ChatColor.RED + "SmokeTrails Admin" + ChatColor.DARK_AQUA + " ===");
				d.sendMessage(ChatColor.AQUA + "/sta trailinfo <trailname> - Info on a trail");

				d.sendMessage(ChatColor.AQUA + "/sta newtrail <trailname> <permission> - Create new trail");
				d.sendMessage(ChatColor.AQUA + "/sta addeffect <trailname> <effectname> - Add effect to trail");
				d.sendMessage(ChatColor.AQUA + "/sta deltrail <trailname> - Delete a trail");
				d.sendMessage(ChatColor.AQUA + "/sta removeeffect <trailname> <effectname> - Remove effect from a trail");

				d.sendMessage(ChatColor.AQUA + "/sta neweffect <effectname> <range> - Create new effect");
				d.sendMessage(ChatColor.AQUA + "/stc edit <effectname> - View editing commands for effect");
				d.sendMessage(ChatColor.AQUA + "/sta deleffect <effectname> - Delete an effect");

				d.sendMessage(ChatColor.AQUA + "/sta listeffects - View usable effects");
				d.sendMessage(ChatColor.AQUA + "/sta listtrails - View usable trails");

				d.sendMessage(ChatColor.AQUA + "/sta reload - Reloads the config, effects, players trails, and trails");
			} else if (args.length > 0) {
				if (args[0].equalsIgnoreCase("reload")) {
					if (args.length == 1) {
						this.reloadConfig();
						trailmanager = new TrailManager();
						for (Player p : Bukkit.getOnlinePlayers()) {
							this.loadPlayerTrail(p);
						}
						d.sendMessage(stBlue + "Successfully reloaded plugin!");
					} else {
						d.sendMessage(stRed + "Usage: /sta reload");
					}
					return false;
				}
				if (args[0].equalsIgnoreCase("listtrails")) {
					if (args.length == 1) {
						d.sendMessage(stBlue + "Trails: " + Arrays.toString(trailmanager.trails.keySet().toArray()));
					} else {
						d.sendMessage(stRed + "Usage: /sta listtrails");
					}
					return false;
				}
				if (args[0].equalsIgnoreCase("listeffects")) {
					if (args.length == 1) {
						d.sendMessage(stBlue + "Trails: " + Arrays.toString(trailmanager.effects.keySet().toArray()));
					} else {
						d.sendMessage(stRed + "Usage: /sta listeffects");
					}
					return false;
				}
				if (args[0].equalsIgnoreCase("trailinfo")) {
					if (args.length == 2) {
						if (trailmanager.doesTrailExist(args[1])) {
							d.sendMessage(stRed + "TrailInfo : " + args[1]);
							d.sendMessage(stBlue + "Effects(" + trailmanager.trails.get(args[1]).size() + "): " + Arrays.toString(trailmanager.trails.get(args[1]).toArray()));
							d.sendMessage(stBlue + "Permission: " + trailmanager.trailPerm.get(args[1]));
							return false;
						} else {
							d.sendMessage(stRed + "Trail '" + args[1] + "' does not exist!");
						}
					} else {
						d.sendMessage(stRed + "Usage: /sta trailinfo <trailname>");
					}
					return false;
				}
				if (args[0].equalsIgnoreCase("newtrail")) {
					if (args.length == 3) {
						if (trailmanager.doesTrailExist(args[1])) {
							d.sendMessage(stRed + "Trail '" + args[1] + "' already exists!");
							return false;
						}
						if (!args[2].contains(".")) {
							d.sendMessage(stRed + "Please enter in a valid permission! EX: simpletrails.use." + args[1]);
							return false;
						}
						getConfig().set("trails." + args[1] + ".permission", args[2]);
						saveConfig();
						silentReload();
						d.sendMessage(stBlue + "Trail created! Now add an effect to your trail with /sta addeffect <trailname> <effectname>");
						return false;
					} else {
						d.sendMessage(stRed + "Usage: /sta newtrail <trailname> <permission>");
					}
					return false;
				}
				if (args[0].equalsIgnoreCase("addeffect")) {
					if (args.length == 3) {
						if (!getConfig().contains("trails." + args[1])) {
							d.sendMessage(stRed + "Trail '" + args[1] + "' doesn't exist!");
							return false;
						}
						if (!trailmanager.doesEffectExist(args[2])) {
							d.sendMessage(stRed + "Effect '" + args[2] + "' doesn't exist!");
							return false;
						}
						List<String> list = new ArrayList<String>();
						if (getConfig().contains("trails." + args[1] + ".effects")) {
							list.addAll(getConfig().getStringList("trails." + args[1] + ".effects"));
						}
						list.add(args[2]);
						getConfig().set("trails." + args[1] + ".effects", list);
						silentReload();
						d.sendMessage(stBlue + "Effect added to your trail!");
						return false;
					} else {
						d.sendMessage(stRed + "Usage: /sta addeffect <trailname> <effectname>");
					}
				}
				if (args[0].equalsIgnoreCase("removeeffect")) {
					if (args.length == 3) {
						if (!getConfig().contains("trails." + args[1])) {
							d.sendMessage(stRed + "Trail '" + args[1] + "' doesn't exist!");
							return false;
						}
						if (!trailmanager.doesEffectExist(args[2])) {
							d.sendMessage(stRed + "Effect '" + args[2] + "' doesn't exist!");
							return false;
						}
						List<String> list = new ArrayList<String>();
						if (getConfig().contains("trails." + args[1] + ".effects")) {
							list.addAll(getConfig().getStringList("trails." + args[1] + ".effects"));
						}
						list.remove(args[2]);
						getConfig().set("trails." + args[1] + ".effects", list);
						silentReload();
						d.sendMessage(stBlue + "Effect removed to your trail!");
						return false;
					} else {
						d.sendMessage(stRed + "Usage: /sta removeeffect <trailname> <effectname>");
					}
				}
				if (args[0].equalsIgnoreCase("deltrail")) {
					if (args.length == 2) {
						if (!trailmanager.doesTrailExist(args[1])) {
							d.sendMessage(stRed + "Trail '" + args[1] + "' does not exist!");
							return false;
						}
						getConfig().set("trails." + args[1], null);
						saveConfig();
						silentReload();
						d.sendMessage(stBlue + "Trail deleted!");
						return false;
					} else {
						d.sendMessage(stRed + "Usage: /sta deltrail <trailname>");
					}
					return false;
				}
				if (args[0].equalsIgnoreCase("deleffect")) {
					if (args.length == 3) {
						if (!getConfig().contains("effects." + args[1])) {
							d.sendMessage(stRed + "Effect '" + args[1] + "' doesn't exist!");
							return false;
						}
						getConfig().set("effects." + args[1], null);
						saveConfig();
						silentReload();
						d.sendMessage(stBlue + "Removed effect from config.");
						return false;
					} else {
						d.sendMessage(stRed + "Usage: /sta deleffect <effectname>");
					}
				}
				if (args[0].equalsIgnoreCase("neweffect")) {
					if (args.length == 3) {
						if (getConfig().contains("effects." + args[1])) {
							d.sendMessage(stRed + "Effect '" + args[1] + "' already exists!");
							return false;
						}
						if (!trailmanager.canBeFloat(args[2])) {
							d.sendMessage(stRed + "Range must be an integer or double!");
							return false;
						}
						getConfig().set("effects." + args[1] + ".range", args[2]);
						saveConfig();
						silentReload();
						d.sendMessage(stBlue + "Created effect! To view effect options, use /sta edit " + args[1]);
						return false;
					} else {
						d.sendMessage(stRed + "Usage: /sta neweffect <effectname> <range>");
					}
				}
				if (args[0].equalsIgnoreCase("edit")) {
					if (args.length >= 2) {
						if (args.length == 4) {
							// args 1 = effectname
							// args 2 = option
							// args 3 = value
							if (!getConfig().contains(("effects." + args[1] + args[2]))) {
								d.sendMessage(stRed + "Configuration option not found! /sta options to view options!");
								return false;
							}
							getConfig().set(("effects." + args[1] + args[2]), args[3]);
							saveConfig();
							silentReload();
							d.sendMessage(stBlue + "Set config area '" + ("effects." + args[1] + args[2]) + "' to '" + args[3] + "' for effect '" + args[1] + "'.");
						} else {
							if (!getConfig().contains("effects." + args[1])) {
								d.sendMessage(stRed + "Effect '" + args[1] + "' does not exist!");
								return false;
							}
							d.sendMessage(stRed + "Editing '" + args[1] + "'");
							d.sendMessage(ChatColor.DARK_AQUA + "/sta edit <effectname> <option> <value>");
							d.sendMessage(ChatColor.BLUE + "View options with /sta options"); //TODO
						}
					} else {
						d.sendMessage(stRed + "Usage: /sta edit <effectname>");
					}
					return false;
				}

			}
		}
		return false;
	}

	public void silentReload() {
		this.reloadConfig();
		trailmanager = new TrailManager();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!getConfig().contains("players." + p.getUniqueId().toString())) {
				continue;
			}
			if (trailmanager.doesTrailExist(getConfig().getString("players." + p.getUniqueId().toString()))) {
				playerTrails.put(p.getUniqueId(), getConfig().getString("players." + p.getUniqueId().toString()));
				continue;
			} else {
				playerTrails.remove(p.getUniqueId());
				getConfig().set("players." + p.getUniqueId().toString(), null);
				saveConfig();
			}
		}
	}

	public void loadPlayerTrail(Player p) {
		if (!getConfig().contains("players." + p.getUniqueId().toString())) {
			return;
		}
		if (trailmanager.doesTrailExist(getConfig().getString("players." + p.getUniqueId().toString()))) {
			playerTrails.put(p.getUniqueId(), getConfig().getString("players." + p.getUniqueId().toString()));
			log.logToMiniLog("Loaded trail for user '" + p.getUniqueId() + "'", "INFO");
			return;
		} else {
			log.logToMiniLog("Unable to find trail for user '" + p.getUniqueId() + "'", "WARNING");
			playerTrails.remove(p.getUniqueId());
			getConfig().set("players." + p.getUniqueId().toString(), null);
			saveConfig();
		}
	}

	private void savePlayerTrail(Player p) {
		getConfig().set("players." + p.getUniqueId().toString(), playerTrails.get(p.getUniqueId()));
		saveConfig();
	}

	private void savePlayerTrails() {
		for (UUID id : playerTrails.keySet()) {
			getConfig().set("players." + id.toString(), playerTrails.get(id));
			saveConfig();
		}

	}

}
