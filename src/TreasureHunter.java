/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all of the display based on the messages it receives from the Town object.
 *
 */
import java.util.Scanner;

public class TreasureHunter
{
    //Instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private boolean easyMode;
    private String mode;

    //Constructor
    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter()
    {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        mode = "normal";
    }

    // starts the game; this is the only public method
    public void play ()
    {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to TREASURE HUNTER!");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = scanner.nextLine();

        // set hunter instance variable
        hunter = new Hunter(name, 10);

        System.out.print("Hard mode/Normal mode/Easy mode? (h/n/e): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("h")) {
            mode = "hard";
        } else if (choice.equalsIgnoreCase("e")) {
            mode = "easy";
        } else if (choice.equalsIgnoreCase("cheat")){
            mode = "cheat";
        } else {
            mode = "normal";
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown()
    {
        double markdown = 0.25;
        double toughness = 0.4;
        if (mode.equalsIgnoreCase("hard"))
        {
            // in hard mode, you get less money back when you sell items
            markdown = 0.15;

            // and the town is "tougher"
            toughness = 0.75;
        }
        else if(mode.equalsIgnoreCase("easy")){
            markdown = .75;
            toughness = .1;
        } else if (mode.equals("cheat")) {
            markdown = 100;
            toughness = 0;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown, mode);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, mode, toughness);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu()
    {
        Scanner scanner = new Scanner(System.in);
        String choice = "";

        while (!(choice.equals("X") || choice.equals("x")) && !(Town.checkIfAllTreasure()) && !(hunter.getGold() <= 0))
        {
            System.out.println();
            System.out.println(currentTown.getLatestNews());
            System.out.println("***");
            System.out.println(hunter);
            System.out.println(currentTown);
            System.out.println("(B)uy something at the shop.");
            System.out.println("(S)ell something at the shop.");
            System.out.println("(M)ove on to a different town.");
            System.out.println("(L)ook for trouble!");
            System.out.println("(H)unt for treasure!");
            System.out.println("Give up the hunt and e(X)it.");
            System.out.println();
            System.out.print("What's your next move? ");
            choice = scanner.nextLine();
            choice = choice.toUpperCase();
            processChoice(choice);
        }
        if (Town.checkIfAllTreasure()) {
            System.out.println("\nYou have found all three of the most valuable treasures in this world and decide to end your journey for now...");
        } else if (hunter.getGold() <= 0) {
            System.out.println(currentTown.getLatestNews());
            System.out.println("\nYou have lost all your money in a battle and your opponent wants your head as payment. Your journey has ended. Maybe try not to get too overconfident next time?");
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice)
    {
        choice = choice.toLowerCase();
        if (choice.equals("b")  || choice.equals("s"))
        {
            currentTown.enterShop(choice);
        }
        else if (choice.equals("m"))
        {
            if (currentTown.leaveTown())
            {
                //This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
            }
        }
        else if(choice.equals("h")){
            System.out.println(currentTown.generateTreasure(hunter));
        }
        else if (choice.equals("l"))
        {
            currentTown.lookForTrouble();
        }
        else if (choice.equals("x"))
        {
            System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
        }
        else
        {
            System.out.println("Yikes! That's an invalid option! Try again.");
        }
    }
}