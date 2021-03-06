package com.skraylabs.poker;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.skraylabs.poker.model.BoardFormatException;
import com.skraylabs.poker.model.Card;
import com.skraylabs.poker.model.CardFormatException;
import com.skraylabs.poker.model.GameState;
import com.skraylabs.poker.model.GameStateFactory;
import com.skraylabs.poker.model.GameStateFormatException;
import com.skraylabs.poker.model.PocketFormatException;
import com.skraylabs.poker.model.Rank;
import com.skraylabs.poker.model.Suit;

import org.junit.Test;

import java.util.ArrayList;

public class FullHouseTest {

  @Test
  public void givenLessThanFiveCardsReturnsFalse() {
    ArrayList<Card> cards = new ArrayList<Card>();
    cards.add(new Card(Rank.Ace, Suit.Clubs));
    cards.add(new Card(Rank.Two, Suit.Clubs));
    cards.add(new Card(Rank.Three, Suit.Clubs));

    boolean result = ProbabilityCalculator.hasFullHouse(cards);

    assertThat(result, is(false));
  }

  @Test
  public void givenNotAFullHouseReturnsFalse() {
    ArrayList<Card> cards = new ArrayList<Card>();
    cards.add(new Card(Rank.Ace, Suit.Clubs));
    cards.add(new Card(Rank.Ace, Suit.Spades));
    cards.add(new Card(Rank.Three, Suit.Clubs));
    cards.add(new Card(Rank.Three, Suit.Spades));
    cards.add(new Card(Rank.Ten, Suit.Spades));
    cards.add(new Card(Rank.Jack, Suit.Spades));
    cards.add(new Card(Rank.King, Suit.Spades));

    boolean result = ProbabilityCalculator.hasFullHouse(cards);

    assertThat(result, is(false));
  }

  @Test
  public void givenAFullHouseReturnsTrue() {
    ArrayList<Card> cards = new ArrayList<Card>();
    cards.add(new Card(Rank.Ace, Suit.Clubs));
    cards.add(new Card(Rank.Ace, Suit.Spades));
    cards.add(new Card(Rank.Three, Suit.Clubs));
    cards.add(new Card(Rank.Three, Suit.Spades));
    cards.add(new Card(Rank.Ten, Suit.Spades));
    cards.add(new Card(Rank.Jack, Suit.Spades));
    cards.add(new Card(Rank.Ace, Suit.Hearts));

    boolean result = ProbabilityCalculator.hasFullHouse(cards);

    assertThat(result, is(true));
  }

  @Test
  public void givenBustedHandReturnsZeroProbability() throws CardFormatException,
      BoardFormatException, PocketFormatException, GameStateFormatException {
    GameState game = GameStateFactory.createGameStateFromString("Ah 2h 3h\n Kc Qc");
    ProbabilityCalculator calculator = new ProbabilityCalculator(game);

    double probability = calculator.fullHouseForPlayer(0);

    assertThat(probability, equalTo(0.0));
  }

  @Test
  public void givenTwoPairWithTwoChancesReturnsCorrectProbability() throws CardFormatException,
      BoardFormatException, PocketFormatException, GameStateFormatException {
    GameState game = GameStateFactory.createGameStateFromString("Ah Kh Qh\n Kc Qc");
    ProbabilityCalculator calculator = new ProbabilityCalculator(game);

    double probability = calculator.fullHouseForPlayer(0);

    assertThat(probability, equalTo(181.0 / 1081.0));
  }
}
