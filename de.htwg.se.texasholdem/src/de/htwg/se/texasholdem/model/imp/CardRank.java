package de.htwg.se.texasholdem.model.imp;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.htwg.se.texasholdem.model.Card;

public enum CardRank {
	HIGHEST_CARD {
		@Override
		public List<Card> evaluate(List<Card> cards) {
			List<Card> highestCardList = new LinkedList<Card>();
			Card highestCard = null;

			// Hint: ordinal() returns index of element in enum
			// if highestCard is null or the index in enum of card c is higher
			// than
			// highestcard, then highestCard is set to c
			for (Card c : cards) {
				if (highestCard == null || c.getRank().ordinal() > highestCard.getRank().ordinal()) {
					highestCard = c;
				}
			}
			highestCardList.add(highestCard);

			return highestCardList;
		}

		@Override
		public String toString() {
			return "Highest Card";
		}
	},
	ONE_PAIR {
		@Override
		public List<Card> evaluate(List<Card> cards) {
			Map<Card, Card> pairs = findPairs(cards);
			List<Card> listOfPairs = new LinkedList<Card>();

			listOfPairs.addAll(pairs.values());
			listOfPairs.addAll(pairs.keySet());

			if (pairs.size() == 1) {

				return listOfPairs;
			} else if (pairs.size() > 1) {

				Card highestCard;

				highestCard = CardRank.HIGHEST_CARD.evaluate(listOfPairs).get(0);
				listOfPairs.clear();

				for (Map.Entry<Card, Card> entry : pairs.entrySet()) {
					if (entry.getKey() == highestCard || entry.getValue() == highestCard) {
						listOfPairs.add(entry.getKey());
						listOfPairs.add(entry.getValue());
					}
				}

				return listOfPairs;
			} else {
				return Collections.emptyList();
			}
		}

		@Override
		public String toString() {
			return "One Pair";
		}
	},
	TWO_PAIR {
		@Override
		public List<Card> evaluate(List<Card> cards) {
			Map<Card, Card> pairsHash = findPairs(cards);
			List<Card> pairsList = new LinkedList<Card>();

			for (Map.Entry<Card, Card> entry : pairsHash.entrySet()) {
				pairsList.add(entry.getKey());
				pairsList.add(entry.getValue());
			}

			if (pairsList.size() != 4) {
				return Collections.emptyList();
			} else {
				return pairsList;
			}
		}

		@Override
		public String toString() {
			return "Two Pair";
		}
	},
	THREE_OF_A_KIND {
		@Override
		public List<Card> evaluate(List<Card> cards) {
			List<Card> cardsOfSameKind = findCardsofSameKind(cards);

			if (cardsOfSameKind != null) {
				return cardsOfSameKind;
			} else {
				return Collections.emptyList();
			}
		}

		@Override
		public String toString() {
			return "Three of a kind";
		}
	},
	STRAIGHT {
		@Override
		public List<Card> evaluate(List<Card> cards) {

			List<Card> straight = new LinkedList<Card>();
			List<Card> cardsOrdered = sortToLowestCard(cards);

			// 2 For-loops to iterate over all Cards to check each card against
			// each
			// other (to be sure to match every combination)
			for (Card cardOne : cardsOrdered) {
				// Clear list and add first card to the list
				straight.clear();
				straight.add(cardOne);

				for (Card cardTwo : cardsOrdered) {
					if (cardOne.getRank() == Rank.ACE) {
						if (cardTwo.getRank().ordinal() == (cardOne.getRank().ordinal() - (Rank.values().length - 1)
								+ straight.size() - 1)) {
							straight.add(cardTwo);
						} else {
							continue;
						}
					} else if (cardTwo.getRank().ordinal() == (cardOne.getRank().ordinal() + straight.size())) {
						straight.add(cardTwo);
					} else {
						continue;
					}
				}

				if (straight.size() == 5) {
					return straight;
				} else if (straight.size() >= 5) {
					return getFiveHighestCards(straight);
				}
			}
			return Collections.emptyList();
		}

		@Override
		public String toString() {
			return "Straight";
		}
	},
	FLUSH {
		@Override
		public List<Card> evaluate(List<Card> cards) {
			List<Card> flush = new LinkedList<Card>();

			for (Suit suit : Suit.values()) {
				flush.clear();

				for (Card card : cards) {
					if (suit == card.getSuit()) {
						flush.add(card);
					}
				}

				if (flush.size() == 5) {
					return flush;
				} else if (flush.size() >= 5) {
					return getFiveHighestCards(flush);
				}
			}
			return Collections.emptyList();
		}

		@Override
		public String toString() {
			return "Flush";
		}
	},
	FULL_HOUSE {
		@Override
		public List<Card> evaluate(List<Card> cards) {
			List<Card> fullHouse = new LinkedList<Card>();

			fullHouse = CardRank.THREE_OF_A_KIND.evaluate(cards);

			if (fullHouse == null || fullHouse.isEmpty()) {
				return Collections.emptyList();
			} else {
				cards.removeAll(fullHouse);
				List<Card> onePair = CardRank.ONE_PAIR.evaluate(cards);
				if (onePair == null || onePair.isEmpty()) {
					return Collections.emptyList();
				} else {
					for (Card c : onePair) {
						fullHouse.add(c);
					}
					return fullHouse;
				}
			}
		}

		@Override
		public String toString() {
			return "Full House";
		}
	},
	FOUR_OF_A_KIND {
		@Override
		public List<Card> evaluate(List<Card> cards) {
			return CardRank.THREE_OF_A_KIND.evaluate(cards);
		}

		@Override
		public String toString() {
			return "Four of a kind";
		}
	},
	STRAIGHT_FLUSH {
		@Override
		public List<Card> evaluate(List<Card> cards) {
			List<Card> straightFlush = new LinkedList<Card>();

			for (Suit suit : Suit.values()) {
				straightFlush.clear();

				for (Card card : cards) {
					if (suit == card.getSuit()) {
						straightFlush.add(card);
					}
				}

				if (straightFlush.size() >= 5) {
					straightFlush = CardRank.STRAIGHT.evaluate(straightFlush);
					if (straightFlush == null) {
						return Collections.emptyList();
					} else if (straightFlush.size() > 5) {
						return getFiveHighestCards(straightFlush);
					}
					return straightFlush;
				}
			}
			return Collections.emptyList();
		}

		@Override
		public String toString() {
			return "Straight Flush";
		}
	},
	ROYAL_FLUSH {
		@Override
		public List<Card> evaluate(List<Card> cards) {
			List<Card> royalFlush = new LinkedList<Card>();

			royalFlush = CardRank.FLUSH.evaluate(cards);
			if (royalFlush != null) {
				royalFlush = CardRank.STRAIGHT.evaluate(royalFlush);
				if (!royalFlush.isEmpty() && CardRank.HIGHEST_CARD.evaluate(royalFlush).get(0).getRank() == Rank.ACE) {
					return royalFlush;
				}
			}

			return Collections.emptyList();
		}

		@Override
		public String toString() {
			return "Royal Flush";
		}
	};

	// Finds pairs in List 'sevenCards' and add them into a HashMap
	private static Map<Card, Card> findPairs(List<Card> sevenCards) {
		Map<Card, Card> pairs = new HashMap<Card, Card>();

		// 2 For-loops to iterate over all Cards to check each card against each
		// other (to be sure to match every combination)
		for (Card cardOne : sevenCards) {
			for (Card cardTwo : sevenCards) {
				// cardOne needs to be different to cardTwo and must have same
				// Rank
				if (cardOne != cardTwo && isSameRank(cardOne, cardTwo)) {
					// Adds the pair to the HashMap if cardOne nor cardTwo were
					// already added
					if (!(pairs.containsKey(cardOne) || pairs.containsKey(cardTwo))) {
						pairs.put(cardOne, cardTwo);
					}
				}
			}
		}
		// Size of HashMap equals number of pairs found in 'sevenCards'
		return pairs;
	}

	// Finds sameOfAKind in List 'sevenCards' and adds them to another list
	private static List<Card> findCardsofSameKind(List<Card> sevenCards) {
		List<Card> cardsOfSameKind = new LinkedList<Card>();

		// 2 For-loops to iterate over all Cards to check each card against each
		// other (to be sure to match every combination)
		for (Card cardOne : sevenCards) {
			// Clear list and add first card to the list
			cardsOfSameKind.clear();
			cardsOfSameKind.add(cardOne);

			for (Card cardTwo : sevenCards) {
				// cardOne needs to be different to cardTwo and must have same
				// Rank
				if (cardOne != cardTwo && isSameRank(cardOne, cardTwo)) {
					// Add only cardTwo; cardOne was already added.
					cardsOfSameKind.add(cardTwo);
				}
			}

			// Check list size for ThreeOfAKind(size 3) or higher and return
			// list with cards
			if (cardsOfSameKind.size() >= 3) {
				return cardsOfSameKind;
			}
		}

		return Collections.emptyList();
	}

	private static boolean isSameRank(Card cardOne, Card cardTwo) {
		return cardOne.getRank() == cardTwo.getRank();
	}

	private static List<Card> sortToLowestCard(List<Card> straight) {
		List<Card> sortedStraight = new LinkedList<Card>();

		for (int i = 0; i <= Rank.values().length; i++) {
			for (Card c : straight) {
				if (c.getRank().ordinal() == i) {
					sortedStraight.add(c);
				}
			}
		}

		assert straight.size() == sortedStraight.size();

		return sortedStraight;
	}

	private static List<Card> getFiveHighestCards(List<Card> sevenCards) {
		List<Card> fiveCards = new LinkedList<Card>();
		Card card;

		for (int i = 0; i < 5; i++) {
			card = CardRank.HIGHEST_CARD.evaluate(sevenCards).get(0);
			fiveCards.add(card);
			sevenCards.remove(card);
		}

		return fiveCards;
	}

	public abstract List<Card> evaluate(List<Card> cards);
}
