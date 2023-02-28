import static java.lang.Integer.parseInt;

public class Lab1 {
    public static int getControlDigit(int n) {
        while (n > 10) {
            int s = 0;
            while (n != 0) {
                s += n % 10;
                n /= 10;
            }
            n = s;
        }
        return n;
    }

    public static void main(String[] args) {
        Lab1 lab1 = new Lab1();
        // lab1.bonus(args);
        lab1.bonus2(args);
    }

    /**
     * Always prints "Java"
     */
    void compulsory() {
        System.out.println("Hello world!");
        String[] languages = {"C", "C++", "C#", "Python", "Go", "Rust", "JavaScript", "PHP", "Swift", "Java"};
        int n = (int) (Math.random() * 1_000_000);
        n *= 3;
        n += 0b10101;
        n += 0xFF;
        n = n * 6;
        n = getControlDigit(n);
        System.out.println("Willy-nilly, this semester I will learn " + languages[n]);
    }

    /**
     * Creates a latin matrix of size n
     * if n is less than or equal to 500, it prints the matrix
     *
     * @param args -> n = args[0]
     */
    void homework(String[] args) {
        long startTime = System.currentTimeMillis();
        if (args.length < 1) {
            System.out.println("Error, size of matrix not provided in arguments");
            System.exit(-1);
        }
        int n = 0;
        try {
            n = parseInt(args[0]);
            assert (n > 0);
        } catch (Exception e) {
            System.out.println("Argument can't be converted to a positive integer");
            System.exit(-1);
        }

        Matrix matrix = new Matrix(n);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                matrix.m[i][j] = (j + i) % n + 1;

        if (n <= 500) {
            matrix.printWithoutSpaces();
        }
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        System.out.println("Runtime is " + runTime + " ms");
    }

    /**
     * The function prints
     * the adjacency matrix A for a cycle graph of degree args[0] raised to powers 2, 3,...args[0]
     * (A^k)[i][j] -> number of paths of length exactly k from i to j
     *
     * @param args -> command line arguments
     */
    void bonus(String[] args) {
        if (args.length < 1) {
            System.out.println("Error: Please provide the size of the graph");
            System.exit(-1);
        }
        int n = 0;
        try {
            n = parseInt(args[0]);
            assert (n > 0);
        } catch (Exception e) {
            System.out.println("Error: Can't convert provided argument to a positive int");
            System.exit(-1);
        }
        Matrix c = new Matrix(n);
        c.setToCycleGraph();
        for (int i = 2; i <= n; i++) {
            c.pow(i).print();
        }
    }

    /**
     * Prints a graph of size n that is k-regular
     *
     * @param args -> n = args[0] size of graph, k = args[1] degree of vertices
     */
    void bonus2(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: Please provide the size of the graph and the degree");
            System.exit(-1);
        }
        int n = 0, k = 0;
        try {
            n = parseInt(args[0]);
            k = parseInt(args[1]);
            assert (n > 0);
            assert (k > 0);
        } catch (Exception e) {
            System.out.println("Error: Can't convert provided arguments to a positive ints");
            System.exit(-1);
        }
        if (k >= n || (k % 2 == 1 && n % 2 == 1)) {
            System.out.println("Error: can't have a regular graph with size " + n + " and degree " + k);
            System.exit(-1);
        }

        Matrix kRegGraph = new Matrix(n);
        /*
         connect i to i - 1, i - 2...i - k / 2
               and to i + 1, i + 2...i + k / 2
         */
        for (int i = 0; i < n; i++) {
            for (int x = 1; x <= k / 2; x++) {
                kRegGraph.addEdge(i, (i + x) % n);
                kRegGraph.addEdge(i, (i - x + n) % n);
            }
        }

        if (k % 2 != 0) {
            for (int i = 0; i < n; i++)
                kRegGraph.addEdge(i, (i + n / 2) % n);
        }

        kRegGraph.print();
    }
}