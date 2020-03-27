package za.co.discovery.assignment.planet.service;

import org.junit.Before;
import org.junit.Test;
import za.co.discovery.assignment.planet.entity.Edge;
import za.co.discovery.assignment.planet.entity.Traffic;
import za.co.discovery.assignment.planet.entity.Vertex;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

public class XLSXHandlerTest {
    private static final String EXCEL_FILENAME = "/test.xlsx";
    private XLSXHandler xlsxHandler;

    @Before
    public void setUp() throws Exception {
        URL resource = getClass().getResource(EXCEL_FILENAME);
        File file1 = new File(resource.toURI());
        xlsxHandler = new XLSXHandler(file1);
    }

    @Test
    public void verifyThatReadingVerticesFromFileIsCorrect() throws Exception {
        //Set
        Vertex vertex1 = new Vertex("A", "Earth");
        Vertex vertex2 = new Vertex("B", "Moon");
        Vertex vertex3 = new Vertex("C", "Jupiter");
        Vertex vertex4 = new Vertex("D", "Venus");
        Vertex vertex5 = new Vertex("E", "Mars");

        List<Vertex> expectedVertexes = new ArrayList<>();
        expectedVertexes.add(vertex1);
        expectedVertexes.add(vertex2);
        expectedVertexes.add(vertex3);
        expectedVertexes.add(vertex4);
        expectedVertexes.add(vertex5);

        //Test
        List<Vertex> readVertexes = xlsxHandler.readVertexes();
        //Verify
        assertThat(expectedVertexes, sameBeanAs(readVertexes));
    }

    @Test
    public void verifyThatReadingEdgesFromFileIsCorrect() throws Exception {
        //Set
        Edge edge1 = new Edge(1, "1", "A", "B", 0.44f);
        Edge edge2 = new Edge(2, "2", "A", "C", 1.89f);
        Edge edge3 = new Edge(3, "3", "A", "D", 0.10f);
        Edge edge4 = new Edge(4, "4", "B", "H", 2.44f);
        Edge edge5 = new Edge(5, "5", "B", "E", 3.45f);

        List<Edge> expectedEdges = new ArrayList<>();
        expectedEdges.add(edge1);
        expectedEdges.add(edge2);
        expectedEdges.add(edge3);
        expectedEdges.add(edge4);
        expectedEdges.add(edge5);

        //Test
        List<Edge> readEdges = xlsxHandler.readEdges();
        //Verify
        assertThat(expectedEdges, sameBeanAs(readEdges));
    }

    @Test
    public void verifyThatReadingTrafficsFromFileIsCorrect() throws Exception {
        //Set
        Traffic traffic1 = new Traffic("1", "A", "B", 0.30f);
        Traffic traffic2 = new Traffic("2", "A", "C", 0.90f);
        Traffic traffic3 = new Traffic("3", "A", "D", 0.10f);
        Traffic traffic4 = new Traffic("4", "B", "H", 0.20f);
        Traffic traffic5 = new Traffic("5", "B", "E", 1.30f);

        List<Traffic> expectedTraffics = new ArrayList<>();
        expectedTraffics.add(traffic1);
        expectedTraffics.add(traffic2);
        expectedTraffics.add(traffic3);
        expectedTraffics.add(traffic4);
        expectedTraffics.add(traffic5);

        //Test
        List<Traffic> readTraffics = xlsxHandler.readTraffics();
        //Verify
        assertThat(expectedTraffics, sameBeanAs(readTraffics));
    }

}