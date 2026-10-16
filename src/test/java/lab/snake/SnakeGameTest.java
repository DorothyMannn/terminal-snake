package lab.snake;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SnakeGameTest {
    @Test void normalMovePreservesLength() {
        SnakeGame g=new SnakeGame(Scenario.NORMAL);g.tick(Direction.RIGHT);
        assertEquals(new Cell(4,2),g.head());assertEquals(3,g.body().size());assertEquals(SnakeGame.Status.RUNNING,g.status());
    }
    @Test void foodGrowsSnakeAndAddsTen() {
        SnakeGame g=new SnakeGame(Scenario.PICNIC);g.tick(Direction.RIGHT);
        assertEquals(4,g.body().size());assertEquals(10,g.score());assertFalse(g.body().contains(g.food()));
    }
    @Test void reverseDirectionIsIgnored() {
        SnakeGame g=new SnakeGame(Scenario.NORMAL);g.tick(Direction.LEFT);
        assertEquals(new Cell(4,2),g.head());assertEquals(Direction.RIGHT,g.direction());
    }
    @Test void topWallEndsRound() {
        SnakeGame g=new SnakeGame(Scenario.NORMAL);
        g.tick(Direction.UP);g.tick(Direction.UP);g.tick(Direction.UP);
        assertEquals(SnakeGame.Status.WALL,g.status());assertEquals(new Cell(3,0),g.head());
    }
    @Test void endedRoundDoesNotMove() {
        SnakeGame g=new SnakeGame(Scenario.NORMAL);
        g.tick(Direction.UP);g.tick(Direction.UP);g.tick(Direction.UP);Cell head=g.head();
        g.tick(Direction.RIGHT);assertEquals(head,g.head());assertEquals(SnakeGame.Status.WALL,g.status());
    }
    @Test void restartRestoresStartingBody() {
        SnakeGame g=new SnakeGame(Scenario.NORMAL);g.tick(Direction.RIGHT);g.reset();
        assertEquals(Scenario.NORMAL.snake,g.body());assertEquals(Scenario.NORMAL.food,g.food());assertEquals(SnakeGame.Status.RUNNING,g.status());
    }

    @Test void loopSteppingOntoOldTailCellIsLegal() {
        SnakeGame g = new SnakeGame(Scenario.LOOP);
        Cell oldTail = Scenario.LOOP.snake.get(Scenario.LOOP.snake.size() - 1);
        g.tick(Direction.LEFT);
        assertEquals(SnakeGame.Status.RUNNING, g.status());
        assertEquals(oldTail, g.head());
        assertEquals(4, g.body().size());
        assertEquals(0, g.score());
        assertEquals(java.util.List.of(new Cell(1,2), new Cell(2,2), new Cell(2,3), new Cell(1,3)), g.body());
    }

    @Test void loopCyclesThroughVacatedTailCells() {
        SnakeGame g = new SnakeGame(Scenario.LOOP);
        g.tick(Direction.LEFT); g.tick(Direction.DOWN); g.tick(Direction.RIGHT); g.tick(Direction.UP);
        assertEquals(SnakeGame.Status.RUNNING, g.status());
        assertEquals(new Cell(2,2), g.head());
        assertEquals(Scenario.LOOP.snake, g.body());
        assertEquals(0, g.score());
    }

    @Test void loopTailEntryIsReproducible() {
        SnakeGame a = new SnakeGame(Scenario.LOOP); a.tick(Direction.LEFT);
        SnakeGame b = new SnakeGame(Scenario.LOOP); b.tick(Direction.LEFT);
        assertEquals(a.snapshot(), b.snapshot());
    }
}
