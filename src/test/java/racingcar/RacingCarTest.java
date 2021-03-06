package racingcar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import racingcar.controller.GameService;
import racingcar.domain.Car;
import racingcar.domain.Cars;
import racingcar.domain.CarsMock;
import racingcar.domain.Game;
import racingcar.domain.GameMock;
import racingcar.util.AlwaysUpdatingRandomMovingStrategy;
import racingcar.util.NotUpdatingRandomMovingStrategy;
import racingcar.util.RandomUtil;
import racingcar.view.ResultView;

@DisplayName("자동차 경주의 핵심 로직을 테스트한다.")
public class RacingCarTest {

  GameService gameService;

  @BeforeEach
  void setUp() {
    int attemptNum = 3;
    String line = "honux, pobi, crong";
    String[] names = line.split(", ");
    Cars cars = Cars.create(names);
    Game game = Game.create(cars);
    gameService = GameService.create(attemptNum, game);
  }


  @Test
  void 랜덤값이_0과_9_사이이다() {
    assertThat(RandomUtil.getRandomValue()).isBetween(0, 9);
  }

  @Test
  void 랜덤값이_4이상일때_전진한다() {
    //given
    String[] names = {"pobi", "crong", "honux"};
    Cars cars = Cars.create(names);
    int previousPositionOfCar1 = cars.getCarList().get(0).getPosition();

    //when
    assertThat(cars.getCarList().get(0).updatePosition(NotUpdatingRandomMovingStrategy.create()))
        //then
        .isEqualTo(previousPositionOfCar1);

    //when
    assertThat(cars.getCarList().get(0).updatePosition(AlwaysUpdatingRandomMovingStrategy.create()))
        //then
        .isGreaterThan(previousPositionOfCar1);
  }

  @Test
  void 입력한_횟수만큼_자동차가_경주된다() {

    //given
    int attemptNum = 4;
    String[] names = {"pobi", "crong", "honux"};
    Cars cars = Cars.create(names);
    GameMock gameMock = GameMock.create(cars);
    GameService gameService = GameService.create(attemptNum, gameMock);

    //when
    gameService.proceedGame();

    //then
    assertThat(gameMock.getAttemptNum()).isEqualTo(attemptNum);
  }

  @Test
  void 자동차의_전진수만큼_하이픈이_출력된다() {
    Game game = gameService.proceedGame();
    ResultView resultView = gameService.getResults();
    assertThat(game.getCars().getCarList()
        .stream()
        .map(Car::getPosition))
        .isEqualTo(resultView.getResultListSize());
  }

  @Test
  void 자동차마다_생성되는_레이싱_수가_다르다() {
    Game game = gameService.proceedGame();
    IntStream.range(0, game.getCars().getCarList().size() - 1).forEach(i -> {
      assertThat(game.getCars().getCarList().get(i).getPosition())
          .isNotEqualTo(game.getCars().getCarList().get(i + 1).getPosition());
    });
  }

  @Test
  void 입력한_이름이_자동차_이름이_된다() {
    assertAll(
        () -> assertThat(gameService.getGame().getCars().getCarList().get(0).getName()).isEqualTo("honux"),
        () -> assertThat(gameService.getGame().getCars().getCarList().get(1).getName()).isEqualTo("pobi"),
        () -> assertThat(gameService.getGame().getCars().getCarList().get(2).getName()).isEqualTo("crong")
    );
  }

  @Test
  void 우승자가_한_명일때_정상적으로_출력된다() {

    //given
    Car car1 = Car.create("honux");
    car1.updatePosition(AlwaysUpdatingRandomMovingStrategy.create());
    Car car2 = Car.create("crong");
    car2.updatePosition(NotUpdatingRandomMovingStrategy.create());
    Car car3 = Car.create("pobi");
    car3.updatePosition(NotUpdatingRandomMovingStrategy.create());

    List<Car> carList = new ArrayList<>();
    carList.add(car1);
    carList.add(car2);
    carList.add(car3);

    CarsMock cars = CarsMock.create(carList);

    //when
    GameMock game = GameMock.create(cars);

    //then
    assertThat(game.calculateWinner(carList).size()).isEqualTo(1);
    assertThat(game.calculateWinner(carList).get(0).getName()).isEqualTo("honux");
  }

  @Test
  void 우승자가_두_명일때_정상적으로_출력된다() {

    //given
    Car car1 = Car.create("honux");
    car1.updatePosition(AlwaysUpdatingRandomMovingStrategy.create());
    Car car2 = Car.create("crong");
    car2.updatePosition(AlwaysUpdatingRandomMovingStrategy.create());
    Car car3 = Car.create("pobi");
    car3.updatePosition(NotUpdatingRandomMovingStrategy.create());

    List<Car> carList = new ArrayList<>();
    carList.add(car1);
    carList.add(car2);
    carList.add(car3);

    CarsMock cars = CarsMock.create(carList);

    //when
    Game game = Game.create(cars);

    //then
    assertThat(game.calculateWinner(carList).size()).isEqualTo(2);
    assertThat(game.calculateWinner(carList).get(0).getName()).isEqualTo("honux");
    assertThat(game.calculateWinner(carList).get(1).getName()).isEqualTo("crong");
  }
}
