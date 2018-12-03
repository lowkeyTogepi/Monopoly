package edu.neumont.csc110;

import java.io.IOException;
import java.util.Random;

import edu.neumont.csc110.Player.PieceNames;
import interfaces.ConsoleUI;

public class Game {
	
	Random rnd = new Random();
	
	ChanceCard chanceCard = new ChanceCard();
	CommunityChestCard chestCards = new CommunityChestCard();
	PropSpace[] propSpace = new PropSpace[22];
	UtilSpace[] utilSpace = new UtilSpace[6];
	
	Player[] players = new Player[8];
	int playerCount = 0;
	public int turn = 0;
	
	public void printStartMenu() throws IOException, NoSuchFieldException {
		String[] menuOptions = new String[2];
		menuOptions[0] = "Quit Game";
		menuOptions[1] = "New Game";
		int selection = ConsoleUI.promptForMenuSelection(menuOptions, false);
		switch(selection) {
		case 0:
			System.out.println("Thanks for playing");
			break;
		case 1:
			game();
			break;
		default:
			System.out.println("That's not an option");
		}
	}
	
	public void game() throws IOException, NoSuchFieldException {
		boolean play = true;
		while(play) {		
			initChestCards();
			initChanceCards();
			askForPlayers();
			run();
			play = ConsoleUI.promptForBool("Do you want to play again? Y/N", "Y", "N");
		}	
	}
	
	public void run() throws NoSuchFieldException, IOException {
		boolean gameOver = false;
		while(!gameOver) {
			System.out.println("");
			System.out.println(players[turn].Name + ", your turn" );
			if(players[turn].inJail == true) {
				jailedAction();
			}
			else {
				roll();
				players[turn].doubleCount = 0;
			}
			turn++;
			if(turn == playerCount) {
				turn = 0;
			}
		}
	}

	public void jailedAction() throws IOException, NoSuchFieldException {
		String[] jailOptions = new String[4];
		jailOptions[0] = "Roll for double";
		jailOptions[1] = "Use 'Get out of Jail Free' card";
		jailOptions[2] = "Purchase card from another player";
		jailOptions[3] = "Pay fine of $50";
		int selection = ConsoleUI.promptForMenuSelection(jailOptions, false);
		switch(selection) {
		case 0:
			int dice1 = rnd.nextInt(6);
			int dice2 = rnd.nextInt(6);
			System.out.println("You rolled " + dice1 + " and " + dice2);
			if(dice1 == dice2) {
				int roll = dice1+dice2;
				moveToSpace(roll);
				players[turn].inJail = false;
			}
			break;
		case 1:
			players[turn].useJailCard();
			players[turn].inJail = false;
			break;
		case 2:
			//check if anyone has a card
			//player name price
			//y/n
			//transfer
			//use
			break;
		case 3:
			players[turn].setMoney(players[turn].Money-50);
			players[turn].inJail = false;
		}
	}

	public void roll() throws IOException {
		int dice1 = rnd.nextInt(6)+1;
		int dice2 = rnd.nextInt(6)+1;
		if(dice1 == dice2) {
			players[turn].doubleCount++;
		}
		System.out.println("You rolled " + dice1 + " and " + dice2 + " for " + (dice1+dice2));
		int roll = dice1+dice2;
		moveToSpace(roll);
		printGameMenu();
		if(dice1 == dice2 && players[turn].doubleCount < 3) {
			roll();
		}
	}
	
	public void printGameMenu() throws IOException {
		if(players[turn].location != 10) {
			String[] options = new String[5];
			options[0] = "Buy Property";
			options[1] = "Sell Property";
			options[2] = "Buy House";
			options[3] = "Buy Hotel";
			options[4] = "Pay Rent";
			int selection = ConsoleUI.promptForMenuSelection(options, false);
			switch(selection) {
			case 0:
				propSpace[players[turn].location].buySpace(players[turn]);
				break;
			case 1:
				//sell property
				break;
			case 2:
				propSpace[players[turn].location].buyHouse();
				players[turn].houseCount++;
				break;
			case 3:
				propSpace[players[turn].location].buyHotel();
				players[turn].hotelCount++;
				break;
			case 4:
				
				break;
			default:
				System.out.println("Not an option");
			}
		}
	}

	public void moveToSpace(int roll) {
		for(int i=0;i<roll;i++) {
			players[turn].location++;
			if(players[turn].location == 40) {
				players[turn].location = 0;
			}
		}
		if(players[turn].doubleCount == 3) {
			players[turn].inJail = true;
			players[turn].location = 10;
			players[turn].doubleCount = 0;
		}
		else if(players[turn].location == 30) {
			players[turn].location = 10;
			players[turn].inJail = true;
		}
	}
	
	public void jumpToSpace(int space) {
		players[turn].location = space;
	}
	
	
	
	
	
	
	
	public void chanceCards() {
		int draw = rnd.nextInt(17);
		System.out.println(chanceCard.chanceCards[draw]);
		switch(draw) {
		case 0:
			//Advance to Go
			jumpToSpace(0);
			break;
		case 1:
			//Advace to Illinois Ave.
			jumpToSpace(24);
			break;
		case 2:
			//Advance to St. Charles Ave.
			jumpToSpace(11);
			break;
		case 3:
			//Advance to nearest utility
			if(players[turn].location<12 || players[turn].location > 28) {
				players[turn].location = 12;
			}
			else {
				players[turn].location = 28;
			}
			break;
		case 4:
			//Advance to nearest Railroad
			if(players[turn].location<5 || players[turn].location>35) {
				players[turn].location = 5;
			}
			else if(players[turn].location<15 && players[turn].location > 5) {
				players[turn].location = 15;
			}
			else if(players[turn].location<25 && players[turn].location>15) {
				players[turn].location = 25;
			}
			else if(players[turn].location<35 && players[turn].location > 25) {
				players[turn].location = 35;
			}
			break;
		case 5:
			//Receive $50
			players[turn].setMoney(players[turn].Money + 50);
			break;
		case 6:
			//Get out of jail free
			players[turn].getJailCard();
			break;
		case 7:
			//go back three spaces
			moveToSpace(-3);
			break;
		case 8:
			//Go to jail
			players[turn].inJail = true;
			jumpToSpace(10);
		case 9:
			//Pay $25 per house and $100 per hotel
			players[turn].setMoney(players[turn].Money+(25 * players[turn].houseCount)+(100 * players[turn].hotelCount));
			break;
		case 10:
			//Pay $15
			players[turn].setMoney(players[turn].Money-15);
			break;
		case 11:
			//Advance Reading Railroad
			jumpToSpace(5);
			break;
		case 12:
			//Advance to Boardwalk
			jumpToSpace(39);
			break;
		case 13:
			//Pay each player $50
			for(int i=0;i<players.length-1;i++) {
				players[i].setMoney(players[turn].Money+50);
			}
			players[turn].setMoney(players[turn].Money-(players.length*50));
			break;
		case 14:
			//Recieve $150
			players[turn].setMoney(players[turn].Money+150);
			break;
		case 15:
			//Collect $100
			players[turn].setMoney(players[turn].Money+100);
		}
	}
	
	public void chestCards() {
		int draw = rnd.nextInt(17);
		System.out.println(chestCards.communityChestCard[draw]);
		switch(draw) {
		case 0:
			//Advance to Go
			jumpToSpace(0);
			break;
		case 1:
			//Collect $200
			players[turn].setMoney(players[turn].Money+200);
			break;
		case 2:
			//Pay $50
			players[turn].setMoney(players[turn].Money-50);
			break;
		case 3:
			//Get $50
			players[turn].setMoney(players[turn].Money+50);
			break;
		case 4:
			//Get out of jail free
			players[turn].getJailCard();
			break;
		case 5:
			//go to jail
			players[turn].inJail = true;
			jumpToSpace(10);
			break;
		case 6:
			//Collect $50 from each player
			for(int i=0;i<players.length-1;i++) {
				players[i].setMoney(players[turn].Money-50);
			}
			players[turn].setMoney(players[turn].Money+(players.length*50));
			break;
		case 7:
			//Recieve $100
			players[turn].setMoney(players[turn].Money+100);
			break;
		case 8:
			//Collect $20
			players[turn].setMoney(players[turn].Money+20);
			break;
		case 9:
			//Collect $10 from each player
			for(int i=0;i<players.length-1;i++) {
				players[i].setMoney(players[turn].Money-10);
			}
			players[turn].setMoney(players[turn].Money+(players.length*10));
			break;
		case 10:
			//Collect $100
			players[turn].setMoney(players[turn].Money+100);
			break;
		case 11:
			//Pay $50
			players[turn].setMoney(players[turn].Money-50);
			break;
		case 12:
			//Pay $50
			players[turn].setMoney(players[turn].Money-50);
			break;
		case 13:
			//Recieve $25
			players[turn].setMoney(players[turn].Money+25);
			break;
		case 14:
			//Pay $40 per house and $115 per hotel
			players[turn].setMoney(players[turn].Money+(45 * players[turn].houseCount)+(115 * players[turn].hotelCount));
			break;
		case 15:
			//Collect $10
			players[turn].setMoney(players[turn].Money+10);
			break;
		case 16:
			//Collect $1000
			players[turn].setMoney(players[turn].Money+1000);
		}
	}
	
	
	private void initProp() {
		
	}
	
	private void initChestCards() {
		chestCards.communityChestCard[0] = "Advance to 'Go'";
		chestCards.communityChestCard[1] = "Bank error in your favor. Collect $200";
		chestCards.communityChestCard[2] = "Doctor's fee. Pay $50";
		chestCards.communityChestCard[3] = "From sale of stock you get $50";
		chestCards.communityChestCard[4] = "Get out of Jail Free";
		chestCards.communityChestCard[5] = "Go to Jail";
		chestCards.communityChestCard[6] = "Grand Opera Night. Collect $50 from each player for opening night seats";
		chestCards.communityChestCard[7] = "Holiday Fund matures. Receive $100";
		chestCards.communityChestCard[8] = "Income tax refund. Collect $20";
		chestCards.communityChestCard[9] = "It's your birthday. Collect $10 from each player";
		chestCards.communityChestCard[10] = "Life insurance matures. Collect $100";
		chestCards.communityChestCard[11] = "Hospital fees. Pay $50";
		chestCards.communityChestCard[12] = "School fees. Pay $50";
		chestCards.communityChestCard[13] = "Receive $25 consultancy fee";
		chestCards.communityChestCard[14] = "You are assessed for street repairs";
		chestCards.communityChestCard[15] = "You have won second prize in a beauty contest. Collect $10";
		chestCards.communityChestCard[16] = "You inherit $1000";
	}	
	
	public void initChanceCards() {
		chanceCard.chanceCards[0] = "Advance to 'Go'";
		chanceCard.chanceCards[1] = "Advance to Illinois Ave.";
		chanceCard.chanceCards[2] = "Advance to St. Charles Place";
		chanceCard.chanceCards[3] = "Advance token to nearest Utility. If unowned, you may buy. If owned, throw a dice and pay owner a total 10 times the amount thrown";
		chanceCard.chanceCards[4] = "Advanc token to nearest Railroad. if unowned, you may buy. If owned, pay double the rental";
		chanceCard.chanceCards[5] = "Bank pays you dividend of $50";
		chanceCard.chanceCards[6] = "Get out of Jail Free";
		chanceCard.chanceCards[7] = "Go back three spaces";
		chanceCard.chanceCards[8] = "Go to Jail";
		chanceCard.chanceCards[9] = "Make general repairs on all your property. For each house pay $25. For each hotel, pay $100";
		chanceCard.chanceCards[10] = "Pay poor tax of $15";
		chanceCard.chanceCards[11] = "Take a trip to Reading Railroad";
		chanceCard.chanceCards[12] = "Take a walk on the Boardwalk. Advance to Boardwalk";
		chanceCard.chanceCards[13] = "You have been elected Chairman of the Board. Pay each player $50";
		chanceCard.chanceCards[14] = "Your building loan matures. Receive $150";
		chanceCard.chanceCards[15] = "You have won a crossword competition. Collect $100";
	}
	
	private void askForPlayers() throws IOException {
		playerCount = ConsoleUI.promptForInt("How many people are playing?", 2, 8);
		int mon = 1500;
		for(int i=0;i<playerCount;i++) {
			players[i] = new Player();
			String playerName = ConsoleUI.promptForInput("What is your name?", false);
			players[i].init(playerName, PieceNames.BATTLESHIP, mon);
		}
	}
}