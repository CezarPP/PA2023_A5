package com.example.lab11;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/graph")
public class GraphController {
    @GetMapping
    List<Integer> getC() {
        List<List<Integer>> graph = GraphPartitioner.getArbitraryGraph(15, 0.2);
        GraphPartitioner graphPartitioner = new GraphPartitioner(graph);
        return graphPartitioner.solve();
    }
}
