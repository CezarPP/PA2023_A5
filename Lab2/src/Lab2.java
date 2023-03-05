public class Lab2 {
    public static void main(String[] args) {
        Problem p = new Problem(Problem.getRandomProblem(25, 30));
        assert (p.isValidProblem());

        for (int i = 0; i < 10; i++) {
            Solution sol = new Solution(Problem.getRandomProblem(100, 200));
            if (sol.isReachable())
                System.out.println("For random problem " + i + " the answer is " + sol.findShortestRoute());
            else
                System.out.println("For random problem " + i + " the vertex is not reachable");
        }
    }
}