package za.co.discovery.assignment.planet.service;

import org.junit.Test;
import za.co.discovery.assignment.planet.entity.Edge;
import za.co.discovery.assignment.planet.entity.Traffic;
import za.co.discovery.assignment.planet.entity.Vertex;
import za.co.discovery.assignment.planet.helper.Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.Assert.assertThat;

public class ShortestPathServiceTest {

    @Test
    public void verifyThatShortestPathAlgorithmIsCorrect() throws Exception {
        Vertex vertex1 = new Vertex("A", "Earth");
        Vertex vertex2 = new Vertex("B", "Moon");
        Vertex vertex3 = new Vertex("C", "Jupiter");
        Vertex vertex4 = new Vertex("D", "Venus");
        Vertex vertex5 = new Vertex("E", "Mars");

        List<Vertex> vertices = new ArrayList<>();
        vertices.add(vertex1);
        vertices.add(vertex2);
        vertices.add(vertex3);
        vertices.add(vertex4);
        vertices.add(vertex5);

        Edge edge1 = new Edge(1, "1", "A", "B", 1.0f);
        Edge edge2 = new Edge(2, "2", "A", "C", 1.0f);
        Edge edge3 = new Edge(3, "3", "A", "D", 1.0f);
        Edge edge4 = new Edge(4, "4", "B", "E", 1.0f);
        Edge edge5 = new Edge(5, "5", "C", "E", 1.0f);
        Edge edge6 = new Edge(6, "5", "X", "Y", 1.0f);

        List<Edge> edges = new ArrayList<>();
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);
        edges.add(edge5);
        edges.add(edge6);

        Traffic traffic1 = new Traffic("1", "A", "B", 5.0f);
        Traffic traffic2 = new Traffic("2", "A", "C", 5.0f);
        Traffic traffic3 = new Traffic("3", "A", "D", 5.0f);
        Traffic traffic4 = new Traffic("4", "B", "E", 15.0f);
        Traffic traffic5 = new Traffic("5", "C", "E", 5.0f);

        List<Traffic> traffics = new ArrayList<>();
        traffics.add(traffic1);
        traffics.add(traffic2);
        traffics.add(traffic3);
        traffics.add(traffic4);
        traffics.add(traffic5);

        String expectedPath = "A C E ";

        //Test
        StringBuilder path = new StringBuilder();
        Vertex source = vertices.get(0);
        Vertex destination = vertices.get(vertices.size() - 1);
        Graph graph = new Graph(vertices, edges, traffics);
        graph.setTrafficAllowed(true);
        graph.setUndirectedGraph(true);
        ShortestPathService shortestPathService = new ShortestPathService();
        shortestPathService.initializePlanets(graph);
        shortestPathService.run(source);
        LinkedList<Vertex> paths = shortestPathService.getPath(destination);

        if (paths != null) {
            for (Vertex v : paths) {
                path.append(v.getVertexId());
                path.append(" ");
            }
        } else {
            path.append("Not available");
        }

        String actual = path.toString();
        assertThat(expectedPath, sameBeanAs(actual));
    }
}