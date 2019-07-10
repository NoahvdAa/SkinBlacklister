package me.noahvdaa.skinblacklister.bungee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class SkinBlacklisterBungee extends Plugin {
	
	public Configuration config;
	private MetricsBungee metrics;
	private Logger logger;
	private Double minMatchRate = 97.5;
	private String dataFolderPath;
	
	@Override
    public void onEnable() {
		this.logger = getLogger();
		
		if(!configExists()) saveDefaultConfig();
		
		loadConfig();
		
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
			this.getProxy().getPluginManager().unregisterListeners(this);
	        this.getProxy().getPluginManager().unregisterCommands(this);
	        this.onDisable();
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
        
        // Register commands
        getProxy().getPluginManager().registerCommand(this, new ReloadCommand());
        
        // Register events
        this.getProxy().getPluginManager().registerListener(this, new SkinCheckerBungee(this));
        
        // Metrics start here.
		this.metrics = new MetricsBungee();
		this.metrics.addCustomChart(new MetricsBungee.SingleLineChart("blacklisted_skins", new Callable<Integer>() {
	        @Override
	        public Integer call() throws Exception {
	        	File f = new File(dataFolderPath);
	            int count = 0;
	            for (File file : f.listFiles()) {
	            	if (file.isFile() && file.getName().endsWith(".png") && !file.getName().startsWith(".")) {
	                	count++;
	            	}
	            }
	            
	            return count;
	        }
	    }));
    }
	
	private boolean configExists() {
		if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");
   
        return file.exists();
	}
	
	private void loadConfig() {
		try {
			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			this.logger.severe("Failed to load config. Replacing it with default config...");
			saveDefaultConfig();
		}
	}
	
	private void saveDefaultConfig() {
		File file = new File(getDataFolder(), "config.yml");
		try (InputStream in = getResourceAsStream("config.yml")) {
            Files.copy(in, file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void saveConfig() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			this.logger.severe("Failed to save config. :(");
		}
	}
	
	class ReloadCommand extends Command {

		public ReloadCommand() {
			super("sblbr", "skinblacklister.reload", "skinblacklisterbungeereload");
		}

		@Override
		public void execute(CommandSender sender, String[] args) {
			sender.sendMessage(new TextComponent(ChatColor.GOLD + "Reloading config..."));
			loadConfig();
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "Reloaded config!"));
		}
		
	}
}
