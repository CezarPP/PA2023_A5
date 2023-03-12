# Lab3
* [x] Compulsory
* [x] Homework

Homework main:
```java
public class Main {
    public static void main(String[] args) {
        Network network = Network.getRandomNetwork(10, 15);
        network.printNodes();
        network.printNetwork();
    }
}
```

Sample output for homework:
```
The network has 10 nodes
NEEDCZ
ONZCVVSLE
PLSNE
JRHEFQ
YREVBPQR
IPYUJVUS
XKAHITCKE
F
AIGBRMPTT
FCMLVP
Nodes printed in increasing order of importance
XKAHITCKE
NEEDCZ
YREVBPQR
IPYUJVUS
AIGBRMPTT
FCMLVP
ONZCVVSLE
PLSNE
F
JRHEFQ
```

Bonus:
* [x] Find articulation points
* [x] Find biconnected components
* [x] Add JUnit tests

Sample output of second unit test:
```
The articulation points are [0]
The biconnected components are [[Pair{x=0, y=2}], [Pair{x=0, y=3}], [Pair{x=0, y=4}], [Pair{x=0, y=1}]]
```