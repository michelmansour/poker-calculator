package com.skraylabs.poker.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CardFactoryStringTest {

  @Rule public ExpectedException exception = ExpectedException.none();

  Card card;

  @Test
  public void testInvalidInput_null() {
    // Set up
    card = null;
    // Verify
    exception.expect(IllegalArgumentException.class);
    // Exercise
    CardFactory.createStringFromCard(card);
  }

  @Test
  public void testInvalidInput_emptyCard() {
    // Set up
    card = new Card(null, null);
    // Verify
    exception.expect(IllegalArgumentException.class);
    // Exercise
    CardFactory.createStringFromCard(card);
  }

  @Test
  public void testAceOfHearts() {
    // Set up
    card = new Card(Rank.Ace, Suit.Hearts);
    // Exercise
    String string = CardFactory.createStringFromCard(card);
    // Verify
    assertThat(string, is("Ah"));
  }

  @Test
  public void testTwoOfClubs() {
    // Set up
    card = new Card(Rank.Two, Suit.Clubs);
    // Exercise
    String string = CardFactory.createStringFromCard(card);
    // Verify
    assertThat(string, is("2c"));
  }

  @Test
  public void testFiveOfDiamonds() {
    // Set up
    card = new Card(Rank.Five, Suit.Diamonds);
    // Exercise
    String string = CardFactory.createStringFromCard(card);
    // Verify
    assertThat(string, is("5d"));
  }

  @Test
  public void testTenOfSpades() {
    // Set up
    card = new Card(Rank.Ten, Suit.Spades);
    // Exercise
    String string = CardFactory.createStringFromCard(card);
    // Verify
    assertThat(string, is("Ts"));
  }
}
