package me.noahvdaa.skinblacklister;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
public class SkinChecker implements Listener {
	
	private SkinBlacklister plugin;
	
	public SkinChecker(SkinBlacklister plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent e)  {
		Player p = e.getPlayer();
		if(p.hasPermission("skinblacklister.bypass")) return;
		plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
			  public void run() {
				  try {
					  if(!p.isOnline()) return;
					  URL url = new URL("https://crafatar.com/skins/"+e.getPlayer().getUniqueId());
					  BufferedImage img = ImageIO.read(url);
					  
					  String dataFolderPath = plugin.getDataFolder().getPath()+"/blacklisted_skins";
					  File f = new File(dataFolderPath);
					  
					  Double mostMatchPercent = 0.0;
					  
					  String mostMatchName = "";

				      for (File file : f.listFiles()) {
				    	  if (file.isFile() && file.getName().endsWith(".png") && !file.getName().startsWith(".")) {
				    		  BufferedImage img2 = ImageIO.read(file);
				    		  Double matchRate = 100-getDifferencePercent(img, img2);
				    		  
				    		  if(matchRate > mostMatchPercent) {
				    			  mostMatchPercent = matchRate;
					    		  mostMatchName = file.getName();
				    		  }
				    	  }
				      }
				      
				      final String mmn = mostMatchName;
				      final String mmp = round(mostMatchPercent, 2).toString();
				     
				      if(mostMatchPercent > plugin.getConfig().getDouble("minMatchRate", 97.5)) {
				    	  // Match found!
				    	  plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

							@Override
							public void run() {
								if(!p.isOnline()) return;
								for (String command : plugin.getConfig().getStringList("commandsToExecute")) {
									command = command.replaceAll("%playername%", p.getName())
								    .replaceAll("%matchedfile%", mmn)
									.replaceAll("%matchpercentage%", mmp);
									
									Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
								}
							}
				    	  
				    	  }, 1L);
				    	  
				      }
				      

				  } catch(Exception err) {
					  // Silently fail.
				  }
			  }
			}, 1L);
	}
	
	private static double getDifferencePercent(BufferedImage img1, BufferedImage img2) {
        int width = img1.getWidth();
        int height = img1.getHeight();
        int width2 = img2.getWidth();
        int height2 = img2.getHeight();
        if (width != width2 || height != height2) {
            throw new IllegalArgumentException(String.format("Images must have the same dimensions: (%d,%d) vs. (%d,%d)", width, height, width2, height2));
        }
 
        long diff = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                diff += pixelDiff(img1.getRGB(x, y), img2.getRGB(x, y));
            }
        }
        long maxDiff = 3L * 255 * width * height;
 
        return 100.0 * diff / maxDiff;
    }
 
    private static int pixelDiff(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >>  8) & 0xff;
        int b1 =  rgb1        & 0xff;
        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >>  8) & 0xff;
        int b2 =  rgb2        & 0xff;
        return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
    }
    
    private static Double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
