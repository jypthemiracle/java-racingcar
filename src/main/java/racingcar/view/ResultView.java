package racingcar.view;

import java.util.List;
import java.util.stream.Collectors;
import racingcar.domain.Game;
import racingcar.domain.Results;

public class ResultView {

  private final Game game;
  private final Results results;

  private ResultView(Game game) {
    this.game = game;
    this.results = Results.create();
  }

  public ResultView printView() {
    for (int carSizeIndex = 0; carSizeIndex < game.getCars().getCarList().size(); carSizeIndex++) {
      results.updateResults(makeView(carSizeIndex));
      System.out.println(
          game.getCars().getCarList().get(carSizeIndex).getName() + " " + results.getLastestResult()
      );
    }
    game.calculateWinner(this.game.getCars().getCarList())
        .forEach(element -> System.out.println("우승자: " + element.getName()));
    return this;
  }

  private String makeView(int carSizeIndex) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int positionIndex = 0; positionIndex < game.getCars().getCarList().get(carSizeIndex).getPosition();
        positionIndex++) {
      stringBuilder.append("-");
    }
    return stringBuilder.toString();
  }

  public List<Integer> getResultListSize() {
    return results.getResults().stream().map(String::length).collect(Collectors.toList());
  }

  public static ResultView create(Game game) {
    return new ResultView(game);
  }
}
