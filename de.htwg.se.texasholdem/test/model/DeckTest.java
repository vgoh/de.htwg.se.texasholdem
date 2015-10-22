package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DeckTest {

	Deck d;

	@Before
	public void _setup() {
		d = new Deck(52);
	}

	@Test
	public void getCard_inputDeckWith52Cards_returnsOneCard() {
		Card c = d.getCard();
		Assert.assertEquals(Card.class, c.getClass());
	}

	@Test
	public void getNumberOfCards_inputDeckWith52Cards_returnsInteger52() {
		Assert.assertEquals(52, d.getNumberOfCards());
	}
}
