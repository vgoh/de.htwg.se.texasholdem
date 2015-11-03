package de.htwg.se.texasholdem.model.imp;

import java.util.LinkedList;
import java.util.List;

import de.htwg.se.texasholdem.model.Player;
import de.htwg.se.texasholdem.model.Table;

public class TableImp implements Table {

	private LinkedList<Player> players;
	private int potValue;
	private int BigBlindValue;
	private int SmallBlindValue;

	public TableImp() {
		players = new LinkedList<Player>();
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public Player getNextPlayer(Player player) {
		int index = players.indexOf(player);
		return players.get((index + 1) % players.size());
	}

	public List<Player> getPlayerList() {
		return players;
	}

	public int getPotValue() {
		return potValue;
	}

	public void setPotValue(int value) {
		potValue = value;
	}

	public int getBigBlindValue() {
		return BigBlindValue;
	}

	public void setBigBlindValue(int bigBlindValue) {
		BigBlindValue = bigBlindValue;
	}

	public int getSmallBlindValue() {
		return SmallBlindValue;
	}

	public void setSmallBlindValue() {
		SmallBlindValue = BigBlindValue / 2;
	}

}
