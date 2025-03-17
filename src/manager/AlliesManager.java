package manager;

import entity.MovedObject.Allies;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AlliesManager {


    private Queue<Allies> alliesQueue = new LinkedList<>();


    public void add(Allies ally) {
        alliesQueue.offer(ally);
    }


    public void update() {
        for (Allies ally : alliesQueue) {
            ally.update();
        }
    }


    public void clear() {
        alliesQueue.clear();
    }


    public List<Allies> getAlliesList() {
        return new ArrayList<>(alliesQueue);
    }
}
