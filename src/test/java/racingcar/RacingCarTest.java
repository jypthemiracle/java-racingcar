package racingcar;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import racingcar.domain.Car;
import racingcar.domain.Game;
import racingcar.util.RandomUtil;

@DisplayName("자동차 경주의 핵심 로직을 테스트한다.")
public class RacingCarTest {

  @Test
  void 랜덤값이_0과_9_사이이다() {
    assertThat(RandomUtil.getRandomValue()).isBetween(0, 9);
  }

  @Test
  void 랜덤값이_4이상일때_전진한다() {

    //given
    Car car1 = Car.create("first car");
    int previousPositionOfCar1 = car1.getPosition();
    Car car2 = Car.create("second car");
    Car car3 = Car.create("third car");
    Car car4 = Car.create("fourth car");
    Car car5 = Car.create("fifth car");

    List<Car> carList = new ArrayList<>();
    carList.add(car1);
    carList.add(car2);
    carList.add(car3);
    carList.add(car4);
    carList.add(car5);

    Game game = Game.create(carList);

    //when
    assertThat(game.doRace(3).get(0).getPosition())
        //then
        .isEqualTo(previousPositionOfCar1);

    //when
    assertThat(game.doRace(4).get(0).getPosition())
        //then
        .isGreaterThan(previousPositionOfCar1);
  }
}
