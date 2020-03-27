package za.co.discovery.assignment.planet.helper;

import org.junit.Before;
import org.junit.Test;
import za.co.discovery.assignment.planet.entity.Edge;
import za.co.discovery.assignment.planet.entity.Traffic;
import za.co.discovery.assignment.planet.entity.Vertex;

import java.util.ArrayList;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.Assert.assertEquals;

public class GraphTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void verifyThatTrafficOverlayOnGraphIsCorrect() throws Exception {
        //Set
        List<Vertex> vertices = new ArrayList<>();

        Edge edge1 = new Edge(1, "1", "A", "B", 0.44f);
        Edge edge2 = new Edge(2, "2", "A", "C", 1.89f);
        Edge edge3 = new Edge(3, "3", "A", "D", 0.10f);
        List<Edge> edges = new ArrayList<>();
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);

        Traffic traffic1 = new Traffic("1", "A", "B", 0.30f);
        Traffic traffic2 = new Traffic("2", "A", "C", 0.90f);
        Traffic traffic3 = new Traffic("3", "A", "D", 0.10f);

        List<Traffic> traffics = new ArrayList<>();
        traffics.add(traffic1);
        traffics.add(traffic2);
        traffics.add(traffic3);

        Edge edgeExpected1 = new Edge(1, "1", "A", "B", 0.44f, 0.30f);
        Edge edgeExpected2 = new Edge(2, "2", "A", "C", 1.89f, 0.90f);
        Edge edgeExpected3 = new Edge(3, "3", "A", "D", 0.10f, 0.10f);
        List<Edge> edgesExpected = new ArrayList<>();
        edgesExpected.add(edgeExpected1);
        edgesExpected.add(edgeExpected2);
        edgesExpected.add(edgeExpected3);
        boolean expectedTraffic = true;
        Graph expectedGraph = new Graph(vertices, edgesExpected, traffics);
        expectedGraph.setTrafficAllowed(expectedTraffic);


        //Test
        Graph actualGraph = new Graph(vertices, edges, traffics);
        actualGraph.setTrafficAllowed(true);
        actualGraph.processTraffics();
        boolean actualTraffic = actualGraph.isTrafficAllowed();

        List<Vertex> verticesExpected = expectedGraph.getVertexes();
        List<Traffic> trafficsExpected = expectedGraph.getTraffics();
        //Verify
        assertThat(actualGraph, sameBeanAs(expectedGraph));
        assertThat(actualGraph, sameBeanAs(expectedGraph));
        assertThat(vertices, sameBeanAs(verticesExpected));
        assertThat(traffics, sameBeanAs(trafficsExpected));
        assertThat(actualTraffic, sameBeanAs(expectedTraffic));
    }

    @Test
    public void verifyThatUndirectedEdgesOnGraphIsCorrect() throws Exception {
        //Set
        List<Vertex> vertices = new ArrayList<>();

        Edge edge1 = new Edge(1, "1", "A", "B", 0.44f);
        Edge edge2 = new Edge(2, "2", "A", "C", 1.89f);
        Edge edge3 = new Edge(3, "3", "A", "D", 0.10f);
        List<Edge> edges = new ArrayList<>();
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);

        Traffic traffic1 = new Traffic("1", "A", "B", 0.30f);
        Traffic traffic2 = new Traffic("2", "A", "C", 0.90f);
        Traffic traffic3 = new Traffic("3", "A", "D", 0.10f);

        List<Traffic> traffics = new ArrayList<>();
        traffics.add(traffic1);
        traffics.add(traffic2);
        traffics.add(traffic3);

        boolean expectedUndirected = true;

        //Test
        Graph graph = new Graph(vertices, edges, traffics);
        graph.setUndirectedGraph(true);
        List<Edge> actualEdges = graph.getUndirectedEdges();
        boolean actualUndirected = graph.isUndirectedGraph();

        Graph actualGraph = new Graph(vertices, actualEdges, traffics);

        Edge edgeExpected1 = new Edge(1, "1", "A", "B", 0.44f);
        Edge edgeExpected2 = new Edge(0, "1", "B", "A", 0.44f);
        Edge edgeExpected3 = new Edge(2, "2", "A", "C", 1.89f);
        Edge edgeExpected4 = new Edge(0, "2", "C", "A", 1.89f);
        Edge edgeExpected5 = new Edge(3, "3", "A", "D", 0.10f);
        Edge edgeExpected6 = new Edge(0, "3", "D", "A", 0.10f);
        List<Edge> edgesExpected = new ArrayList<>();
        edgesExpected.add(edgeExpected1);
        edgesExpected.add(edgeExpected2);
        edgesExpected.add(edgeExpected3);
        edgesExpected.add(edgeExpected4);
        edgesExpected.add(edgeExpected5);
        edgesExpected.add(edgeExpected6);

        Graph expectedGraph = new Graph(vertices, edgesExpected, traffics);

        //Verify
        assertThat(actualEdges, sameBeanAs(edgesExpected));
        assertThat(actualGraph, sameBeanAs(expectedGraph));
        assertEquals(actualUndirected, expectedUndirected);
    }

    @Test
    public void verifyThatObjectsAreEqualIsCorrect() throws Exception {
        //Set
        String actualA = "Yes";
        String actualB = "No";
        Object actualObjectA = null;
        Object actualObjectB = null;
        Object actualObjectNotNullA = new Object();
        Object actualObjectNotNullB = new Object();
        Object actualObjectEitherA = null;
        Object actualObjectEitherB = new Object();

        boolean expectedString = false;
        boolean expectedObject = true;
        boolean expectedObjectNotNull = false;
        boolean expectedObjectEither = false;

        Graph graph = new Graph(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        boolean actualString = graph.checkObjectsEqual(actualA, actualB);
        boolean actualObject = graph.checkObjectsEqual(actualObjectB, actualObjectA);
        boolean actualObjectNotNull = graph.checkObjectsEqual(actualObjectNotNullA, actualObjectNotNullB);
        boolean actualObjectEither = graph.checkObjectsEqual(actualObjectEitherA, actualObjectEitherB);

        //Verify
        assertEquals(expectedString, actualString);
        assertEquals(expectedObject, actualObject);
        assertEquals(expectedObjectNotNull, actualObjectNotNull);
        assertEquals(actualObjectEither, expectedObjectEither);
    }

}