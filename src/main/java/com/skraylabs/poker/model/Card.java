package com.skraylabs.poker.model;

/**
 * A playing card.
 */
public class Card {
  /**
   * Playing card rank (A, K, Q, J, 10-2).
   */
  Rank rank;

  /**
   * Playing card suit (Spades, Hearts, Diamond, Clubs).
   */
  Suit suit;

  /**
   * Constructor.
   *
   * @param rank playing card rank
   * @param suit playing card suit
   */
  public Card(Rank rank, Suit suit) {
    this.rank = rank;
    this.suit = suit;
  }

  /**
   * Copy constructor.
   *
   * @param card non-null Card from which to copy member values.
   */
  Card(Card card) {
    this(card.rank, card.suit);
  }

  /**
   * Test equality with another object. Other object must be a Card with identical {link Rank} and
   * {@link Suit} values.
   *
   * @return true if {@code o} is a Card with the same rank and suit; false otherwise.
   */
  @Override
  public boolean equals(Object object) {
    boolean result = false;
    if (object instanceof Card) {
      Card card = (Card) object;
      if (card.rank == this.rank && card.suit == this.suit) {
        result = true;
      }
    }
    return result;
  }

  @Override
  public int hashCode() {
    String hashString = CardFactory.createStringFromCard(this);
    return hashString.hashCode();
  }

  /**
   * Accessor: Playing card rank (A, K, Q, J, 10-2).
   * @return the rank
   */
  public Rank getRank() {
    return rank;
  }

  /**
   * Accessor: Playing card suit (Spades, Hearts, Diamond, Clubs).
   * @return the suit
   */
  public Suit getSuit() {
    return suit;
  }
}
