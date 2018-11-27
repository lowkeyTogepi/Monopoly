package parra.alexis.monopoly;

public class Player {
	String Name;
	PieceNames Piece;
	int Money;
	int location;
	int numJailCards;

	public Player() {
		Name = "Default";
		Piece = PieceNames.DEFAULT;
		Money = 0;
		location = 0;
		numJailCards = 0;
	}

	public void init(String playerName, PieceNames thePiece, int mon) {
		Name = playerName;
		Piece = thePiece;
		Money = 0;
		location = 0;
		numJailCards = 0;
	}

	public void setName(String playerName) {
		Name = playerName;
	}

	public void setPeice(PieceNames thePiece) {
		Piece = thePiece;
	}

	public void setMoney(int mon) {
		Money = mon;
	}

	public void setLocation(int loc) {
		location = loc;
	}

	public void getJailCard() {
		numJailCards++;
	}

	public void useJailCard() throws NoSuchFieldException {
		if (numJailCards == 0) {
			throw new NoSuchFieldException();
		}
		numJailCards--;
	}

	public String getName() {
		return Name;
	}

	public PieceNames getPiece() {
		return Piece;
	}

	public int getLocation() {
		return location;
	}

	public int getMoney() {
		return Money;
	}

	public int getNumJailCards() {
		return numJailCards;
	}

	public String toString() {
		return "Player name:" + Name + "\n"
				+ "Money: " + Money + "\n"
				+ "Piece: " + Piece;
	}

}
