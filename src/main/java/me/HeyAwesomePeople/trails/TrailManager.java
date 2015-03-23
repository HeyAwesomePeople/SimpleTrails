package me.HeyAwesomePeople.trails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

public class TrailManager {
	private SimpleTrails plugin = SimpleTrails.instance;

	private List<String> skip = new ArrayList<String>();

	public HashMap<String, List<String>> trails = new HashMap<String, List<String>>();
	public HashMap<String, String> trailPerm = new HashMap<String, String>();

	public HashMap<String, HashMap<String, String>> effects = new HashMap<String, HashMap<String, String>>();

	public String MFDEnabled = "false";
	public String MFDOffsetMax = "1.0";
	public String MFDOffsetMin = "0.1";
	public String pSO = "1.0";
	public String SpeedEnabled = "false";
	public String speedMax = "0.2";
	public String speedMin = "0.05";
	public String sSpeed = "0.1";
	public String randomAmountEnabled = "false";
	public String amountMax = "3";
	public String amountMin = "1";
	public String sAmount = "2";
	public String locOffsetY = "1";
	public String sRange = "20.0";
	
	public TrailManager() {
		skip.add("randomMaxFlyDis");
		skip.add("enabled");
		skip.add("offsetMax");
		skip.add("offsetMin");
		skip.add("particleSpawnOffset");
		skip.add("randomSpeed");
		skip.add("enabled");
		skip.add("speedMax");
		skip.add("speedMin");
		skip.add("staticSpeed");
		skip.add("randomAmount");
		skip.add("amountMax");
		skip.add("amountMin");
		skip.add("staticAmount");
		skip.add("locationOffsetY");
		skip.add("range");

		loadEffects();
		loadTrails();
		checkTrails();
	}

	private Boolean straightEffect(String s) {
		for (String s1 : skip) {
			if (s.contains(s1)) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}

	public void loadEffects() {
		clearLocalData();
		for (String f : plugin.getConfig().getConfigurationSection("effects").getKeys(true)) {
			if (straightEffect(f)) continue;
			
			HashMap<String, String> vars = new HashMap<String, String>();
			vars.put("randomMaxFlyDis", getCString("effects." + f + ".randomMaxFlyDis.enabled") + ":" + getCString("effects." + f + ".randomMaxFlyDis.offsetMax") + ":" + getCString("effects." + f + ".randomMaxFlyDis.offsetMin"));
			vars.put("particleSpawnOffset", getCString("effects." + f + ".particleSpawnOffset"));

			vars.put("randomSpeed", getCString("effects." + f + ".randomSpeed.enabled") + ":" + getCString("effects." + f + ".randomSpeed.speedMax") + ":" + getCString("effects." + f + ".randomSpeed.speedMin"));
			vars.put("speed", getCString("effects." + f + ".staticSpeed"));

			vars.put("randomAmount", getCString("effects." + f + ".randomAmount.enabled") + ":" + getCString("effects." + f + ".randomAmount.amountMax") + ":" + getCString("effects." + f + ".randomAmount.amountMin"));
			vars.put("amount", getCString("effects." + f + ".staticAmount"));

			vars.put("locationOffsetY", getCString("effects." + f + ".locationOffsetY"));
			vars.put("range", getCString("effects." + f + ".range"));

			this.effects.put(f, vars);
		}
		plugin.log.logToMiniLog("Loaded Effects: " + this.effects.keySet(), "INFO");
	}

	public String getCString(String s) {
		
		String c = plugin.getConfig().getString(s);
		if (s.contains("randomMaxFlyDis")) {
			if (s.contains("enabled")) {
				if (c == null) {
					plugin.log.logToMiniLog("Configuration returned null when trying to get '" + s + "'. Defaulting to " + MFDEnabled, "FALLBACK");
					return MFDEnabled;
				}
				if (canBeBool(c)) {
					return c;
				}
				plugin.log.logToMiniLog("Unable to convert config entry '" + s + "' into a boolean! Defaulting to false.", "FALLBACK");
				return MFDEnabled;

			}
			if (s.contains("offsetMax")) {
				if (c == null) {
					plugin.log.logToMiniLog("Configuration returned null when trying to get '" + s + "'. Defaulting to " + MFDOffsetMax, "FALLBACK");
					return MFDOffsetMax;
				}
				if (canBeFloat(c)) {
					return c;
				}
				plugin.log.logToMiniLog("Unable to convert config entry '" + s + "' into a float! Defaulting to 1.0.", "FALLBACK");
				return MFDOffsetMax;
			}
			if (s.contains("offsetMin")) {
				if (c == null) {
					plugin.log.logToMiniLog("Configuration returned null when trying to get '" + s + "'. Defaulting to " + MFDOffsetMin, "FALLBACK");
					return MFDOffsetMin;
				}
				if (canBeFloat(c)) {
					return c;
				}
				plugin.log.logToMiniLog("Unable to convert config entry '" + s + "' into a float! Defaulting to 1.0.", "FALLBACK");
				return MFDOffsetMin;
			}
		} else if (s.contains("particleSpawnOffset")) {
			if (c == null) {
				plugin.log.logToMiniLog("Configuration returned null when trying to get '" + s + "'. Defaulting to " + pSO, "FALLBACK");
				return pSO;
			}
			if (canBeFloat(c)) {
				return c;
			}
			plugin.log.logToMiniLog("Unable to convert config entry '" + s + "' into a float! Defaulting to false.", "FALLBACK");
			return pSO;
		} else if (s.contains("randomSpeed")) {
			if (s.contains("enabled")) {
				if (c == null) {
					plugin.log.logToMiniLog("Configuration returned null when trying to get '" + s + "'. Defaulting to " + SpeedEnabled, "FALLBACK");
					return SpeedEnabled;
				}
				if (canBeBool(c)) {
					return c;
				}
				plugin.log.logToMiniLog("Unable to convert config entry '" + s + "' into a boolean! Defaulting to false.", "FALLBACK");
				return SpeedEnabled;

			}
			if (s.contains("speedMax")) {
				if (c == null) {
					plugin.log.logToMiniLog("Configuration returned null when trying to get '" + s + "'. Defaulting to " + speedMax, "FALLBACK");
					return speedMax;
				}
				if (canBeFloat(c)) {
					return c;
				}
				plugin.log.logToMiniLog("Unable to convert config entry '" + s + "' into a float! Defaulting to 0.2.", "FALLBACK");
				return speedMax;
			}
			if (s.contains("speedMin")) {
				if (c == null) {
					plugin.log.logToMiniLog("Configuration returned null when trying to get '" + s + "'. Defaulting to " + speedMin, "FALLBACK");
					return speedMin;
				}
				if (canBeFloat(c)) {
					return c;
				}
				plugin.log.logToMiniLog("Unable to convert config entry '" + s + "' into a float! Defaulting to 0.05.", "FALLBACK");
				return speedMin;
			}
		} else if (s.contains("staticSpeed")) {
			if (c == null) {
				plugin.log.logToMiniLog("Configuration returned null when trying to get '" + s + "'. Defaulting to " + sSpeed, "FALLBACK");
				return sSpeed;
			}
			if (canBeFloat(c)) {
				return c;
			}
			plugin.log.logToMiniLog("Unable to convert config entry '" + s + "' into a float! Defaulting to 0.1.", "FALLBACK");
			return sSpeed;
		} else if (s.contains("randomAmount")) {
			if (s.contains("enabled")) {
				if (c == null) {
					plugin.log.logToMiniLog("Configuration returned null when trying to get '" + s + "'. Defaulting to " + randomAmountEnabled, "FALLBACK");
					return randomAmountEnabled;
				}
				if (canBeBool(c)) {
					return c;
				}
				plugin.log.logToMiniLog("Unable to convert config entry '" + s + "' into a boolean! Defaulting to false.", "FALLBACK");
				return randomAmountEnabled;

			}
			if (s.contains("amountMax")) {
				if (c == null) {
					plugin.log.logToMiniLog("Configuration returned null when trying to get '" + s + "'. Defaulting to " + amountMax, "FALLBACK");
					return amountMax;
				}
				if (canBeInt(c)) {
					return c;
				}
				plugin.log.logToMiniLog("Unable to convert config entry '" + s + "' into an integer! Defaulting to 3.", "FALLBACK");
				return amountMax;
			}
			if (s.contains("amountMin")) {
				if (c == null) {
					plugin.log.logToMiniLog("Configuration returned null when trying to get '" + s + "'. Defaulting to " + amountMin, "FALLBACK");
					return amountMin;
				}
				if (canBeInt(c)) {
					return c;
				}
				plugin.log.logToMiniLog("Unable to convert config entry '" + s + "' into an integer! Defaulting to 1.", "FALLBACK");
				return amountMin;
			}
		} else if (s.contains("staticAmount")) {
			if (c == null) {
				plugin.log.logToMiniLog("Configuration returned null when trying to get '" + s + "'. Defaulting to " + sAmount, "FALLBACK");
				return sAmount;
			}
			if (canBeInt(c)) {
				return c;
			}
			plugin.log.logToMiniLog("Unable to convert config entry '" + s + "' into an integer! Defaulting to 2.", "FALLBACK");
			return sAmount;
		} else if (s.contains("locationOffsetY")) {
			if (c == null) {
				plugin.log.logToMiniLog("Configuration returned null when trying to get '" + s + "'. Defaulting to " + locOffsetY, "FALLBACK");
				return locOffsetY;
			}
			if (canBeInt(c)) {
				return c;
			}
			plugin.log.logToMiniLog("Unable to convert config entry '" + s + "' into an integer! Defaulting to 1.", "FALLBACK");
			return locOffsetY;
		} else if (s.contains("range")) {
			if (c == null) {
				plugin.log.logToMiniLog("Configuration returned null when trying to get '" + s + "'. Defaulting to " + sRange, "FALLBACK");
				return sRange;
			}
			if (canBeFloat(c)) {
				return c;
			}
			plugin.log.logToMiniLog("Unable to convert config entry '" + s + "' into an integer! Defaulting to 20.", "FALLBACK");
			return sRange;
		}
		
		plugin.log.logToMiniLog("Report this immediatly to the plugin maker! Plugin tried to find non-existent value!", "SEVERE");
		return "";
	}

	public void loadTrails() {
		for (String s : plugin.getConfig().getConfigurationSection("trails").getKeys(true)) {
			if (s.contains("permission") || s.contains("effects")) continue;

			trailPerm.put(s, plugin.getConfig().getString("trails." + s + ".permission"));
			trails.put(s, plugin.getConfig().getStringList("trails." + s + ".effects"));
		}
		plugin.log.logToMiniLog("Loaded Trails: " + this.trails.keySet(), "INFO");
	}

	public void checkTrails() {
		List<String> toBeRemoved = new ArrayList<String>();
		for (String s : this.trails.keySet()) {
			for (String e : this.trails.get(s)) {
				if (!doesEffectExist(e)) {
					toBeRemoved.add(e);
					plugin.log.logToMiniLog("Effect '" + e + "' was not found! Removing from trail '" + s + "'", "SEVERE");
				}
			}
		}
		for (String ef : toBeRemoved) {
			this.trails.remove(ef);
		}
	}

	public void displayTrail(Player p, String trailName) {
		for (String s : trails.get(trailName)) {
			HashMap<String, String> e = this.effects.get(s);
			if (e == null) {
				plugin.log.logToMiniLog("Plugin was unable to find the effect '" + s + "'!", "SEVERE");
				return;
			}
			plugin.trailcreator.createParticle(s, p.getLocation(), getMFD(e), getSpawnOffset(e), getRandomSpeed(e), getSpeed(e), getRandomAmount(e), getAmount(e), getLocationOffsetY(e), getRange(e));
		}
	}

	//*********** Effect Value Grabbers ****************//
	private String[] getMFD(HashMap<String, String> ef) {
		String[] fallBack = { "false", "0", "0" };

		if (ef.get("randomMaxFlyDis") == null) {
			return fallBack;
		} else {
			return ef.get("randomMaxFlyDis").split(":");
		}
	}

	private String getSpawnOffset(HashMap<String, String> ef) {
		String fallBack = "0.5";

		if (ef.get("particleSpawnOffset") == null) {
			return fallBack;
		} else {
			return ef.get("particleSpawnOffset");
		}
	}

	private String[] getRandomSpeed(HashMap<String, String> ef) {
		String[] fallBack = { "false", "0", "0" };

		if (ef.get("randomSpeed") == null) {
			return fallBack;
		} else {
			return ef.get("randomSpeed").split(":");
		}
	}

	private String getSpeed(HashMap<String, String> ef) {
		String fallBack = "0.1";

		if (ef.get("speed") == null) {
			return fallBack;
		} else {
			return ef.get("speed");
		}
	}

	private String[] getRandomAmount(HashMap<String, String> ef) {
		String[] fallBack = { "false", "0", "0" };

		if (ef.get("randomAmount") == null) {
			return fallBack;
		} else {
			return ef.get("randomAmount").split(":");
		}
	}

	private String getAmount(HashMap<String, String> ef) {
		String fallBack = "3";

		if (ef.get("amount") == null) {
			return fallBack;
		} else {
			return ef.get("amount");
		}
	}

	private String getLocationOffsetY(HashMap<String, String> ef) {
		String fallBack = "1";

		if (ef.get("locationOffsetY") == null) {
			return fallBack;
		} else {
			return ef.get("locationOffsetY");
		}
	}

	private String getRange(HashMap<String, String> ef) {
		String fallBack = "25";

		if (ef.get("range") == null) {
			return fallBack;
		} else {
			return ef.get("range");
		}
	}

	public boolean canBeBool(String s) {
		try {
			Boolean.parseBoolean(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public boolean canBeFloat(String s) {
		try {
			Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public boolean canBeInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private void clearLocalData() {
		this.trails.clear();
		this.trailPerm.clear();
		this.effects.clear();
	}


	public Boolean doesEffectExist(String s) {
		if (this.effects.keySet().contains(s)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean doesTrailExist(String s) {
		if (this.trails.keySet().contains(s.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}
	
}
