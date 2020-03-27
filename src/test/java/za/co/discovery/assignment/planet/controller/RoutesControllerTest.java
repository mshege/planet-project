package za.co.discovery.assignment.planet.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;
import za.co.discovery.assignment.planet.entity.Edge;
import za.co.discovery.assignment.planet.entity.Traffic;
import za.co.discovery.assignment.planet.entity.Vertex;
import za.co.discovery.assignment.planet.model.ShortestPathModel;
import za.co.discovery.assignment.planet.service.RepositoryManagerService;

import java.util.ArrayList;
import java.util.List;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class RoutesControllerTest {
    @Mock
    View mockView;
    @InjectMocks
    private RoutesController controller;
    @Mock
    private RepositoryManagerService repositoryManagerService;
    private List<Vertex> vertices;
    private List<Edge> edges;
    private List<Traffic> traffics;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        Vertex vertex1 = new Vertex("A", "Earth");
        Vertex vertex2 = new Vertex("B", "Moon");
        Vertex vertex3 = new Vertex("C", "Jupiter");
        Vertex vertex4 = new Vertex("D", "Venus");
        Vertex vertex5 = new Vertex("E", "Mars");

        vertices = new ArrayList<>();
        vertices.add(vertex1);
        vertices.add(vertex2);
        vertices.add(vertex3);
        vertices.add(vertex4);
        vertices.add(vertex5);

        Edge edge1 = new Edge(1, "1", "A", "B", 0.44f);
        Edge edge2 = new Edge(2, "2", "A", "C", 1.89f);
        Edge edge3 = new Edge(3, "3", "A", "D", 0.10f);
        Edge edge4 = new Edge(4, "4", "B", "H", 2.44f);
        Edge edge5 = new Edge(5, "5", "B", "E", 3.45f);

        edges = new ArrayList<>();
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);
        edges.add(edge5);

        Traffic traffic1 = new Traffic("1", "A", "B", 0.30f);
        Traffic traffic2 = new Traffic("2", "A", "C", 0.90f);
        Traffic traffic3 = new Traffic("3", "A", "D", 0.10f);
        Traffic traffic4 = new Traffic("4", "B", "H", 0.20f);
        Traffic traffic5 = new Traffic("5", "B", "E", 1.30f);

        traffics = new ArrayList<>();
        traffics.add(traffic1);
        traffics.add(traffic2);
        traffics.add(traffic3);
        traffics.add(traffic4);
        traffics.add(traffic5);
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(controller).setSingleView(mockView).build();

    }

    @Test
    public void verifyThatSaveExistingVertexViewAndModelIsCorrect() throws Exception {
        //Set
        Vertex expectedVertex = new Vertex("A", "Earth");
        when(repositoryManagerService.vertexExist("A")).thenReturn(true);
        when(repositoryManagerService.getVertexById("A")).thenReturn(expectedVertex);
        String message = "Planet A already exists as Earth";
    }

    @Test
    public void verifyThatListEdgesViewAndModelIsCorrect() throws Exception {
        //Set
        when(repositoryManagerService.getAllEdges()).thenReturn(edges);
        //Verify
        mockMvc.perform(get("/edges")).andExpect(model().attribute("edges", sameBeanAs(edges))).andExpect(view().name("edges"));
    }

    @Test
    public void verifyThatShowEdgeViewAndModelIsCorrect() throws Exception {
        //Set
        Edge expectedEdge = new Edge(2, "2", "A", "C", 1.89f);
        long recordId = 2;
        when(repositoryManagerService.getEdgeById(recordId)).thenReturn(expectedEdge);
        //Verify
        mockMvc.perform(get("/edge/" + recordId)).andExpect(status().isOk()).andExpect(model().attribute("edge", sameBeanAs(expectedEdge))).andExpect(view().name("edgeshow"));
    }

    @Test
    public void verifyThatDeleteEdgeViewIsCorrect() throws Exception {
        //Set
        long recordId = 2;
        when(repositoryManagerService.deleteEdge(recordId)).thenReturn(true);
        //Verify
        mockMvc.perform(post("/edge/delete/" + recordId)).andExpect(status().isOk()).andExpect(view().name("redirect:/edges"));
    }

    @Test
    public void verifyThatAddEdgeViewAndModelIsCorrect() throws Exception {
        //Set
        Edge expectedEdge = new Edge();
        ShortestPathModel sh = new ShortestPathModel();
        when(repositoryManagerService.getAllVertices()).thenReturn(vertices);
        //Verify
        mockMvc.perform(get("/edge/new")).andExpect(status().isOk()).andExpect(model().attribute("edge", sameBeanAs(expectedEdge))).andExpect(model()
                .attribute("edgeModel", sameBeanAs(sh))).andExpect(model().attribute("routeList", sameBeanAs(vertices))).andExpect(view().name("edgeadd"));
    }

    @Test
    public void verifyThatSaveEdgeViewAndModelIsCorrect() throws Exception {
        //Set
        Edge expectedEdge = new Edge(2, "2", "A", "C", 1.89f);
        long max = 1;
        when(repositoryManagerService.edgeExists(expectedEdge)).thenReturn(false);
        when(repositoryManagerService.getEdgeMaxRecordId()).thenReturn(max);
        when(repositoryManagerService.saveEdge(expectedEdge)).thenReturn(expectedEdge);

        //Test
        mockMvc.perform(post("/edge").param("recordId", "" + max).param("distance", "1.0").param("sourceVertex", "A").param("destinationVertex", "C"))
                .andExpect(status().isOk()).andExpect(view().name("redirect:/edge/" + expectedEdge.getRecordId()));
    }

    @Test
    public void verifyThatSaveSameEdgeViewAndModelIsCorrect() throws Exception {
        //Set
        long max = 1;
        when(repositoryManagerService.getEdgeMaxRecordId()).thenReturn(max);
        String message = "You cannot link a route to itself.";
        //Verify
        mockMvc.perform(post("/edge").param("recordId", "" + max).param("distance", "1.0").param("sourceVertex", "A").param("destinationVertex", "A"))
                .andExpect(status().isOk()).andExpect(model().attribute("validationMessage", sameBeanAs(message))).andExpect(view().name("validation"));
    }

    @Test
    public void verifyThatSaveExistingEdgeViewAndModelIsCorrect() throws Exception {
        //Set
        Edge expectedEdge = new Edge(2, "2", "A", "C", 1.89f);
        Vertex source = new Vertex("A", "Earth");
        long recordId = 1;
        when(repositoryManagerService.getEdgeMaxRecordId()).thenReturn(recordId);
        when(repositoryManagerService.edgeExists(any(Edge.class))).thenReturn(true);
        when(repositoryManagerService.getVertexById("A")).thenReturn(source);
        String message = "The route from Earth (A) to (C) exists already.";
        //Verify
        mockMvc.perform(post("/edge").param("recordId", "" + recordId).param("edgeId", "2").param("sourceVertex", "A").param("destinationVertex", "C").param("distance", "1.89"))
                .andExpect(status().isOk()).andExpect(model().attribute("edge", sameBeanAs(expectedEdge))).andExpect(model().attribute("validationMessage", sameBeanAs(message)))
                .andExpect(view().name("validation"));
    }

    @Test
    public void verifyThatEditEdgeViewAndModelIsCorrect() throws Exception {
        //Set
        Edge expectedEdge = new Edge(1, "1", "A", "B", 0.44f);
        ShortestPathModel sh = new ShortestPathModel();
        when(repositoryManagerService.getAllVertices()).thenReturn(vertices);
        when(repositoryManagerService.getEdgeById(expectedEdge.getRecordId())).thenReturn(expectedEdge);
        sh.setSourceVertex(expectedEdge.getSource());
        sh.setDestinationVertex(expectedEdge.getDestination());
        //Verify
        mockMvc.perform(get("/edge/edit/" + expectedEdge.getRecordId()))
                .andExpect(status().isOk()).andExpect(model().attribute("edge", sameBeanAs(expectedEdge))).andExpect(model().attribute("edgeModel", sameBeanAs(sh)))
                .andExpect(model().attribute("routeList", sameBeanAs(vertices))).andExpect(view().name("edgeupdate"));
    }

    @Test
    public void verifyThatUpdateEdgeViewAndModelIsCorrect() throws Exception {
        //Set
        Edge expectedEdge = new Edge(2, "2", "A", "B", 1.89f);
        long recordId = 2;
        when(repositoryManagerService.edgeExists(expectedEdge)).thenReturn(false);
        when(repositoryManagerService.updateEdge(expectedEdge)).thenReturn(expectedEdge);

        //Test
        mockMvc.perform(post("/edgeupdate").param("recordId", "" + recordId).param("edgeId", "2").param("sourceVertex", "A").param("destinationVertex", "B").param("distance", "1.89"))
                .andExpect(status().isOk()).andExpect(model().attribute("edge", sameBeanAs(expectedEdge))).andExpect(view().name("redirect:/edge/" + expectedEdge.getRecordId()));
    }

    @Test
    public void verifyThatUpdateSameEdgeViewAndModelIsCorrect() throws Exception {
        //Set
        long recordId = 1;
        String message = "You cannot link a route to itself.";
        //Verify
        mockMvc.perform(post("/edgeupdate").param("recordId", "" + recordId).param("edgeId", "2").param("sourceVertex", "A").param("destinationVertex", "A").param("distance", "1.89"))
                .andExpect(status().isOk()).andExpect(model().attribute("validationMessage", sameBeanAs(message))).andExpect(view().name("validation"));
    }

    @Test
    public void verifyThatUpdateExistingEdgeViewAndModelIsCorrect() throws Exception {
        //Set
        Edge expectedEdge = new Edge(2, "2", "A", "B", 1.89f);
        Vertex vertex = new Vertex("A", "Moon");
        long recordId = 2;
        when(repositoryManagerService.edgeExists(any(Edge.class))).thenReturn(true);
        when(repositoryManagerService.getVertexById("A")).thenReturn(vertex);
        String message = "The route from Moon (A) to (B) exists already.";
        //Verify
        mockMvc.perform(post("/edgeupdate").param("recordId", "" + recordId).param("edgeId", "2").param("sourceVertex", "A").param("destinationVertex", "B").param("distance", "1.89"))
                .andExpect(status().isOk()).andExpect(model().attribute("edge", sameBeanAs(expectedEdge))).andExpect(model().attribute("validationMessage", sameBeanAs(message)))
                .andExpect(view().name("validation"));
    }

}
