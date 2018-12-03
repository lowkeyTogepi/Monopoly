package edu.neumont.csc110;

import java.io.IOException;
import java.util.Random;
import interfaces.ConsoleUI;

public class Game {
	
	Random rnd = new Random();
	
	ChanceCard chanceCard = new ChanceCard();
	CommunityChestCard chestCards = new CommunityChestCard();
	
	Player[] players = new Player[8];
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
			askForPlayers();
			run();
			play = ConsoleUI.promptForBool("Do you want to play again? Y/N", "Y", "N");
		}	
	}

	private void askForPlayers() throws IOException {
		int playerCount = ConsoleUI.promptForInt("How many people are playing?", 2, 8);
		int mon = 1500;
		for(int i=0;i<playerCount;i++) {
			players[i] = new Player();
			String playerName = ConsoleUI.promptForInput("What is your name?", false);
			players[i].init(playerName, null, mon);
		}
	}
	
	public void run() throws NoSuchFieldException, IOException {
		boolean gameOver = false;
		while(!gameOver) {
			System.out.println(players[turn].Name + "(" + players[turn].Piece + "), your turn" );
			if(players[turn].location == 10) {
				jailedAction();
			}
			else {
				int roll = roll();
				moveToSpace(roll);
				//spaceAction();
				printGameMenu();
				turn++;
			}
		}
	}

	private void jailedAction() throws IOException, NoSuchFieldException {
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
			if(dice1 == dice2) {
				int roll = dice1+dice2;
				moveToSpace(roll);
			}
			break;
		case 1:
			players[turn].useJailCard();
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
		}
	}

	public int roll() {
		int dice1 = rnd.nextInt(6);
		int dice2 = rnd.nextInt(6);
		if(dice1 == dice2) {
			players[turn].doubleCount++;
		}
		int roll = dice1+dice2;
		return roll;
	}
	
	public void printGameMenu() {
		
	}
	
	public void moveToSpace(int roll) {
		for(int i=0;i<roll;i++) {
			players[turn].location++;
			if(players[turn].location == 40) {
				players[turn].location = 0;
			}
		}
		if(players[turn].doubleCount == 3) {
			jumpToSpace(10);
			players[turn].doubleCount = 0;
		}
	}
	
	public void jumpToSpace(int space) {
		players[turn].location = space;
	}
	
	public void buyProperty() {
		
	}
	
	public void sellProperty() {
		
	}
	
	public void chanceCards() {
		int draw = rnd.nextInt(17);
		System.out.println(chanceCard.chanceCards[draw]);
		switch(draw) {
		case 0:
			jumpToSpace(0);
			break;
		case 1:
			jumpToSpace(24);
			break;
		case 2:
			jumpToSpace(11);
			break;
		case 3:
			if(players[turn].location<12 || players[turn].location > 28) {
				players[turn].location = 12;
			}
			else {
				players[turn].location = 28;
			}
			break;
		case 4:
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
			players[turn].setMoney(players[turn].Money + 50);
			break;
		case 6:
			players[turn].getJailCard();
			break;
		case 7:
			moveToSpace(-3);
			break;
		case 8:
			jumpToSpace(10);
		case 9:
			players[turn].setMoney(players[turn].Money+(25)+(100));
			break;
		case 10:
			players[turn].setMoney(players[turn].Money-15);
			break;
		case 11:
			jumpToSpace(5);
			break;
		case 12:
			jumpToSpace(39);
			break;
		case 13:
			for(int i=0;i<players.length-1;i++) {
				players[i].setMoney(players[turn].Money+50);
			}
			players[turn].setMoney(players[turn].Money-(players.length*50));
			break;
		case 14:
			players[turn].setMoney(players[turn].Money+150);
			break;
		case 15:
			players[turn].setMoney(players[turn].Money+100);
		}
	}
	
	public void chestCards() {
		int draw = rnd.nextInt(17);
		System.out.println(chestCards.communityChestCard[draw]);
		switch(draw) {
		case 0:
			jumpToSpace(0);
			break;
		case 1:
			players[turn].setMoney(players[turn].Money+200);
			break;
		case 2:
			players[turn].setMoney(players[turn].Money-50);
			break;
		case 3:
			players[turn].setMoney(players[turn].Money+50);
			break;
		case 4:
			players[turn].getJailCard();
			break;
		case 5:
			jumpToSpace(10);
			break;
		case 6:
			for(int i=0;i<players.length-1;i++) {
				players[i].setMoney(players[turn].Money-50);
			}
			players[turn].setMoney(players[turn].Money+(players.length*50));
			break;
		case 7:
			players[turn].setMoney(players[turn].Money+100);
			break;
		case 8:
			players[turn].setMoney(players[turn].Money+20);
			break;
		case 9:
			for(int i=0;i<players.length-1;i++) {
				players[i].setMoney(players[turn].Money-10);
			}
			players[turn].setMoney(players[turn].Money+(players.length*10));
			break;
		case 10:
			players[turn].setMoney(players[turn].Money+100);
			break;
		case 11:
			players[turn].setMoney(players[turn].Money-50);
			break;
		case 12:
			players[turn].setMoney(players[turn].Money-50);
			break;
		case 13:
			players[turn].setMoney(players[turn].Money+25);
			break;
		case 14:
			players[turn].setMoney(players[turn].Money+(45)+(115));
			break;
		case 15:
			players[turn].setMoney(players[turn].Money+10);
			break;
		case 16:
			players[turn].setMoney(players[turn].Money+1000);
		}
	}
}