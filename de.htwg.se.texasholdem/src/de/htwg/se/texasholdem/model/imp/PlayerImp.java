package de.htwg.se.texasholdem.model.imp;

import java.util.LinkedList;

import de.htwg.se.texasholdem.model.Card;
import de.htwg.se.texasholdem.model.Player;

public class PlayerImp implements Player {

	private String playerName;
	private int playerCash;
	private LinkedList<Card> holeCards;

	public PlayerImp(String name) {
		setPlayerName(name);
		holeCards = new LinkedList<Card>();
	}

	public LinkedList<Card> getHoleCards() {
		return holeCards;
	}

	public int getPlayerMoney() {
		return playerCash;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setHoleCard(Card holeCard) {
		this.holeCards.add(holeCard);
	}

	public void setPlayerMoney(int playerMoney) {
		this.playerCash = playerMoney;
	}

	private void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

}
