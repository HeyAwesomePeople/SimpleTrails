package me.HeyAwesomePeople.trails;

import java.util.Random;

import me.HeyAwesomePeople.trails.lib.ParticleEffect;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TrailCreator {

	ItemStack redrose = new ItemStack(Material.RED_ROSE, 1);
	ItemStack blueorchid = new ItemStack(Material.RED_ROSE, 1, (short) 1);
	ItemStack allium = new ItemStack(Material.RED_ROSE, 1, (short) 2);
	ItemStack azurebluet = new ItemStack(Material.RED_ROSE, 1, (short) 3);
	ItemStack redtulip = new ItemStack(Material.RED_ROSE, 1, (short) 4);
	ItemStack orangetulip = new ItemStack(Material.RED_ROSE, 1, (short) 5);
	ItemStack whitetulip = new ItemStack(Material.RED_ROSE, 1, (short) 6);
	ItemStack pinktulip = new ItemStack(Material.RED_ROSE, 1, (short) 7);
	ItemStack oxeyedaisy = new ItemStack(Material.RED_ROSE, 1, (short) 8);
	ItemStack sunflower = new ItemStack(Material.DOUBLE_PLANT, 1);
	ItemStack lilac = new ItemStack(Material.DOUBLE_PLANT, 1, (short) 1);
	ItemStack rosebush = new ItemStack(Material.DOUBLE_PLANT, 1, (short) 4);
	ItemStack peony = new ItemStack(Material.RED_ROSE, 1, (short) 5);
	ItemStack dandelion = new ItemStack(Material.YELLOW_FLOWER, 1);
	ItemStack[] flowerlist = { redrose, blueorchid, allium, azurebluet, redtulip, orangetulip, whitetulip, pinktulip, oxeyedaisy, sunflower, lilac, rosebush, peony, dandelion };

	ItemStack coal = new ItemStack(Material.COAL, 1);
	ItemStack iron = new ItemStack(Material.IRON_INGOT, 1);
	ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
	ItemStack gold = new ItemStack(Material.GOLD_INGOT, 1);
	ItemStack emerald = new ItemStack(Material.EMERALD, 1);
	ItemStack[] lootlist = { coal, iron, diamond, gold, emerald };

	public float r(String[] rMFD, String offset) {
		if (Boolean.parseBoolean(rMFD[0])) {
			Float min = Float.parseFloat(rMFD[1]);
			Float max = Float.parseFloat(rMFD[2]);
			Float n = (float) Math.random();
			return (min + (n * (max - min)));	
		} else {
			return Float.parseFloat(offset);
		}
	}
	
	public float r2(String[] randomSpeed, String speed) {
		if (Boolean.parseBoolean(randomSpeed[0])) {
			Float min = Float.parseFloat(randomSpeed[1]);
			Float max = Float.parseFloat(randomSpeed[2]);
			Float n = (float) Math.random();
			return (min + (n * (max - min)));
		} else {
			return Float.parseFloat(speed);
		}
	}
	
	public int r3(String[] randomAmount, String amount) {
		if (Boolean.parseBoolean(randomAmount[0])) {
			Random random = new Random();
			int min = Integer.parseInt(randomAmount[1]);
			int max = Integer.parseInt(randomAmount[2]);
			return random.nextInt((max - min) + min) + 1;
		} else {
			return Integer.parseInt(amount);
		}
	}

	public void createParticle(String effectName, Location l, String[] rMFD,
			String sOff, String[] rSpeed, String speed,
			String[] rAmount, String amount, String locationOffset, String range) {

		ParticleEffect.fromName(effectName).display(
				r(rMFD, sOff),
				r(rMFD, sOff),
				r(rMFD, sOff),
				r2(rSpeed, speed),
				r3(rAmount, amount),
				l.add(0D, Double.parseDouble(locationOffset), 0D),
				Double.parseDouble(range));

	}
}
