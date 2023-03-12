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