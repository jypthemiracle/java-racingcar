package racingcar;

import racingcar.controller.GameController;
import racingcar.domain.Cars;
import racingcar.domain.Game;
import racingcar.view.InputView;
import racingcar.view.ResultView;

public class RacingGame {

  public static void main(String[] args) {
    String[] names = InputView.getUserStringInput(InputView.CAR_INPUT_MESSAGE).split(", ");
    int attemptNum = InputView.getUserNumInput(InputView.ATTEMPT_INPUT_MESSAGE);

    Game beforeGame = Game.create(Cars.create(names));
    GameController gameController = GameController.create(attemptNum, beforeGame);
    Game playedGame = gameController.proceedGame();
    ResultView resultView = ResultView.create(playedGame);
    resultView.printView();
  }
}
