package de.htwg.se.texasholdem.controller;

import java.util.LinkedList;

import de.htwg.se.texasholdem.model.Card;
import de.htwg.se.texasholdem.model.Player;

public interface TableManager {

	void addPlayer(Player player);

	int getBigBlind();

	LinkedList<Card> getHoleCards(Player player);

	LinkedList<Player> getPlayerList();

	int getSmallBlind();

	void resetGame();

	void setHoleCards(Player player);

	void setSmallBlind(int smallBlind);
}
