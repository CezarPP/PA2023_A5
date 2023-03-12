import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class NetworkTest {

    @Test
    @DisplayName("Two points, no articulation points")
    void smallFindCutPoints() {
        Network network = new Network(2);
        Person Andrew = new Person("Andrew", LocalDate.now());
        Company JavaInc = new Company("JavaInc");
        assertTrue(network.addNode(Andrew));
        assertTrue(network.addNode(JavaInc));
        network.addConnection(0, 1);
        ArrayList<Integer> articulationPoints = network.findCutPoints();
        assertEquals(0, articulationPoints.size());
    }

    @Test
    @DisplayName("5 points, 1 articulation point")
    void oneFindCutPoints() {
        Network network = new Network(5);
        Company JavaInc = new Company("JavaInc");
        Person Andrew = new Person("Andrew", LocalDate.now());
        Designer Anne = new Designer("Anne", LocalDate.now(), 3);
        Programmer Jeff = new Programmer("Jeff", LocalDate.now(), 4);
        Person Bezos = new Person("Bezos", LocalDate.now());
        assertTrue(network.addNode(JavaInc));
        assertTrue(network.addNode(Andrew));
        assertTrue(network.addNode(Anne));
        assertTrue(network.addNode(Jeff));
        assertTrue(network.addNode(Bezos));
        network.addConnection(0, 1);
        network.addConnection(0, 2);
        network.addConnection(0, 3);
        network.addConnection(0, 4);
        ArrayList<Integer> articulationPoints = network.findCutPoints();
        assertEquals(1, articulationPoints.size());
        assertEquals(0, articulationPoints.get(0));
    }
}