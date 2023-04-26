package org.example;

import org.graph4j.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.*;

public class FastCollaborativeGraphExploration {
    private final Graph graph;
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    FastCollaborativeGraphExploration(Graph graph) {
        this.graph = graph;
    }

    public void exploreGraph(int cntAgents) {
        int N = graph.vertices().length;
        List<Agent> agents = new ArrayList<>();
        Random random = new Random();
        ConcurrentHashMap<Integer, Agent> agentPositions = new ConcurrentHashMap<>();

        for (int i = 0; i < cntAgents; i++) {
            Agent agent = new Agent(graph, random.nextInt(N));
            agents.add(agent);
            agentPositions.put(i, agent);
        }

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < cntAgents; i++) {
            final int agentId = i;
            executor.execute(() -> {
                Agent currentAgent = agents.get(agentId);
                while (!currentAgent.isFinished()) {
                    currentAgent.explore();
                    agentPositions.replace(agentId, currentAgent);

                    handleAgentMeetings(agentPositions, currentAgent);
                }
            });
        }

        executor.shutdown();
        try {
            boolean done = executor.awaitTermination(10000, TimeUnit.MILLISECONDS);
            if (done)
                System.out.println("Executor finished");
            else
                System.out.println("Executor not finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Error awaiting termination of executor" + e.getMessage());
        }
    }

    private static void handleAgentMeetings(ConcurrentHashMap<Integer, Agent> agentPositions, Agent currentAgent) {
        for (Agent agent : agentPositions.values()) {
            if (currentAgent != agent &&
                    !currentAgent.getPath().isEmpty() &&
                    !agent.getPath().isEmpty() &&
                    Objects.equals(currentAgent.getPath().peek(), agent.getPath().peek())) {
                currentAgent.meet(agent);
            }
        }
    }
}
