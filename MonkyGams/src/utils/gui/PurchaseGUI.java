package utils.gui;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import core.data.PlayerStatistics;
import core.data.PlayerStatisticsManager;
import net.md_5.bungee.api.ChatColor;
import utils.MonkyItemStack;
import utils.Utils;
import utils.game.GameUtils;

public class PurchaseGUI extends MonkyGUI {
	
	@FunctionalInterface
	public interface PurchaseSuccess {
		public void doSuccess(int quantity);
	}
	
	public Material item;
	public String itemName;
	public int cost;
	public int maxQuantity;
	
	public PurchaseGUI(Player p, Material item, String itemName, String itemDescription, int maxQuantity, int cost, PurchaseSuccess onSuccess) {
		super(p, "Checkout", 6);
		
		this.item = item;
		this.itemName = itemName;
		this.cost = cost;
		this.maxQuantity = maxQuantity;
		
		for (int i = 0; i!=9; i++) {
			getInventory().setItem(i, new MonkyItemStack(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.GRAY + "Available Items"));
		}
		
		if (maxQuantity > 1) {
			getInventory().addItem(new MonkyItemStack(item)
					.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "1x " + itemName)
					.setLore(ChatColor.GRAY + itemDescription, "", ChatColor.GRAY + "" + ChatColor.BOLD + "Cost: " + (cost * 1) + " pound" + Utils.plural(cost * 1))
					.setNumber(1));
			getInventory().addItem(new MonkyItemStack(item)
					.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "10x " + itemName)
					.setLore(ChatColor.GRAY + itemDescription, "", ChatColor.GRAY + "" + ChatColor.BOLD + "Cost: " + (cost * 10) + " pound" + Utils.plural(cost * 10))
					.setNumber(10));
			getInventory().addItem(new MonkyItemStack(item)
					.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "32x " + itemName)
					.setLore(ChatColor.GRAY + itemDescription, "", ChatColor.GRAY + "" + ChatColor.BOLD + "Cost: " + (cost * 32) + " pound" + Utils.plural(cost * 32))
					.setNumber(32));
			getInventory().addItem(new MonkyItemStack(item)
					.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "64x " + itemName)
					.setLore(ChatColor.GRAY + itemDescription, "", ChatColor.GRAY + "" + ChatColor.BOLD + "Cost: " + (cost * 64) + " pound" + Utils.plural(cost * 64))
					.setNumber(64));
		}
		
		else {
			getInventory().addItem(new MonkyItemStack(item)
					.setName(ChatColor.GOLD + "" + ChatColor.BOLD + itemName)
					.setLore(ChatColor.GRAY + itemDescription, "", ChatColor.GRAY + "" + ChatColor.BOLD + "Cost: " + cost + " pound" + Utils.plural(cost)));
		}
		
		for (int i = 27; i!=27+9; i++) {
			getInventory().setItem(i, new MonkyItemStack(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.GRAY + "Your Cart"));
		}
		
		setClickEvent(ClickType.DOUBLE_CLICK, (clicker, clickedItem) -> {
			if (clickedItem.getType() == Material.LIME_STAINED_GLASS_PANE) {
				clicker.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
				clicker.sendMessage(GameUtils.getSuccessMessage("PURCHASE", "Purchase successful!"));
				GameUtils.delayTask(() -> {
					clicker.sendMessage(GameUtils.getSuccessMessage("PURCHASE", "You purchased " + Utils.bold((maxQuantity == 1 ? "" : getTotalItems() + "x ") + itemName)) + "!");
				}, 60);
				
				PlayerStatistics ps = PlayerStatisticsManager.getStatistics(clicker);
				ps.setCurrency(ps.getCurrency() - getTotalCost());
				onSuccess.doSuccess(getTotalItems());
				clicker.closeInventory();
				clearButton(clicker);
			}
		});
		
		setClickEvent(ClickType.LEFT, (clicker, clickedItem) -> {
			
			if (clickedItem.getType() == Material.GRAY_STAINED_GLASS_PANE) {
				clicker.sendMessage(GameUtils.getFailureMessage("PURCHASE", clickedItem.getItemMeta().getLore().get(0)));
				updateButton(clicker);
				return;
			}
			
			if (clickedItem.getType() == Material.RED_STAINED_GLASS_PANE) {
				clicker.closeInventory();
				clearButton(clicker);
				return;
			}
			
			if (clickedItem.getType() == item) {
				if (clickedItem.getItemMeta().getLore() != null) {
					try {
						addToCart(clickedItem.getAmount());
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
					} catch (Exception e) {
						p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
						p.sendMessage(GameUtils.getFailureMessage("PURCHASE", e.getMessage()));
					}
					updateButton(clicker);
					updatePurchaseSummary(p);
				}
				else {
					removeFromCart(clickedItem.getAmount());
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 1);
					updateButton(clicker);
					updatePurchaseSummary(p);
				}
			}
			
		});
		
	}
	
	@Override
	public void present(Player p) {
		super.present(p);
		putLowerInventory(p);
		updateButton(p);
		updatePurchaseSummary(p);
	}
	
	public void clearButton(Player clicker) {
		
		Inventory pi = clicker.getInventory();
		pi.setItem(27, null);
		pi.setItem(28, null);
		pi.setItem(29, null);
		pi.setItem(9, null);
		pi.setItem(10, null);
		pi.setItem(11, null);
		pi.setItem(18, null);
		pi.setItem(19, null);
		pi.setItem(20, null);
		pi.setItem(33, null);
		pi.setItem(34, null);
		pi.setItem(35, null);
		pi.setItem(15, null);
		pi.setItem(16, null);
		pi.setItem(17, null);
		pi.setItem(24, null);
		pi.setItem(25, null);
		pi.setItem(26, null);
		pi.setItem(22, null);
		
	}
	
	public void updateButton(Player clicker) {
		
		PlayerStatistics ps = PlayerStatisticsManager.getStatistics(clicker);
		
		MonkyItemStack noItems = new MonkyItemStack(Material.GRAY_STAINED_GLASS_PANE)
				.setName(ChatColor.GRAY + "" + ChatColor.BOLD + "Buy")
				.setLore(ChatColor.GRAY + "You must add something to your cart to buy it!");
		
		MonkyItemStack notEnoughMoney = new MonkyItemStack(Material.GRAY_STAINED_GLASS_PANE)
				.setName(ChatColor.GRAY + "" + ChatColor.BOLD + "Buy")
				.setLore(ChatColor.GRAY + "You can't afford this purchase!");
		
		MonkyItemStack okay = new MonkyItemStack(Material.LIME_STAINED_GLASS_PANE)
				.setName(ChatColor.GREEN + "" + ChatColor.BOLD + "Buy")
				.setLore(ChatColor.GRAY + "Double click here to purchase " + (maxQuantity == 1 ? "" : getTotalItems() + "x ") + itemName + ChatColor.RESET + "" + ChatColor.GRAY + " for " + getTotalCost() + " pounds");
		
		MonkyItemStack chosen;
		if (getTotalItems() == 0)
			chosen = noItems;
		else if (ps.getCurrency() < getTotalCost())
			chosen = notEnoughMoney;
		else
			chosen = okay;
		
		Inventory pi = clicker.getInventory();
		
		pi.setItem(9, chosen);
		pi.setItem(10, chosen);
		pi.setItem(11, chosen);
		pi.setItem(18, chosen);
		pi.setItem(19, chosen);
		pi.setItem(20, chosen);
		pi.setItem(27, chosen);
		pi.setItem(28, chosen);
		pi.setItem(29, chosen);
		
	}
	
	public void updatePurchaseSummary(Player clicker) {
		PlayerStatistics ps = PlayerStatisticsManager.getStatistics(clicker);
		int after = ps.getCurrency() - getTotalCost();
		
		MonkyItemStack purchaseSummary = new MonkyItemStack(Material.PAPER)
				.setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Receipt")
				.setLore(ChatColor.GRAY + Utils.bold("Cost per item: ") + cost + " pound" + Utils.plural(cost),
						ChatColor.GRAY + Utils.bold("Total items: ") + getTotalItems() + " item" + Utils.plural(getTotalItems()),
						"",
						ChatColor.GRAY + Utils.bold("Total cost: ") + getTotalCost() + " pound" + Utils.plural(getTotalCost()),
						ChatColor.GRAY + Utils.bold("Your funds: ") + ps.getCurrency() + " pound" + Utils.plural(ps.getCurrency()),
						ChatColor.GRAY + Utils.bold("After purchase: ") + (after < 0 ? ChatColor.RED : ChatColor.GRAY) + after + " pound" + Utils.plural(after));
		clicker.getInventory().setItem(22, purchaseSummary);
	}
	
	public void putLowerInventory(Player p) {
		Inventory pi = p.getInventory();
		
		MonkyItemStack greyedOut = new MonkyItemStack(Material.GRAY_STAINED_GLASS_PANE)
				.setName(ChatColor.GRAY + "" + ChatColor.BOLD + "Buy")
				.setLore(ChatColor.GRAY + "You must add something to your cart to buy it!");
		
		pi.setItem(9, greyedOut);
		pi.setItem(10, greyedOut);
		pi.setItem(11, greyedOut);
		pi.setItem(18, greyedOut);
		pi.setItem(19, greyedOut);
		pi.setItem(20, greyedOut);
		pi.setItem(27, greyedOut);
		pi.setItem(28, greyedOut);
		pi.setItem(29, greyedOut);
		
		MonkyItemStack cancel = new MonkyItemStack(Material.RED_STAINED_GLASS_PANE)
				.setName(ChatColor.RED + "" + ChatColor.BOLD + "Cancel")
				.setLore(ChatColor.GRAY + "Click to cancel the purchase.");
		
		pi.setItem(15, cancel);
		pi.setItem(16, cancel);
		pi.setItem(17, cancel);
		pi.setItem(24, cancel);
		pi.setItem(25, cancel);
		pi.setItem(26, cancel);
		pi.setItem(33, cancel);
		pi.setItem(34, cancel);
		pi.setItem(35, cancel);
		
		updatePurchaseSummary(p);
	}
	
	public void addToCart(int addAmount) throws Exception {	
		
		if (getTotalItems() >= maxQuantity) {
			throw new Exception("You can't add any more of this item to your cart.");
		}
		
		for (int i = 36; i != 54; i++) {
			ItemStack itemInInv = getInventory().getItem(i);
			
			if (addAmount == 0)
				break;
			
			if (itemInInv == null) {
				getInventory().setItem(i, new MonkyItemStack(item).setName(ChatColor.GOLD + "" + ChatColor.BOLD + itemName));
				itemInInv = getInventory().getItem(i);
				addAmount--;
			}
			
			if (itemInInv.getAmount() < 64) {
				if (itemInInv.getAmount() + addAmount > 64) {
					addAmount -= 64 - itemInInv.getAmount();
					itemInInv.setAmount(64);
				}
				else {
					itemInInv.setAmount(itemInInv.getAmount() + addAmount);
					addAmount -= addAmount;
				}
			}
		}
		
		if (addAmount == 0) {
			return;
		}
		else {
			throw new Exception("Your cart is full!");
		}
		
	}
	
	public void removeFromCart(int removeAmount) {
		
		for (int i = 53; i != 35; i--) {
			ItemStack itemInInv = getInventory().getItem(i);
			
			if (removeAmount == 0)
				break;
			
			if (itemInInv == null)
				continue;
			
			if (removeAmount > 64) {
				removeAmount -= itemInInv.getAmount();
				itemInInv.setType(null);
			}
			else {
				itemInInv.setAmount(itemInInv.getAmount() - removeAmount);
				removeAmount -= removeAmount;
			}
		}
		
	}
	
	public int getTotalItems() {
		
		int totalItems = 0;
		
		for (int i = 36; i != 54; i++) {
			ItemStack cartItem = getInventory().getContents()[i];
			if (cartItem == null)
				continue;
			if (cartItem.getType() == item) {
				totalItems += cartItem.getAmount();
			}
		}
		
		return totalItems;
		
	}
	
	public int getTotalCost() {
		return getTotalItems() * cost;
	}
	
}
