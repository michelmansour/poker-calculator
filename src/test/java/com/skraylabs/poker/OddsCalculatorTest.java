package com.skraylabs.poker;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.skraylabs.poker.model.BoardFormatException;
import com.skraylabs.poker.model.CardFormatException;
import com.skraylabs.poker.model.GameState;
import com.skraylabs.poker.model.GameStateFactory;
import com.skraylabs.poker.model.GameStateFormatException;
import com.skraylabs.poker.model.PocketFormatException;

import org.junit.Test;

public class OddsCalculatorTest {

  @Test
  public void completeBoardYieldsZeroProbabilityOfTwoOfAKind() throws CardFormatException,
      BoardFormatException, PocketFormatException, GameStateFormatException {

    GameState state = GameStateFactory.createGameStateFromString("Ah Kh Qh Jh Th\n" + "2d 7c");
    OddsCalculator calculator = new OddsCalculator(state);

    double probability = calculator.twoOfAKindForPlayer(0);

    assertThat(probability, equalTo(0.0));
  }

}