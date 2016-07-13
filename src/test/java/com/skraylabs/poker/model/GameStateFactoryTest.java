package com.skraylabs.poker.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

public class GameStateFactoryTest {

  @Rule
  public ExpectedException exception = ExpectedException.none();

  /**
   * Formatted string for a valid Board with 3 Card values. Consists of card #s 49, 50, and 51 --
   * see {@link #cardFromNumber(int)}.
   */
  String threeCardBoardInput;

  /**
   * Serialized version of {@link #threeCardBoardInput}.
   */
  Board threeCardBoard;

  /**
   * Set up shared text fixture.
   *
   * @throws Exception shouldn't happen.
   */
  @Before
  public void setUp() throws Exception {
    Card card49 = cardFromNumber(49);
    Card card50 = cardFromNumber(50);
    Card card51 = cardFromNumber(51);
    String input1 = CardFactory.createStringFromCard(card49);
    String input2 = CardFactory.createStringFromCard(card50);
    String input3 = CardFactory.createStringFromCard(card51);
    threeCardBoardInput = String.format("%s %s %s", input1, input2, input3);
    threeCardBoard = new Board(card49, card50, card51);
  }

  @Test
  public void testInvalidInput_null() throws PokerFormatException {
    // Verify
    exception.expect(GameStateFormatException.class);
    exception.expectMessage(GameStateFormatException.MSG_NULL_INPUT);
    // Exercise
    GameStateFactory.createGameStateFromString(null);
  }

  @Test
  public void testInvalidInput_empty() throws PokerFormatException {
    // Verify
    exception.expect(GameStateFormatException.class);
    exception.expectMessage(GameStateFormatException.MSG_MIN_POCKET_NUM);
    // Exercise
    GameStateFactory.createGameStateFromString("");
  }

  @Test
  public void testInvalidInput_blank() throws PokerFormatException {
    // Verify
    exception.expect(GameStateFormatException.class);
    exception.expectMessage(GameStateFormatException.MSG_MIN_POCKET_NUM);
    // Exercise
    GameStateFactory.createGameStateFromString(" ");
  }

  @Test
  public void testInvalidInput_noPockets() throws PokerFormatException {
    // Setup
    String input = String.format("%s\n", threeCardBoardInput);
    // Verify
    exception.expect(GameStateFormatException.class);
    exception.expectMessage(GameStateFormatException.MSG_MIN_POCKET_NUM);
    // Exercise
    GameStateFactory.createGameStateFromString(input);
  }

  @Test
  public void testInvalidInput_tooManyPockets() throws PokerFormatException {
    // Setup
    // Create a game state with 11 pockets
    StringBuilder builder = new StringBuilder();
    builder.append(String.format("%s\n", threeCardBoardInput));
    for (int i = 0; i < 11; ++i) {
      Card card1 = cardFromNumber(i * 2);
      Card card2 = cardFromNumber(i * 2 + 1);
      String cardInput1 = CardFactory.createStringFromCard(card1);
      String cardInput2 = CardFactory.createStringFromCard(card2);
      builder.append(String.format("%s %s\n", cardInput1, cardInput2));
    }
    String input = builder.toString();
    // Verify
    exception.expect(GameStateFormatException.class);
    exception.expectMessage(GameStateFormatException.MSG_MAX_POCKET_NUM);
    // Exercise
    GameStateFactory.createGameStateFromString(input);
  }

  @Test
  public void testInvalidInput_duplicateCards() throws PokerFormatException {
    // Setup
    String input = "2c 5h 7s Qc\n";
    input += "8s 8h\n";
    input += "\n";
    input += "As 2c";
    String duplicate = "2c";
    // Verify
    String expectedMessage = String.format(GameStateFormatException.MSG_DUPLICATE_CARD, duplicate);
    exception.expect(GameStateFormatException.class);
    exception.expectMessage(expectedMessage);
    // Exercise
    GameStateFactory.createGameStateFromString(input);
  }

  @Test
  public void testInvalidInput_board() throws PokerFormatException {
    // Set up
    String input = "5h 7s";
    // Verify
    exception.expect(BoardFormatException.class);
    // Exercise
    GameStateFactory.createGameStateFromString(input);
  }

  @Test
  public void testInvalidInput_pocket() throws PokerFormatException {
    // Set up
    String input = "5h 7s Th\n";
    input += "As";
    // Verify
    exception.expect(PocketFormatException.class);
    // Exercise
    GameStateFactory.createGameStateFromString(input);
  }

  @Test
  public void testInvalidInput_card() throws PokerFormatException {
    // Set up
    String input = "5h 7s TH\n";
    // Verify
    exception.expect(CardFormatException.class);
    // Exercise
    GameStateFactory.createGameStateFromString(input);
  }

  @Test
  public void testValidInput_emptyBoard() throws PokerFormatException {
    // Set up
    String input = "\n";
    input += "As Ac";
    // Exercise
    GameState sut = GameStateFactory.createGameStateFromString(input);
    // Verify
    Board expectedBoard = new Board();
    Board sutBoard = sut.getBoard();
    assertThat(sutBoard, equalTo(expectedBoard));
  }

  @Test
  public void testValidInput_minPockets() throws PokerFormatException {
    // Setup
    // Create a game state with 1 pocket
    String input = String.format("%s\n", threeCardBoardInput);
    input += "As Ac";
    // Exercise
    GameState sut = GameStateFactory.createGameStateFromString(input);
    // Verify
    Board expectedBoard = threeCardBoard;
    Board sutBoard = sut.getBoard();
    assertThat(sutBoard, equalTo(expectedBoard));
    Pocket expectedPocket0 =
        new Pocket(new Card(Rank.Ace, Suit.Spades), new Card(Rank.Ace, Suit.Clubs));
    Pocket[] sutPockets = sut.getPockets();
    for (int i = 0; i < GameState.MAX_PLAYERS; ++i) {
      Pocket sutPocket = sutPockets[i];
      if (i == 0) {
        assertThat(sutPocket, equalTo(expectedPocket0));
      } else {
        assertThat(sutPocket, is(nullValue()));
      }
    }
  }

  @Test
  public void testValidInput_maxPockets() throws PokerFormatException {
    // Setup
    // Create a game state with 10 pockets
    StringBuilder builder = new StringBuilder();
    builder.append(String.format("%s\n", threeCardBoardInput));
    Pocket[] expectedPockets = new Pocket[GameState.MAX_PLAYERS];
    for (int i = 0; i < GameState.MAX_PLAYERS; ++i) {
      Card card1 = cardFromNumber(i * 2);
      Card card2 = cardFromNumber(i * 2 + 1);
      expectedPockets[i] = new Pocket(card1, card2);
      String cardInput1 = CardFactory.createStringFromCard(card1);
      String cardInput2 = CardFactory.createStringFromCard(card2);
      builder.append(String.format("%s %s\n", cardInput1, cardInput2));
    }
    String input = builder.toString();
    // Exercise
    GameState sut = GameStateFactory.createGameStateFromString(input);
    // Verify
    Board expectedBoard = threeCardBoard;
    Board sutBoard = sut.getBoard();
    assertThat(sutBoard, equalTo(expectedBoard));
    Pocket[] sutPockets = sut.getPockets();
    for (int i = 0; i < GameState.MAX_PLAYERS; ++i) {
      assertThat(sutPockets[i], equalTo(expectedPockets[i]));
    }
  }

  @Test
  public void testValidInput_emptyPocket() throws PokerFormatException {
    // Setup
    Card card0 = cardFromNumber(0);
    Card card1 = cardFromNumber(1);
    String input = this.threeCardBoardInput + "\n";
    input += "\n";
    input += String.format("%s %s", CardFactory.createStringFromCard(card0),
        CardFactory.createStringFromCard(card1));
    // Exercise
    GameState sut = GameStateFactory.createGameStateFromString(input);
    // Verify
    Pocket expectedPocket1 = new Pocket(card0, card1);
    Pocket[] pockets = sut.getPockets();
    assertThat(pockets[0], is(nullValue()));
    assertThat(pockets[1], is(expectedPocket1));
  }

  @Test
  public void testFindDuplicateCards_multiplicity() {
    // Setup
    // Place 4 copies of the same card in the Game
    Card duplicateCard = cardFromNumber(0);
    GameState gameState = new GameState();
    Board board = new Board(duplicateCard, cardFromNumber(1), duplicateCard);
    gameState.setBoard(board);
    Pocket pocket0 = new Pocket(cardFromNumber(2), duplicateCard);
    Pocket pocket1 = new Pocket(cardFromNumber(3), cardFromNumber(4));
    Pocket pocket2 = new Pocket(duplicateCard, cardFromNumber(5));
    gameState.setPocketForPlayer(0, pocket0);
    gameState.setPocketForPlayer(1, pocket1);
    gameState.setPocketForPlayer(2, pocket2);
    // Exercise
    ArrayList<Card> duplicates = GameStateFactory.findDuplicateCards(gameState);
    // Verify
    // For n duplicates, there should be n-1 copies in the return value
    int duplicateCount = 0;
    for (Card card : duplicates) {
      if (card.equals(duplicateCard)) {
        ++duplicateCount;
      }
    }
    assertThat(duplicateCount, is(3));
  }

  @Test
  public void testFindDuplicateCards_order() {
    // Setup
    // Place duplicate cards in an expected order
    Card duplicateCard0 = cardFromNumber(0);
    Card duplicateCard3 = cardFromNumber(3);
    Card duplicateCard4 = cardFromNumber(4);
    Card duplicateCard6 = cardFromNumber(6);
    Card duplicateCard9 = cardFromNumber(9);
    Board board = new Board(duplicateCard0, duplicateCard9, duplicateCard0);
    Pocket pocket0 = new Pocket(duplicateCard4, cardFromNumber(1));
    Pocket pocket1 = new Pocket(cardFromNumber(2), duplicateCard3);
    Pocket pocket2 = new Pocket(duplicateCard3, duplicateCard4);
    Pocket pocket3 = new Pocket(cardFromNumber(5), duplicateCard6);
    Pocket pocket4 = new Pocket(cardFromNumber(7), duplicateCard6);
    Pocket pocket5 = new Pocket(duplicateCard9, cardFromNumber(8));
    GameState gameState = new GameState();
    gameState.setBoard(board);
    gameState.setPocketForPlayer(0, pocket0);
    gameState.setPocketForPlayer(1, pocket1);
    gameState.setPocketForPlayer(2, pocket2);
    gameState.setPocketForPlayer(3, pocket3);
    gameState.setPocketForPlayer(4, pocket4);
    gameState.setPocketForPlayer(5, pocket5);
    // Exercise
    ArrayList<Card> duplicates = GameStateFactory.findDuplicateCards(gameState);
    // Verify
    assertThat(duplicates.size(), is(5));
    assertThat(duplicates.get(0), equalTo(duplicateCard0));
    assertThat(duplicates.get(1), equalTo(duplicateCard3));
    assertThat(duplicates.get(2), equalTo(duplicateCard4));
    assertThat(duplicates.get(3), equalTo(duplicateCard6));
    assertThat(duplicates.get(4), equalTo(duplicateCard9));
  }

  /**
   * Test helper that returns a card based on a number in the range [0, 51].
   *
   * @param number integer in range [0, 51]
   * @return a Card
   */
  static Card cardFromNumber(int number) {
    if (number < 0 || number > 51) {
      throw new IllegalArgumentException("Parameter \"number\" must be in range [0, 51]");
    }
    Card result = new Card(null, null);
    // Assign suit
    switch (number / 13) {
      case 0:
        result.suit = Suit.Spades;
        break;
      case 1:
        result.suit = Suit.Hearts;
        break;
      case 2:
        result.suit = Suit.Diamonds;
        break;
      case 3:
        result.suit = Suit.Clubs;
        break;
      default:
        throw new RuntimeException("Logic error!");
    }
    // Assign rank
    switch (number % 13) {
      case 0:
        result.rank = Rank.Ace;
        break;
      case 1:
        result.rank = Rank.King;
        break;
      case 2:
        result.rank = Rank.Queen;
        break;
      case 3:
        result.rank = Rank.Jack;
        break;
      case 4:
        result.rank = Rank.Ten;
        break;
      case 5:
        result.rank = Rank.Nine;
        break;
      case 6:
        result.rank = Rank.Eight;
        break;
      case 7:
        result.rank = Rank.Seven;
        break;
      case 8:
        result.rank = Rank.Six;
        break;
      case 9:
        result.rank = Rank.Five;
        break;
      case 10:
        result.rank = Rank.Four;
        break;
      case 11:
        result.rank = Rank.Three;
        break;
      case 12:
        result.rank = Rank.Two;
        break;
      default:
        throw new RuntimeException("Logic error!");
    }
    return result;
  }

  @Test
  public void testCardFromNumber_tooLow() {
    // Verify
    exception.expect(IllegalArgumentException.class);
    // Exercise
    cardFromNumber(-1);
  }

  @Test
  public void testCardFromNumber_tooHigh() {
    // Verify
    exception.expect(IllegalArgumentException.class);
    // Exercise
    cardFromNumber(52);
  }

  @Test
  public void testCardFromNumber_noDuplicates() {
    // Exercise
    Card[] cards = new Card[52];
    for (int i = 0; i < 52; ++i) {
      cards[i] = cardFromNumber(i);
    }
    // Verify
    for (int i = 0; i < 52; ++i) {
      Card card = cards[i];
      assertThat(card.rank, notNullValue());
      assertThat(card.suit, notNullValue());
      int numCardsLikeMe = 0;
      for (int j = 0; j < 52; ++j) {
        if (card.equals(cards[j])) {
          numCardsLikeMe++;
        }
      }
      assertThat(numCardsLikeMe, is(1));
    }
  }
}
