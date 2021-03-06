package de.htwg.se.texasholdem.controller.imp;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.htwg.se.texasholdem.controller.EvaluationManager;
import de.htwg.se.texasholdem.model.Card;
import de.htwg.se.texasholdem.model.Player;
import de.htwg.se.texasholdem.model.imp.CardRank;
import de.htwg.se.texasholdem.model.imp.EvaluationObject;

public class EvaluationManagerImp implements EvaluationManager {

	static final class CardListRankingPair {
		private List<Card> cards;
		private CardRank cardRank;

		private CardListRankingPair(List<Card> cards, CardRank cardRank) {
			this.cards = cards;
			this.cardRank = cardRank;
		}

		public CardRank getCardRank() {
			return cardRank;
		}

		public List<Card> getCards() {
			return cards;
		}
	}

	public List<EvaluationObject> evaluate(List<Player> players, List<Card> communityCards) {
		List<EvaluationObject> evalList = new LinkedList<EvaluationObject>();

		// First Iteration: Set Players and the "5" winning cards out of the
		// seven cards in sum
		for (Player player : players) {
			EvaluationObject evalObj = new EvaluationObject(player);
			CardListRankingPair clrp = evaluateCards(player.getHoleCards(), communityCards);
			evalObj.setCards(clrp.getCards());
			evalObj.setRanking(clrp.getCardRank());
			evalList.add(evalObj);
		}

		// Sort the 'evalList' regarding to the winning position of the players
		Collections.sort(evalList);

		// Evaluate Kicker Card of player
		for (EvaluationObject evalObj1 : evalList) {
			for (EvaluationObject evalObj2 : evalList) {
				if (evalObj1 == evalObj2) {
					break;
				}
				if (evalObj1.getRanking().ordinal() == evalObj2.getRanking().ordinal()) {
					if (getSumOfCards(evalObj1.getCards()) == getSumOfCards(evalObj2.getCards())) {

						List<Card> evalObj1Cards = getAllCardsButNoWinningCards(evalObj1.getPlayer().getHoleCards(),
								evalObj1.getCards());
						List<Card> evalObj2Cards = getAllCardsButNoWinningCards(evalObj2.getPlayer().getHoleCards(),
								evalObj2.getCards());

						Card highestCardEvalObj1 = getHighestCard(evalObj1Cards).get(0);
						Card highestCardEvalObj2 = getHighestCard(evalObj2Cards).get(0);

						if (highestCardEvalObj1.getRank().numVal() == highestCardEvalObj2.getRank().numVal()) {
							evalObj1.setPosition(true);
							evalObj2.setPosition(true);
						} else if (highestCardEvalObj1.getRank().numVal() < highestCardEvalObj2.getRank().numVal()) {
							Collections.swap(evalList, evalList.indexOf(evalObj1), evalList.indexOf(evalObj2));
						}

					} else if (getSumOfCards(evalObj1.getCards()) < getSumOfCards(evalObj2.getCards())) {
						Collections.swap(evalList, evalList.indexOf(evalObj1), evalList.indexOf(evalObj2));
					}
				}
			}
		}

		return evalList;
	}

	private CardListRankingPair evaluateCards(List<Card> playerCards, List<Card> communityCards) {
		List<Card> cards = new LinkedList<Card>();
		List<Card> winningCards = new LinkedList<Card>();
		CardRank cardRank = CardRank.ROYAL_FLUSH;

		// Add all cards to one list
		cards.addAll(playerCards);
		cards.addAll(communityCards);

		// Iterate over all evaluation methods of CardRank
		do {
			winningCards = cardRank.evaluate(cards);
			if (winningCards.isEmpty() && cardRank.ordinal() > 0) {
				cardRank = CardRank.values()[cardRank.ordinal() - 1];
			}
		} while (winningCards.isEmpty() && cardRank.ordinal() >= 0);

		return new CardListRankingPair(winningCards, cardRank);
	}

	private List<Card> getAllCardsButNoWinningCards(List<Card> holeCards, List<Card> winningCards) {
		List<Card> allCardsButNoWinningCards = new LinkedList<Card>();
		allCardsButNoWinningCards.addAll(holeCards);

		allCardsButNoWinningCards.removeAll(winningCards);
		return allCardsButNoWinningCards;
	}

	// <------------- EVALUATION METHODS -------------->

	public List<Card> getHighestCard(List<Card> cards) {
		return CardRank.HIGHEST_CARD.evaluate(cards);
	}

	public int getSumOfCards(List<Card> cards) {
		int sum = 0;

		for (Card c : cards) {
			sum += c.getRank().numVal();
		}

		return sum;
	}

	public List<Card> isFlush(List<Card> cards) {
		return CardRank.FLUSH.evaluate(cards);
	}

	public List<Card> isFourOfAKind(List<Card> cards) {
		return CardRank.FOUR_OF_A_KIND.evaluate(cards);
	}

	public List<Card> isFullHouse(List<Card> cards) {
		return CardRank.FULL_HOUSE.evaluate(cards);
	}

	public List<Card> isOnePair(List<Card> cards) {
		return CardRank.ONE_PAIR.evaluate(cards);
	}

	public List<Card> isRoyalFlush(List<Card> cards) {
		return CardRank.ROYAL_FLUSH.evaluate(cards);
	}

	public List<Card> isStraight(List<Card> cards) {
		return CardRank.STRAIGHT.evaluate(cards);
	}

	public List<Card> isStraightFlush(List<Card> cards) {
		return CardRank.STRAIGHT_FLUSH.evaluate(cards);
	}

	public List<Card> isThreeOfAKind(List<Card> cards) {
		return CardRank.THREE_OF_A_KIND.evaluate(cards);
	}

	public List<Card> isTwoPair(List<Card> cards) {
		return CardRank.TWO_PAIR.evaluate(cards);
	}
}
