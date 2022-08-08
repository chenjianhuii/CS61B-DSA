package creatures;

import java.awt.Color;
import java.util.Map;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.HugLifeUtils;
import huglife.Occupant;
import huglife.Action.ActionType;

import java.util.Map;
import java.util.List;


public class Clorus extends Creature{

    public Clorus(double e) {
        super("clorus");
        energy = e;
    }

    public Clorus() {
        this(0.5);
    }

    @Override
    public void move() {
        energy -= 0.03;
    }

    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    @Override
    public Creature replicate() {
        Creature newClorus = new Clorus(energy / 2);
        energy /= 2;
        return newClorus;
    }

    @Override
    public void stay() {
        energy -= 0.01;
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        if (empties.isEmpty()) {
            return new Action(ActionType.STAY);
        }
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");
        if (!plips.isEmpty()) {
            Direction moveDir = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, moveDir);
        }
        Direction moveDir = HugLifeUtils.randomEntry(empties);
        if (energy - 1 > -EPS) {
            return new Action(Action.ActionType.REPLICATE, moveDir);
        }
        return new Action(Action.ActionType.MOVE, moveDir);
    }

    @Override
    public Color color() {
        return color(34, 0, 231);
    }

    private final static double EPS = 1e-6;
}
