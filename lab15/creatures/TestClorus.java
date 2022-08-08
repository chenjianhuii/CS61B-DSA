package creatures;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Action.ActionType;
import huglife.Impassible;
import huglife.Empty;
public class TestClorus {

    @Test
    public void testChoose() {
        Clorus p = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Plip());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        //You can create new empties with new Empty();
        //Despite what the spec says, you cannot test for Cloruses nearby yet.
        //Sorry!  

        Action actual = p.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);

        Clorus p2 = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded2 = new HashMap<Direction, Occupant>();
        surrounded2.put(Direction.TOP, new Empty());
        surrounded2.put(Direction.BOTTOM, new Plip());
        surrounded2.put(Direction.LEFT, new Impassible());
        surrounded2.put(Direction.RIGHT, new Impassible());

        assertEquals(new Action(ActionType.ATTACK, Direction.BOTTOM), p2.chooseAction(surrounded2));

        Clorus p3 = new Clorus(1.0);
        HashMap<Direction, Occupant> surrounded3 = new HashMap<Direction, Occupant>();
        surrounded3.put(Direction.TOP, new Empty());
        surrounded3.put(Direction.BOTTOM, new Impassible());
        surrounded3.put(Direction.LEFT, new Impassible());
        surrounded3.put(Direction.RIGHT, new Impassible());

        assertEquals(new Action(ActionType.REPLICATE, Direction.TOP), p3.chooseAction(surrounded3));

        Clorus p4 = new Clorus(0.9);
        HashMap<Direction, Occupant> surrounded4 = new HashMap<Direction, Occupant>();
        surrounded4.put(Direction.TOP, new Empty());
        surrounded4.put(Direction.BOTTOM, new Impassible());
        surrounded4.put(Direction.LEFT, new Impassible());
        surrounded4.put(Direction.RIGHT, new Impassible());

        assertEquals(new Action(ActionType.MOVE, Direction.TOP), p4.chooseAction(surrounded4));
    }
    
}
