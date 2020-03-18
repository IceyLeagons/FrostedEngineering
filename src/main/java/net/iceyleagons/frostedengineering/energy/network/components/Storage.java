package net.iceyleagons.frostedengineering.energy.network.components;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.energy.network.EnergyNetwork;
import net.iceyleagons.frostedengineering.energy.network.Unit;
import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import net.iceyleagons.frostedengineering.utils.ItemFactory;

public class Storage extends Unit {

	private float maxStores = 1.0f; //how many FP it can store
	private float stores = 0.0f;
	public boolean full = false;
	public String id;
	
	/**
	 * @param loc is the location of the unit
	 * @param network is the energy network it's in
	 * @param maxStores the capacity of the storage
	 */
	public Storage(Location loc, EnergyNetwork network, float maxStores) {
		super(loc, network);
		this.maxStores = maxStores;
		Main.debug("Creating storage...");
		this.id = RandomStringUtils.randomAlphabetic(5);
		Unit.storageIds.put(getID(), this);
		initInventory();
	}
	
	/**
	 * This is used for storage items.
	 * 
	 * @param loc is the location of the unit
	 * @param network is the energy network it's in
	 * @param maxStores the capacity of the storage
	 * @param ID is the storage id
	 */
	public Storage(Location loc, EnergyNetwork network, float maxStores, String id) {
		super(loc, network);
		Storage s = Unit.getStorage(id);
		if (s!=null) {
			this.stores = s.getStored();
			if (!Unit.doMultipleSameStoragesExists(id))
				this.id = id;
			else
				this.id = RandomStringUtils.randomAlphabetic(5);
			this.maxStores = s.getMaxStorage();
		} else {
			this.id = RandomStringUtils.randomAlphabetic(5);
			this.maxStores = maxStores;
		}
		Main.debug("Creating storage...");
		initInventory();
	}
	
	/**
	 * @return the capacity of the storage
	 */
	public float getMaxStorage() {
		return maxStores;
	}
	
	public ItemStack getItem() {
		ItemFactory itf = new ItemFactory(Material.COBBLESTONE).hideAttributes();
		itf.setDisplayName("§b§lStorage - §r§b" + stores + " FP");
		itf.addLoreLine("§fStores: §b" + stores + " §fFP");
		itf.addLoreLine("§fCapacity: §b" + maxStores + " §fFP");
		itf.addLoreLine(" ");
		itf.addLoreLine("§7Storage ID §b#"+id);
		return itf.build();
	}
	
	
	/**
	 * Used for storage items.
	 * 
	 * @return the ID of the storage.
	 */
	public String getID() {
		return this.id;
	}
	
	/**
	 * @return the currently stored energy
	 */
	public float getStored() {
		return stores;
	}
	
	/**
	 * Adds power to the storage, if the capacity is breached the storage can explode
	 * 
	 * @param fp is the power to add to the storage
	 * @return the remaining power if the storage gets full, the main logic is handled by the energy network.
	 */
	public float addPower(float fp) {
		if ((stores+fp)<=maxStores) {
			stores+=fp;
			updateInventory();
			return 0f;
		} else {
			stores=maxStores;
			updateInventory();
			return maxStores-(stores+fp);
		}
	}
	
	
	/**
	 * This function is used to create our inventory.
	 */
	public void initInventory() {
		InventoryFactory fac = new InventoryFactory("Storage",27, new ItemFactory(Material.GRAY_STAINED_GLASS_PANE).hideAttributes().setDisplayName(" ").build(),true);
		for (int i = 10; i < 15; i++) 
			fac.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName("§f"+stores+"/"+maxStores+" FP").build(), i);
		super.setInventoryFactory(fac);
		updateInventory();
	}

	/**
	 * This function is used to update our inventory.
	 * However it only runs 1/1s to not freak bukkit out.
	 */
	public void updateInventory() {
		if (super.getInventoryFactory() == null) initInventory();
		InventoryFactory fac = super.getInventoryFactory();
		float zero = 0f;
		float twentyfive = maxStores*0.25f;
		float half = maxStores*0.5f;
		float seventyfive = maxStores*0.75f;
		
		String text = "§f"+stores+"/"+maxStores+" FP";
		
		for (int i = 10; i < 15; i++) 
			fac.setItem(new ItemFactory(fac.getSourceInventory().getItem(i).getType()).hideAttributes().setDisplayName(text).build(), i);
		
		if (stores >= zero)
			fac.setItem(new ItemFactory(Material.GREEN_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 10);
		if (stores >= twentyfive)
			fac.setItem(new ItemFactory(Material.GREEN_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 11);
		if (stores >= half)
			fac.setItem(new ItemFactory(Material.GREEN_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 12);
		if (stores >= seventyfive)
			fac.setItem(new ItemFactory(Material.GREEN_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 13);
		if (stores == maxStores)
			fac.setItem(new ItemFactory(Material.GREEN_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 14);
		
		if (stores <= zero)
			fac.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 10);
		if (stores <= twentyfive)
			fac.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 11);
		if (stores <= half)
			fac.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 12);
		if (stores <= seventyfive)
			fac.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 13);
		if (stores < maxStores)
			fac.setItem(new ItemFactory(Material.WHITE_STAINED_GLASS_PANE).hideAttributes().setDisplayName(text).build(), 14);
		
	}
	
	/**
	 * @param fp is the power needed
	 * @return the remainig power to discharge, the main logic is handled by the energy network.
	 */
	public float consumePower(float fp) {
		if ((stores-fp)<0) {
			stores-=maxStores;
			return fp-maxStores;
		}
		
		if ((stores-fp)>=0) {
			stores-=fp;
			return 0f;
		}
		return 0f;
	}

}

