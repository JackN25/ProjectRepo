/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all of the things a Hunter can do in town.
 */
public class Town
{
    //instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private String mode;
    private int count = 0;
    private static boolean TREASURE_1_FOUND = false;
    private static boolean TREASURE_2_FOUND = false;
    private static boolean TREASURE_3_FOUND = false;



    //Constructor
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     * @param s The town's shoppe.
     * @param t The surrounding terrain.
     */
    public Town(Shop shop, String mode, double toughness)
    {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        this.mode = mode;
        toughTown = (Math.random() < toughness);
    }

    public String getLatestNews()
    {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     * @param h The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter)
    {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown)
        {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        }
        else
        {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown()
    {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown)
        {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak())
            {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, your " + item + " broke.";
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    public void enterShop(String choice)
    {
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble()
    {
        double noTroubleChance;
        if (mode.equals("hard"))
        {
            noTroubleChance = 0.66;
        }
        else if (mode.equals("normal"))
        {
            noTroubleChance = 0.33;
        } else if (mode.equals("cheat")){
            noTroubleChance = 0;
        } else {
            noTroubleChance = 0.1;

        }

        if (Math.random() < noTroubleChance)
        {
            printMessage = "You couldn't find any trouble";
        }
        else
        {
            printMessage = "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            int goldDiff;
            if (!mode.equals("cheat")) {
                goldDiff = (int) ((Math.random() * 10) * 1 - noTroubleChance) + 1;
            } else {
                goldDiff = 100;
            }
            if (Math.random() > noTroubleChance)
            {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " +  goldDiff + " gold.";
                hunter.changeGold(goldDiff);
            }
            else
            {
                printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                printMessage += "\nYou lost the brawl and pay " +  goldDiff + " gold.";
                hunter.changeGold(-1 * goldDiff);
            }
        }
    }

    public String generateTreasure(Hunter G){
        int randomValue = (int)(Math.random()*5);
        if (count > 0){
            return "You cannot search for another treasure until you leave and go to the next town.";
        }
        else{
            count++;
            if(randomValue == 1){
                return "You went on a journey to look for the town's treasure. After finding the spot and digging for 3 hours, you found no treasure and give up.";
            }
            else if(randomValue == 2) {
                if (TREASURE_1_FOUND) {
                    return "You have already obtained this treasure. It has been discarded from your inventory.";
                } else {
                    TREASURE_1_FOUND= true;
                    G.addItem("Diamond Skull");
                    return "You went on a journey to look for the town's treasure. After finding the spot and digging for 3 hours, you found the... DIAMOND SKULL!";
                }
            }
            else if(randomValue == 3) {
                if (TREASURE_2_FOUND) {
                    return "You have already obtained this treasure. It has been discarded from your inventory.";
                } else {
                    TREASURE_2_FOUND = true;
                    G.addItem("Ruby Crown");
                    return "You went on a journey to look for the town's treasure. After finding the spot and digging for 3 hours, you found the... RUBY CROWN!";
                }
            }
            else {
                if (TREASURE_3_FOUND) {
                    return "You have already obtained this treasure. It has been discarded from your inventory.";
                } else {
                    TREASURE_3_FOUND = true;
                    G.addItem("Golden Trident");
                    return "You went on a journey to look for the town's treasure. After finding the spot and digging for 3 hours, you found the... GOLDEN TRIDENT!";
                }
            }
       }
    }

    public String toString()
    {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain()
    {
        double rnd = Math.random();
        if (rnd < .2)
        {
            return new Terrain("Mountains", "Rope");
        }
        else if (rnd < .4)
        {
            return new Terrain("Ocean", "Boat");
        }
        else if (rnd < .6)
        {
            return new Terrain("Plains", "Horse");
        }
        else if (rnd < .8)
        {
            return new Terrain("Desert", "Water");
        }
        else
        {
            return new Terrain("Jungle", "Machete");
        }
    }

    /**
     * Determines whether or not a used item has broken.
     * @return true if the item broke.
     */
    private boolean checkItemBreak()
    {
        if(mode.equalsIgnoreCase("easy")){
            double rand = Math.random();
            return (rand < 0.8);
        }
        double rand = Math.random();
        return (rand < 0.5);
    }

    public static  boolean checkIfAllTreasure(){
        if (TREASURE_1_FOUND && TREASURE_2_FOUND && TREASURE_3_FOUND) {
            return true;
        }
        return false;
    }
}