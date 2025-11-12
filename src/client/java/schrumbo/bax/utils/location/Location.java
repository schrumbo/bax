package schrumbo.bax.utils.location;

public enum Location {
    PrivateIsland("Private Island"),
    Garden("Garden"),
    Galatea("Galatea"),
    Hub("Hub"),
    FarmingIslands("The Farming Islands"),
    ThePark("The park"),
    SpidersDen("Spider's Den"),
    TheEnd("The End"),
    CrimsonIsle("Crimson Isle"),
    GoldMine("Gold Mine"),
    DeepCaverns("Deep Caverns"),
    DwarvenMines("Dwarven Mines"),
    Mineshaft("Mineshaft"),
    CrystalHollows("Crystal Hollows"),
    JerrysWorkshop("Jerry's Workshop"),
    DungeonHub("Dungeon Hub"),
    Dungeon("Catacombs"),
    Therift("The Rift"),
    BackwaterBayou("Backwater Bayou");

    private final String displayName;

    Location(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Location isLocation(String location) {
        for (Location loc : Location.values()) {
            if (loc.displayName.equals(location)) {
                return loc;
            }
        }
        return null;
    }
}


