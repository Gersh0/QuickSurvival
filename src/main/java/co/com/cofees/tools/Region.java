package co.com.cofees.tools;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Region {
    private final String name;
    private final Location location1;
    private final Location location2;

    private final List<String> players;

    //private final List<String> players;


    public Region(String name, Location location1, Location location2, List<String> players) {
        this.name = name;
        this.location1 = location1;
        this.location2 = location2;
        //this.players = players;
        this.players = players;
    }


    public boolean isWithin(final Location location) {
        //may be eliminated
        if (!location.getWorld().getName().equals(this.location1.getWorld().getName())) return false;

        final Location[] centered = this.getCorrectedPoints();
        final Location primary = centered[0];
        final Location secondary = centered[1];

        final int x = (int) location.getX();
        final int y = (int) location.getY();
        final int z = (int) location.getZ();

        return x >= primary.getX() && x <= secondary.getX() &&
                y >= primary.getY() && y <= secondary.getY() &&
                z >= primary.getZ() && z <= secondary.getZ();
    }

    private Location[] getCorrectedPoints() {
        if (this.location1 == null || this.location2 == null) return null;

        //Valid.checkBoolean(this.location1.getWorld().getName().equals(this.location2.getWorld().getName()), )

        final int x1 = this.location1.getBlockX(), x2 = this.location2.getBlockX(),
                y1 = this.location1.getBlockY(), y2 = this.location2.getBlockY(),
                z1 = this.location1.getBlockZ(), z2 = this.location2.getBlockZ();

        final Location primary = this.location1.clone();
        final Location secondary = this.location2.clone();

        primary.setX(Math.min(x1, x2));
        primary.setY(Math.min(y1, y2));
        primary.setZ(Math.min(z1, z2));

        secondary.setX(Math.max(x1, x2));
        secondary.setY(Math.max(y1, y2));
        secondary.setZ(Math.max(z1, z2));

        return new Location[]{primary, secondary};

    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", this.name);
        map.put("location1", this.location1);
        map.put("location2", this.location2);
        map.put("players", this.players);
        return map;
    }


    public static Region deserialize(Map<String, Object> map) {
        String name = (String) map.get("name");
        Location location1 = (Location) map.get("location1");
        Location location2 = (Location) map.get("location2");
        List<String> players = (List<String>) map.get("players");
        return new Region(name, location1, location2,players);
    }

    public List<String>getPlayersOf(String regionName){
        if(this.name.equals(regionName)){
            return this.players;
        }
        return null;
    }
    public List<String> getPlayers() {
        return players;
    }

    public void addPlayer(String playerName){
        this.players.add(playerName);
    }

    public void deletePlayer(String playerName){
        this.players.remove(playerName);
    }

    public String getName() {
        return name;
    }

    public Location getLocation1() {
        return location1;
    }

    public Location getLocation2() {
        return location2;
    }

//    public List<String> getPlayers() {
//        return players;
//    }


}
