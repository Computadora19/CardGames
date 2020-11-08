package President;

import java.util.ArrayList;
import java.util.Scanner;

public class President
{

    private ArrayList<Player> players = new ArrayList<Player>();

    public void addPlayers(int numPlayers)
    {

        for (int i = 0; i < numPlayers; i++)
        {

            players.add(new Player(i + 1));
        }

    }

    public void setUpNumOfPlayers()
    {
        System.out.println("Welcome To President \n");

        Scanner kbd = new Scanner(System.in);

        System.out.println("How many players are going to play in the game?");
        System.out.println("(Mininumum # of players is 4 and Maximum # of players is 6) ");

        String numberOfPlayers = kbd.nextLine();

        // if user gives a non-numerical answer
        // continue prompting user until they give a numeric answer
        while (!numberOfPlayers.matches("[0-9]+"))
        {
            System.out.println("Please enter a number for the number of players in this game");

            numberOfPlayers = kbd.nextLine();
        }

        // Convert the string input into an integer
        int numPlayers = Integer.parseInt(numberOfPlayers);

        // If the user puts a number greater than 6 or less than 4
        // Continue prompting the user until they give
        // a number between 2 and 4
        while (numPlayers < 4 || numPlayers > 7)
        {
            System.out.println("Please keep the number of players between 4 and 6");

            // get user input
            numberOfPlayers = kbd.nextLine();

            // if user gives a non-numerical answer
            // continue prompting user until they give a numeric answer
            while (!numberOfPlayers.matches("[0-9]+"))
            {
                System.out.println("Please enter a number");

                // get user input
                numberOfPlayers = kbd.nextLine();
            }

            // convert the user input into an integer
            numPlayers = Integer.parseInt(numberOfPlayers);
        }

        // add the players to the game
        addPlayers(numPlayers);

        System.out.println("\n" + numPlayers + " players have been added to the game\n");
    }

    public void setUpPlayers()
    {
        Scanner kbd = new Scanner(System.in);

        // Create a deck of 52 cards
        Deck deck = new Deck();

        // shuffle the game deck
        deck.shuffle();

        // calculate how many cards we are supposed to distribute to each player
        int howManyCardsToEachPlayer = deck.getSize() / players.size();

        // if we can't give each player the same amount of cards
        if (deck.getSize() % players.size() != 0)
        {
            // calculate how many cards would be left over if we gave each player
            // roughly the same number of cards
            int leftOverCards = deck.getSize() % players.size();

            // and then give a certain number of random players
            // (that number is determined by the number of leftover cards left)
            // a card
            for (int i = 0; i < leftOverCards; i++)
            {
                players.get(i).addOneToPlayerHand(deck.draw());
            }

        }

        // loop through the list of players and give each player a name and their
        // cards
        for (int i = 0; i < players.size(); i++)
        {
            System.out.println("What is player " + (i + 1) + "'s name:");

            String playerName = kbd.nextLine();

            // ask each player for their name
            players.get(i).setName(playerName);

            // draw a predetermined number of cards from the deck
            ArrayList<Card> cardsPerPerson = deck.draw(howManyCardsToEachPlayer);

            // give this player the predetermined number of cards
            players.get(i).addMultipleToPlayerHand(cardsPerPerson);

            // make sure that each status of the player is set to false
            // their out status at the beginning of each round should be set to false
            players.get(i).setOut(false);

            // their president status at the beginning of the first round should be set to false
            players.get(i).setPresident(false);

            // their vice president status at the beginning of the first round should be set to false
            players.get(i).setVicePresident(false);

            // their scum status at the beginning of the first round should be set to false
            players.get(i).setScum(false);

            // their vice scum status at the beginning of the first round should be set to false
            players.get(i).setViceScum(false);

            System.out.println(players.get(i).getName() + " has " + players.get(i).getNumOfPlayerCards() + " cards");

            System.out.println("__________________________________________________\n");

        }
    }

    public void setUpGame()
    {
        Scanner kbd = new Scanner(System.in);

        System.out.println("What is the number of rounds you want?\n");
        System.out.println("The min # is 1 and the max # is 10");
        String numRounds = kbd.nextLine();

        // if user gives a non-numerical answer
        // continue prompting user until they give a numeric answer
        while (!numRounds.matches("[0-9]+"))
        {
            System.out.println("Please enter a number for the number of rounds in this new game");

            numRounds = kbd.nextLine();
        }

        // convert input into an integer
        int numberOfRounds = Integer.parseInt(numRounds);

        // Continue promoting the user until they provide
        // a number between 0 and 5
        while (numberOfRounds > 10 || numberOfRounds < 1)
        {
            System.out.println("Please enter a number that is between 1 and 10");

            // get user input
            numRounds = kbd.nextLine();

            // continue prompting user until the user gives a number
            while (!numRounds.matches("[0-9]+"))
            {
                System.out.println("Please enter a number for the number of rounds in this new game");

                numRounds = kbd.nextLine();
            }
            // convert the user input into an integer
            numberOfRounds = Integer.parseInt(numRounds);
        }

        System.out.println("__________________________________________________\n");

        startGame(numberOfRounds);
    }

    public void startGame(int numRounds)
    {

        Scanner kbd = new Scanner(System.in);

        ArrayList<Card> middleCards = new ArrayList<Card>();

        Card threeOfClubs = new Card(Suit.CLUBS, Value.THREE);

        int rounds = 0;

        int whoseTurn = 0;

        // loop through the players participating in the game
        for (int j = 0; j < players.size(); j++)
        {
            // if this player has the three of clubs
            if (players.get(j).containsThisCard(threeOfClubs))
            {
                // this person will be the first player to put down a card
                whoseTurn = j;

                // loop through the player's hand
                for (int i = 1; i < players.get(j).getPlayerHand().size() + 1; i++)
                {
                    // if the player's card is equal to the one we are looking for (in this case a three of clubs)
                    if (players.get(j).getCardInPlayerCards(i).equals(threeOfClubs))
                    {

                        // remove the card from the person's hand and place it in the middle of table
                        middleCards.add(players.get(whoseTurn).removeOneFromPlayerCards(i));

                    }
                }

                break;
            }
        }

        // since the player that has a three of clubs put down their card in the middle, it's the next player's turn
        whoseTurn = changeTurn(whoseTurn);

        // continue playing the game until there are no more rounds left
        while (numRounds != rounds)
        {

            boolean putDownSingle = true;

            boolean putDownDouble = false;

            boolean putDownTriple = false;

            boolean putDownQuadruple = false;

            // continue playing this round until everyone is out
            while (isEveryoneOut() == false)
            {

                System.out.println("Round " + rounds);
                if (putDownSingle)
                {
                    System.out.println("RULE: YOU CAN ONLY PUT DOWN ONE CARD OF THE SAME RANK\n");
                }

                else if (putDownDouble)
                {
                    System.out.println("RULE: YOU CAN ONLY PUT DOWN TWO CARDS OF THE SAME RANK\n");

                }

                else if (putDownTriple)
                {
                    System.out.println("RULE: YOU CAN ONLY PUT DOWN THREE CARDS OF THE SAME RANK\n");

                }

                else
                {
                    System.out.println("RULE: YOU CAN ONLY PUT DOWN FOUR CARDS OF THE SAME RANK\n");

                }
                System.out.println("Middle Card:");

                if (middleCards.size() > 0)
                {
                    System.out.println(middleCards.get(middleCards.size() - 1));

                }

                // System.out.println("---------------------------------------------------\n");

                // show this player's hand
                players.get(whoseTurn).showPlayerCards();

                System.out.println("Which of these " + players.get(whoseTurn).getNumOfPlayerCards() + " cards do you wish to choose ?");

                // ask the player which card they want to put down
                String selectedCard = kbd.nextLine();

                // if user gives a non-numerical answer
                // continue prompting user until they give a numeric answer
                while (!selectedCard.matches("[0-9]+"))
                {
                    System.out.println("Please enter a number for the card you wish to put down in this game");

                    selectedCard = kbd.nextLine();
                }

                // Convert the string input into an integer
                int whichCard = Integer.parseInt(selectedCard);

                // If the user puts a number greater than the number of cards in the player's hand or less than 1
                // Continue prompting the user until they give
                // a number between 1 and the # of cards in the player's hand
                while (whichCard < 1 || whichCard > players.get(whoseTurn).getNumOfPlayerCards())
                {
                    System.out.println("Please choose a card that's within the range of between 1 and " + players.get(whoseTurn).getNumOfPlayerCards());

                    // get user input
                    selectedCard = kbd.nextLine();

                    // if user gives a non-numerical answer
                    // continue prompting user until they give a numeric answer
                    while (!selectedCard.matches("[0-9]+"))
                    {
                        System.out.println("Please enter a number");

                        // get user input
                        selectedCard = kbd.nextLine();
                    }

                    // convert the user input into an integer
                    whichCard = Integer.parseInt(selectedCard);
                }

                Card thisCard = players.get(whoseTurn).getCardInPlayerCards(whichCard);

                while (isValidSelection(thisCard, middleCards.get(middleCards.size() - 1)) == false)
                {
                    System.out.println("Please choose a card that is higher than the middle card or a two");

                    whichCard = 0;

                    while (whichCard < 1 || whichCard > players.get(whoseTurn).getNumOfPlayerCards())
                    {

                        System.out.println("Please keep the selected card number between 1 and " + players.get(whoseTurn).getNumOfPlayerCards());

                        whichCard = 0;

                        // get user input
                        selectedCard = kbd.nextLine();

                        // if user gives a non-numerical answer
                        // continue prompting user until they give a numeric answer
                        while (!selectedCard.matches("[0-9]+"))
                        {
                            System.out.println("Please enter a number");

                            // get user input
                            selectedCard = kbd.nextLine();
                        }

                        // convert the user input into an integer
                        whichCard = Integer.parseInt(selectedCard);
                    }

                    thisCard = players.get(whoseTurn).getCardInPlayerCards(whichCard);

                }

                // remove the selected card from the player's hand and put it down in the middle
                middleCards.add(players.get(whoseTurn).removeOneFromPlayerCards(whichCard));

                // if the card that's been placed down is a two, clear the middle cards
                if (thisCard.getValue() == Value.TWO || middleCards.size() == 0)
                {
                    middleCards.clear();

                    // show this player's hand
                    players.get(whoseTurn).showPlayerCardsWithNumTimesTheyAppear();

                    System.out.println("How many cards do you wish to put down at the same time?\n");
                    System.out.println("1. Singles");
                    System.out.println("2. Doubles");
                    System.out.println("3. Triples");
                    System.out.println("4. Quadruples");

                    // ask the player how many cards they want to put down on their turn
                    String howManyCardsOfSameRank = kbd.nextLine();

                    // if user gives a non-numerical answer
                    // continue prompting user until they give a numeric answer
                    while (!howManyCardsOfSameRank.matches("[0-9]+"))
                    {
                        System.out.println("Please enter a number for the number of cards of the same rank you want to put down");

                        howManyCardsOfSameRank = kbd.nextLine();
                    }

                    // Convert the string input into an integer
                    int manyCardsOfSameRank = Integer.parseInt(howManyCardsOfSameRank);

                    // If the user puts a number greater than 4 or less than 1
                    // or if user does not have their selcted number of cards of the same rank
                    // Continue prompting the user until they give
                    // a number between 1 and the # of cards in the player's hand
                    while (manyCardsOfSameRank < 1 || manyCardsOfSameRank > 4 || players.get(whoseTurn).hasManyCardsOfSameRank(manyCardsOfSameRank) == false)
                    {

                        // Displays the correct error message depending on what the user selected
                        // if the user wanted to try put down a certain number of cards of the same rank that they don't have in their hand, this message will be displayed
                        if (players.get(whoseTurn).hasManyCardsOfSameRank(manyCardsOfSameRank) == false)
                        {
                            if (manyCardsOfSameRank == 2)
                            {
                                System.out.println("You do not have two cards of the same rank. Choose a different option.");

                            }

                            else if (manyCardsOfSameRank == 3)
                            {
                                System.out.println("You do not have three cards of the same rank. Choose a different option.");

                            }

                            else
                            {
                                System.out.println("You do not have four cards of the same rank. Choose a different option.");

                            }

                        }

                        // if the user tried to put down more than 4 cards of the same rank or less than one card of the same rank, this message will be displayed
                        if (manyCardsOfSameRank < 1 || manyCardsOfSameRank > 4)
                        {
                            System.out.println("Please choose a card that's within the range of 1 and 4");
                        }

                        // get user input
                        howManyCardsOfSameRank = kbd.nextLine();

                        // if user gives a non-numerical answer
                        // continue prompting user until they give a numeric answer
                        while (!howManyCardsOfSameRank.matches("[0-9]+"))
                        {
                            System.out.println("Please enter a number");

                            // get user input
                            howManyCardsOfSameRank = kbd.nextLine();
                        }

                        // convert the user input into an integer
                        manyCardsOfSameRank = Integer.parseInt(howManyCardsOfSameRank);
                    }

                    // if the user wanted to put down two cards in the middle, make sure every boolean variable is false for the upcoming turns
                    // but the one that controls how many singles can be put down
                    if (manyCardsOfSameRank == 1)
                    {
                        putDownSingle = true;

                        putDownDouble = false;

                        putDownTriple = false;

                        putDownQuadruple = false;

                    }
                    // if the user wanted to put down two cards in the middle, make sure every boolean variable is false for the upcoming turns
                    // but the one that controls how many doubles can be put down
                    else if (manyCardsOfSameRank == 2)
                    {
                        putDownSingle = false;

                        putDownDouble = true;

                        putDownTriple = false;

                        putDownQuadruple = false;
                    }

                    // if the user wanted to put down three cards in the middle, make sure every boolean variable is false for the upcoming turns
                    // but the one that controls how many triples can be put down
                    else if (manyCardsOfSameRank == 3)
                    {
                        putDownSingle = false;

                        putDownDouble = false;

                        putDownTriple = true;

                        putDownQuadruple = false;
                    }

                    // if the user wanted to put down four cards in the middle, make sure every boolean variable is false for the upcoming turns
                    // but the one that controls how many quadruples can be put down

                    else if (manyCardsOfSameRank == 4)
                    {
                        putDownSingle = false;

                        putDownDouble = false;

                        putDownTriple = false;

                        putDownQuadruple = true;
                    }

                    // now the player can put down another card
                    System.out.println("Which of these " + players.get(whoseTurn).getNumOfPlayerCards() + " cards do you wish to choose ?");

                    // ask the player which card they want to put down
                    selectedCard = kbd.nextLine();

                    // if user gives a non-numerical answer
                    // continue prompting user until they give a numeric answer
                    while (!selectedCard.matches("[0-9]+"))
                    {
                        System.out.println("Please enter a number for the card you wish to put down in this game");

                        selectedCard = kbd.nextLine();
                    }

                    // Convert the string input into an integer
                    whichCard = Integer.parseInt(selectedCard);

                    // If the user puts a number greater than the number of cards in the player's hand or less than 1
                    // Or if the user selects a card whose rank doesn't appear the number of times the user selected in the previous prompt
                    // (ex. if the user selected they wanted to put down two cards of the same rank in the middle, they need to select a card whose rank appears at least twice in their hand)
                    // Continue prompting the user until they give
                    // a number between 1 and the # of cards in the player's hand
                    while (whichCard < 1 || whichCard > players.get(whoseTurn).getNumOfPlayerCards() || players.get(whoseTurn).howManyTimesThisRankAppears(whichCard - 1) < manyCardsOfSameRank)
                    {

                        // Warning messages depending on the mistake the user made
                        // if the user chose a card that's not in the range of cards they have in their hand, this message will be displayed
                        if (whichCard < 1 || whichCard > players.get(whoseTurn).getNumOfPlayerCards())
                        {
                            System.out.println("Please choose a card that's within the range of between 1 and " + players.get(whoseTurn).getNumOfPlayerCards());

                        }

                        // If the user chose a card whose rank appears less than the specified number of times, this message will be displayed
                        else
                        {
                            System.out.println("Please choose a card whose rank appears equal to or more than " + manyCardsOfSameRank);

                        }

                        // get user input
                        selectedCard = kbd.nextLine();

                        // if user gives a non-numerical answer
                        // continue prompting user until they give a numeric answer
                        while (!selectedCard.matches("[0-9]+"))
                        {
                            System.out.println("Please enter a number");

                            // get user input
                            selectedCard = kbd.nextLine();
                        }

                        // convert the user input into an integer
                        whichCard = Integer.parseInt(selectedCard);
                    }

                    ArrayList<Card> cardsToPutDown = new ArrayList<Card>();

                    System.out.println(players.get(whoseTurn).getCardInPlayerCards(whichCard).getValue());

                    for (int i = 1; i < players.get(whoseTurn).getNumOfPlayerCards() + 1; i++)
                    {
                        if (players.get(whoseTurn).getCardInPlayerCards(i).getValue() == players.get(whoseTurn).getCardInPlayerCards(whichCard).getValue())
                        {

                            cardsToPutDown.add(players.get(whoseTurn).getCardInPlayerCards(i));
                        }

                    }

                    // if user wants to put down just one card down in the middle
                    if (putDownSingle)
                    {
                        // remove the selected card from the player's hand and put it down in the middle
                        middleCards.add(players.get(whoseTurn).removeOneFromPlayerCards(whichCard));
                    }

                    // if the user wants to put down two cards or three in the middle and there happens to be exactly two or three
                    // cards of the same rank then instead of asking the user which of those two cards they
                    // wish to put down, put them down for the user
                    else if (cardsToPutDown.size() == 2 || cardsToPutDown.size() == 3)
                    {
                        middleCards.addAll(cardsToPutDown);

                        players.get(whoseTurn).removeMultipleFromPlayerCards(cardsToPutDown);
                    }

                    // if they user wants to put down four cards at the same time
                    // they will clear the deck and it will be their turn again
                    else if (putDownQuadruple)
                    {

                        // add all the cards of the same rank and put it in the middle
                        middleCards.addAll(cardsToPutDown);

                        // get rid of all those cards from the player's hand
                        players.get(whoseTurn).removeMultipleFromPlayerCards(cardsToPutDown);

                        // since four cards have been placed down in the middle
                        // the middle cards can be cleared
                        middleCards.clear();

                        // this will allow the user to put down more cards
                        continue;

                    }

                    // if the user decides they want to put down two or three cards of the same rank down and
                    // there are three or four of those cards in their hand respectively
                    else
                    {
                        // loop through the cards of the same rank
                        for (int i = 1; i < cardsToPutDown.size() + 1; i++)

                        {
                            // print them out
                            System.out.println((i) + ": (" + players.get(whoseTurn).getCardInPlayerCards(i) + ") ");

                        }

                        // now loop as many times as how many cards of the same rank the user wants to put down
                        while (manyCardsOfSameRank > 0)
                        {

                            System.out.println("Which card of the same rank do you wish do you wish to put down?");

                            String whichCardOfSameRank = kbd.nextLine();

                            // if user gives a non-numerical answer
                            // continue prompting user until they give a numeric answer
                            while (!whichCardOfSameRank.matches("[0-9]+"))
                            {
                                System.out.println("Please enter a number for the card of the same rank you want to put down");

                                whichCardOfSameRank = kbd.nextLine();
                            }

                            // Convert the string input into an integer
                            int selectedCardOfSameRank = Integer.parseInt(whichCardOfSameRank);

                            // If the user puts a number greater than 6 or less than 4
                            // Continue prompting the user until they give
                            // a number between 2 and 4
                            while (selectedCardOfSameRank < 1 || selectedCardOfSameRank > cardsToPutDown.size())
                            {
                                System.out.println("Please keep the number of players between 1 and " + cardsToPutDown.size());

                                // get user input
                                whichCardOfSameRank = kbd.nextLine();

                                // if user gives a non-numerical answer
                                // continue prompting user until they give a numeric answer
                                while (!whichCardOfSameRank.matches("[0-9]+"))
                                {
                                    System.out.println("Please enter a number");

                                    // get user input
                                    whichCardOfSameRank = kbd.nextLine();
                                }

                                // convert the user input into an integer
                                selectedCardOfSameRank = Integer.parseInt(whichCardOfSameRank);
                            }

                            manyCardsOfSameRank--;
                        }

                    }

                }

                // change turns
                whoseTurn = changeTurn(whoseTurn);

                System.out.println("__________________________________________________\n");

            }

            rounds++;

        }

    }

    /***
     * Checks to see if the player has put down card that can be placed on top of the card in the middle
     * @param selectedCard
     * @param middleCard
     * @return
     */
    public boolean isValidSelection(Card selectedCard, Card middleCard)
    {

        if (selectedCard.getValue() == Value.TWO || selectedCard.getValueOfCard() > middleCard.getValueOfCard())
        {
            return true;
        }

        return false;

    }

    /***
     * Determine which player goes next in the game
     * @param currentTurn
     * @return
     */
    public int changeTurn(int currentTurn)
    {
        // If it's the last person's turn then we need to reset whoseTurn to 0
        // So we can start off with the first player in the list of players
        if (currentTurn + 1 == players.size())
        {

            currentTurn = 0;
        }

        // It's the next player's turn, add one more to whoseTurn
        else
        {
            currentTurn++;
        }

        return currentTurn;
    }

    /***
     * Checks to see if every player in the game is out of the game for that round
     * @return
     */
    public boolean isEveryoneOut()
    {
        for (Player p : players)
        {
            if (p.isOut() == false)
            {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args)
    {
        President pres = new President();

        pres.setUpNumOfPlayers();

        pres.setUpPlayers();

        pres.setUpGame();
    }
}
