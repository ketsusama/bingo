package be.ketsu.bingo.utils;

import be.ketsu.bingo.BingoBukkit;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.Random;

@UtilityClass
public class LocationsUtils {

    /***
     * Get a random location on a circle pattern of location
     * @param centre - Center of location
     * @return
     */
    public Location getRandomLocationOnCircles(Location centre) {
        double radius = BingoBukkit.getInstance().getSettings().getSizeOfBeamArea(); // Radius from configuration
        Random rand = new Random();
        double angle = rand.nextFloat() * 2 * Math.PI; //Random angle
        double x = radius * Math.cos(angle);
        double z = radius * Math.sin(angle);
        Location location = centre.clone().add(new Vector(x, 0, z));
        location.setY(location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockY()) + 1);
        return location;
    }

    /***
     * Check if location is safe
     * @param location
     * @return
     */
    public boolean isSafeLocation(Location location) {
        return location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.LAVA;
    }

    /***
     * Get a safe location on a radius
     * @param location
     * @return
     */
    public Location getSafeLocation(Location location) {
        Location safeLocation = getRandomLocationOnCircles(location);
        while (!isSafeLocation(location)) {
            safeLocation = getRandomLocationOnCircles(location);
        }
        return safeLocation;
    }
}
