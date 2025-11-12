package schrumbo.bax.utils.entity;

import net.minecraft.entity.Entity;

public enum CustomPlayerEntity {
    //Dwarven Mines
    GlaciteWalker("Ice Walker"),
    CrystalSentry("Crystal Sentry"),
    Goblin("Goblin"),
    Weakling("Weakling"),
    TreasureHunter("Treasuer Hunter"),

    //CrystalHollows
    KalhuikiTribeMember("Kalhuiki Tribe"),

    //Glacite mineshaft
    GlaciteMage("Glacite Mage"),
    GlaciteCaver("Glacite Caver"),
    GlaciteBowman("Glacite Bowman"),

    //Dungeon Miniboss
    LostAdventurer("Lost Adventurer"),
    AngryArcheologist("Diamond Guy"),
    ShadowAssassin("Shadow Assassin"),

    //Dungeon Normal
    CryptDreadlord("Crypt Dreadlord"),
    ZombieCommander("Zombie Commander"),
    CryptSouleater("Crypt Souleater"),
    SkeletorPrime("Skeletor Prime"),

    //Rift
    Deadgehog("Deadgehog"),
    LeechSupreme("Leech Supreme"),
    Autonull("Autonull"),
    Fledgling("Fledgling"),
    Bloodfiend("Bloodfiend"),

    //Crimson Isle
    Barbarian("barbarian"),
    GoliathBarb("GoliathBarb"),
    BarbarianGuard("BarbarianGuard"),
    MageOutlaw("Mage Outlaw"),
    MageGuard("MageGuard"),
    Matcho("matcho"),
    FireMage("firemage"),

    //Galatea
    Ent("Ent"),

    //Diana
    Sphinx("Sphinx"),

    //Private island
    Vampire("Scion");

    private final String displayName;

    CustomPlayerEntity(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    //TODO CHECK IF IT WORKS
    public static CustomPlayerEntity getEntity(Entity entity) {
        for (CustomPlayerEntity ent : CustomPlayerEntity.values()) {
            if (ent.displayName.toLowerCase().contains(entity.getName().toString().toLowerCase())) {
                return ent;
            }
        }
        return null;
    }
}
