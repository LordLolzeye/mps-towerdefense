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

public class PlayerEvents implements Listener {
	public Main plugin;
	
	public static HashMap<Player, Location> selectedLocation = new HashMap<Player, Location>();
	
	public PlayerEvents(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		this.plugin.playerMoney.put(e.getPlayer(), 30);
		this.plugin.playerTowers.put(e.getPlayer(), new ArrayList<Tower>());
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
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			selectedLocation.put(e.getPlayer(), e.getClickedBlock().getLocation());
			
			showTowerGUI(e.getPlayer());
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
		}
		
		p.closeInventory();
	}
	
	private void showTowerGUI(Player p) {
		// 0 1  2 3  4  5 6  7 8
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Select a Tower");
		
		ItemStack towerItem = new ItemStack(Material.FENCE);
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
		
		p.openInventory(inv);
	}
	
}
