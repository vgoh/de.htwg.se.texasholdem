package de.htwg.se.texasholdem.model.imp;

import java.util.List;

import de.htwg.se.texasholdem.model.Card;
import de.htwg.se.texasholdem.model.Player;

public class EvaluationObject implements Comparable<EvaluationObject> {

	private boolean split;
	private Player player;
	private List<Card> cards;
	private CardRank ranking;

	public EvaluationObject(Player player) {
		this.player = player;
		this.split = false;
	}

	public Player getPlayer() {
		return this.player;
	}

	public List<Card> getCards() {
		return this.cards;
	}

	public CardRank getRanking() {
		return this.ranking;
	}

	public void setRanking(CardRank ranking) {
		this.ranking = ranking;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public int compareTo(EvaluationObject o) {
		if (this.getRanking().ordinal() < o.getRanking().ordinal()) {
			return 1;
		} else if (this.getRanking().ordinal() == o.getRanking().ordinal()) {
			return 0;
		} else {
			return -1;
		}
	}

	public boolean isSplit() {
		return this.split;
	}

	public void setPosition(boolean split) {
		this.split = split;
	}

}
