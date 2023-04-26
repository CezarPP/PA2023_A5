package org.example;

import org.graph4j.Graph;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Agent {
    private final Graph graph;
    private final Set<Integer> visited;
    private final LinkedList<Integer> path;
    private final ReentrantLock lock;

    Agent(Graph graph, int startNode) {
        this.graph = graph;
        this.visited = new HashSet<>();
        this.path = new LinkedList<>();
        this.path.add(startNode);
        this.visited.add(startNode);
        this.lock = new ReentrantLock();
    }

    public boolean isFinished() {
        lock.lock();
        try {
            return !path.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    public void explore() {
        lock.lock();
        try {
            if (isFinished())
                return;
            assert (!path.isEmpty());
            int currentNode = path.peek();
            Integer nextNode = getNextUnvisitedNode(currentNode);

            if (nextNode != null) {
                path.push(nextNode);
                visited.add(nextNode);
            } else {
                path.pop();
            }
        } finally {
            lock.unlock();
        }
    }

    public void meet(Agent other) {
        acquireTwoLocks(this.lock, other.lock);
        try {
            for (Integer node : other.visited) {
                if (!visited.contains(node)) {
                    visited.add(node);
                    path.push(node);
                }
            }
        } finally {
            this.lock.unlock();
            other.lock.unlock();
        }
    }

    public void acquireTwoLocks(ReentrantLock lock1, ReentrantLock lock2) {
        boolean acquiredLock1 = false;
        boolean acquiredLock2 = false;

        while (!acquiredLock1 || !acquiredLock2) {
            try {
                acquiredLock1 = lock1.tryLock();
                acquiredLock2 = lock2.tryLock();
            } finally {
                if (acquiredLock1 && !acquiredLock2) {
                    lock1.unlock();
                } else if (!acquiredLock1 && acquiredLock2) {
                    lock2.unlock();
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Error sleeping agent" + e.getMessage());
            }
        }
    }


    private Integer getNextUnvisitedNode(int currentNode) {
        for (int it : graph.neighbors(currentNode)) {
            if (!visited.contains(it)) {
                return it;
            }
        }
        return null;
    }

    public LinkedList<Integer> getPath() {
        return path;
    }
}
