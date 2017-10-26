package mps.hawks.project.PlayerEvents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mps.hawks.project.Main;
import mps.hawks.project.Tower.Tower;
import mps.hawks.project.Tower.TowerTypes.ArrowTower;
import mps.hawks.project.Tower.TowerTypes.CannonTower;
import mps.hawks.project.Tower.TowerTypes.SpectralTower;

public class PlayerEvents implements Listener {
	public Main plugin;
	
	public static HashMap<Player, Location> selectedLocation = new HashMap<Player, Location>();
	public static HashMap<Player, Tower> selectedTowerUpgrade = new HashMap<Player, Tower>();
	
	public PlayerEvents(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		this.plugin.playerMoney.put(e.getPlayer(), 30);
		this.plugin.playerTowers.put(e.getPlayer(), new ArrayList<Tower>());
		
		e.getPlayer().setHealth(20);
		e.getPlayer().setSaturation(20);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		this.plugin.playerMoney.remove(e.getPlayer());
		this.plugin.playerTowers.remove(e.getPlayer());
	}
	
	@EventHandler
	public void towerLocationSelector(PlayerInteractEvent e) {
		if(e.getPlayer().getGameMode() != GameMode.ADVENTURE) {
			return;
		}
		
		if((e.getAction() == Action.RIGHT_CLICK_BLOCK) && (e.getClickedBlock().getType() == Material.GRASS)) {
			selectedLocation.put(e.getPlayer(), e.getClickedBlock().getLocation());
			
			showTowerGUI(e.getPlayer());
		}
		
		// upgrade tower
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			for(Tower t : this.plugin.playerTowers.get(e.getPlayer())) {
				for(Location l : t.towerBlocks) {
					if(e.getClickedBlock().getLocation().equals(l)) {
						selectedTowerUpgrade.put(e.getPlayer(), t);
						showTowerUpgradeGUI(e.getPlayer(), t.possibleUpgrades);
						
						break;
					}
				}
			}
		}
		
	}
	
	@EventHandler
	public void towerSelectEvent(InventoryClickEvent e) {
		String inventoryName = ChatColor.stripColor(e.getInventory().getName()).replace(" ", "").toLowerCase();
		if(!inventoryName.equals("selectatower")) {
			return;
		}
		
		Player p = (Player) e.getWhoClicked();
		if(!selectedLocation.containsKey(p)) {
			p.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.DARK_RED + "Unknown error has occured!");
			return;
		}
		
		ItemStack selectedItem = e.getCurrentItem();
		if(selectedItem == null || selectedItem.getType().equals(Material.AIR)) {
			return;
		}
		
		String itemName = ChatColor.stripColor(selectedItem.getItemMeta().getDisplayName()).replace(" ", "").toLowerCase();
		if(itemName.equals("arrowtower")) {
			new ArrowTower(p, selectedLocation.get(p));
		} else if(itemName.equals("cannontower")) {
			new CannonTower(p, selectedLocation.get(p));
		} else if(itemName.equals("spectraltower")) {
			new SpectralTower(p, selectedLocation.get(p));
		}
		
		p.closeInventory();
	}
	
	@EventHandler
	public void upgradeSelectEvent(InventoryClickEvent e) {
		String inventoryName = ChatColor.stripColor(e.getInventory().getName()).replace(" ", "").toLowerCase();
		if(!inventoryName.equals("selectanupgrade")) {
			return;
		}
		
		Player p = (Player) e.getWhoClicked();
		if(!selectedTowerUpgrade.containsKey(p)) {
			p.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.DARK_RED + "Unknown error has occured!");
			return;
		}
		
		ItemStack selectedItem = e.getCurrentItem();
		if(selectedItem == null || selectedItem.getType().equals(Material.AIR)) {
			return;
		}
		
		String itemName = ChatColor.stripColor(selectedItem.getItemMeta().getDisplayName()).replace(" ", "").toLowerCase();
		Tower t = selectedTowerUpgrade.get(p);
		
		int upgradeType = 0;
		switch(itemName) {
		case "speed":
			upgradeType = 1;
			break;
		case "damage":
			upgradeType = 2;
			break;
		case "range":
			upgradeType = 3;
			break;
		}
		
		t.upgradeTower(upgradeType);
		
		p.closeInventory();
	}
	
	private void showTowerGUI(Player p) {
		// 0 1  2 3  4  5 6  7 8
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Select a Tower");
		
		// Arrow Tower -----------------------------------
		ItemStack towerItem = new ItemStack(Material.ARROW);
		ItemMeta towerMeta = towerItem.getItemMeta();
		towerMeta.setDisplayName(ChatColor.GOLD + "Arrow Tower");
		List<String> lore = new ArrayList<String>();
		if(towerMeta.hasLore()) {
			lore = towerMeta.getLore();
		}
		lore.add(ChatColor.GRAY + "Fast, but not so powerfull");
		lore.add(ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "10 gold");
		towerMeta.setLore(lore);
		towerItem.setItemMeta(towerMeta);
		
		inv.setItem(3, towerItem);
		// Arrow Tower -----------------------------------		
		
		
		// Cannon Tower -----------------------------------
		ItemStack cannonItem = new ItemStack(Material.ANVIL);
		ItemMeta cannonMeta = cannonItem.getItemMeta();
		cannonMeta.setDisplayName(ChatColor.GOLD + "Cannon Tower");
		lore.clear();
		if(cannonMeta.hasLore()) {
			lore = cannonMeta.getLore();
		}
		lore.add(ChatColor.GRAY + "Slow, but powerfull");
		lore.add(ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "15 gold");
		cannonMeta.setLore(lore);
		cannonItem.setItemMeta(cannonMeta);
		
		inv.setItem(5, cannonItem);
		// Cannon Tower -----------------------------------
		
		// Spectral Tower -----------------------------------
		ItemStack fastTower = new ItemStack(Material.SPECTRAL_ARROW);
		ItemMeta fastTowerMeta = fastTower.getItemMeta();
		fastTowerMeta.setDisplayName(ChatColor.GOLD + "Spectral Tower"); 
		lore.clear();
		if(fastTowerMeta.hasLore()) {
			lore = fastTowerMeta.getLore();
		}
		lore.add(ChatColor.GRAY + "Very fast, very low damage");
		lore.add(ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "20 gold");
		fastTowerMeta.setLore(lore);
		fastTower.setItemMeta(fastTowerMeta);
		
		inv.setItem(4, fastTower);
		// Spectral Tower -----------------------------------
		
		p.openInventory(inv);
	}
	
	private void showTowerUpgradeGUI(Player p, HashMap<Integer, Integer> possibleUpgrades) {
		// 0 1  2  3  4  5  6  7 8 // 2 4 6
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Select an Upgrade");
		
		ItemStack towerItem;
		ItemMeta towerMeta;
		List<String> lore = new ArrayList<String>();
		for(Integer upgrade : possibleUpgrades.keySet()) {
			switch(upgrade) {
			case 1:
				towerItem = new ItemStack(Material.BANNER);
				towerMeta = towerItem.getItemMeta();
				towerMeta.setDisplayName(ChatColor.GOLD + "Speed");
				lore = new ArrayList<String>();
				if(towerMeta.hasLore()) {
					lore = towerMeta.getLore();
				}
				lore.add(ChatColor.GRAY + "Increase rate of fire");
				lore.add(ChatColor.GRAY + "Cost: " + ChatColor.GOLD + possibleUpgrades.get(upgrade) + " gold");
				towerMeta.setLore(lore);
				towerItem.setItemMeta(towerMeta);
				
				inv.setItem(2, towerItem);
				break;
				
			case 2:
				towerItem = new ItemStack(Material.ANVIL);
				towerMeta = towerItem.getItemMeta();
				towerMeta.setDisplayName(ChatColor.GOLD + "Damage");
				lore = new ArrayList<String>();
				if(towerMeta.hasLore()) {
					lore = towerMeta.getLore();
				}
				lore.add(ChatColor.GRAY + "Increase damage");
				lore.add(ChatColor.GRAY + "Cost: " + ChatColor.GOLD + possibleUpgrades.get(upgrade) + " gold");
				towerMeta.setLore(lore);
				towerItem.setItemMeta(towerMeta);
				
				inv.setItem(4, towerItem);
				break;
				
			case 3:
				towerItem = new ItemStack(Material.ARROW);
				towerMeta = towerItem.getItemMeta();
				towerMeta.setDisplayName(ChatColor.GOLD + "Range");
				lore = new ArrayList<String>();
				if(towerMeta.hasLore()) {
					lore = towerMeta.getLore();
				}
				lore.add(ChatColor.GRAY + "Increase Range");
				lore.add(ChatColor.GRAY + "Cost: " + ChatColor.GOLD + possibleUpgrades.get(upgrade) + " gold");
				towerMeta.setLore(lore);
				towerItem.setItemMeta(towerMeta);
				
				inv.setItem(6, towerItem);
				break;
			}
		}
		
		p.openInventory(inv);
	}
	
}
