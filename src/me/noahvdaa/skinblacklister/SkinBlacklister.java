package me.noahvdaa.skinblacklister;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import me.noahvdaa.skinblacklister.Metrics;
import net.md_5.bungee.api.ChatColor;

public class SkinBlacklister extends JavaPlugin {
	
	private FileConfiguration config;
	private Metrics metrics;
	private Logger logger;
	private Double minMatchRate = 97.5;
	private String dataFolderPath;
	
	@Override
	public void onEnable() {
		this.logger = getLogger();
		
		this.saveDefaultConfig();
		
		this.config = getConfig();
		this.minMatchRate = this.config.getDouble("minMatchRate", 97.5);
		if(this.minMatchRate < 0 || this.minMatchRate > 100) {
			logger.info("Invalid minMatchRate. Changing it to the default (97.5)");
			this.config.set("minMatchRate", 97.5);
			this.saveConfig();
			this.minMatchRate = 97.5;
		}
		
		this.dataFolderPath = this.getDataFolder().getPath()+"/blacklisted_skins";
		boolean madeDirs = new File(dataFolderPath).mkdirs();
		
		if(!madeDirs && !new File(this.dataFolderPath).exists()) {
			logger.info("Failed to make folder "+dataFolderPath+". The plugin will be disabled.");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		File f = new File(dataFolderPath);
        int count = 0;
        for (File file : f.listFiles()) {
        	if (file.isFile() && file.getName().endsWith(".png") && !file.getName().startsWith(".")) {
            	count++;
        	}
        }
        
        logger.info("Loaded "+count+" blacklisted skins, with a minimal match rate of "+this.minMatchRate+"%.");
        
        // Register events
        this.getServer().getPluginManager().registerEvents(new SkinChecker(this), this);
		
        // Metrics start here.
		this.metrics = new Metrics(this);
		this.metrics.addCustomChart(new Metrics.SingleLineChart("blacklisted_skins", new Callable<Integer>() {
	        @Override
	        public Integer call() throws Exception {
	        	File f = new File(dataFolderPath);
	            int count = 0;
	            for (File file : f.listFiles()) {
	            	if (file.isFile()) {
	                	count++;
	            	}
	            }
	            
	            return count;
	        }
	    }));
	}
	
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
	    if(!sender.hasPermission("skinblacklister.reload")) {
	    	sender.sendMessage(ChatColor.RED + "Sorry, you don't have enough permissions.");
	    }else {
	    	sender.sendMessage(ChatColor.GOLD + "Reloading config...");
	    	reloadConfig();
	    	sender.sendMessage(ChatColor.GREEN + "Reloaded config!");
	    	
	    	this.minMatchRate = this.config.getDouble("minMatchRate", 97.5);
			if(this.minMatchRate < 0 || this.minMatchRate > 100) {
				logger.info("Invalid minMatchRate. Changing it to the default (97.5)");
				this.config.set("minMatchRate", 97.5);
				this.saveConfig();
				this.minMatchRate = 97.5;
			}
	    }
	    
	    return true;
	}

}
