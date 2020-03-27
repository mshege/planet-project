package za.co.discovery.assignment.planet.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;
import za.co.discovery.assignment.planet.entity.Edge;
import za.co.discovery.assignment.planet.entity.Traffic;
import za.co.discovery.assignment.planet.entity.Vertex;
import za.co.discovery.assignment.planet.service.RepositoryManagerService;

import java.util.ArrayList;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class PlanetsControllerTest {
    @Mock
    View mockView;
    @InjectMocks
    private PlanetsController controller;
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
    public void verifyThatListVerticesViewAndModelIsCorrect() throws Exception {
        //Set
        when(repositoryManagerService.getAllVertices()).thenReturn(vertices);
        //Verify
        mockMvc.perform(get("/vertices")).andExpect(model().attribute("vertices", sameBeanAs(vertices))).andExpect(view().name("vertices"));
    }

    @Test
    public void verifyThatShowVertexViewAndModelIsCorrect() throws Exception {
        //Set
        Vertex expectedVertex = new Vertex("A", "Earth");
        when(repositoryManagerService.getVertexById("vertexId")).thenReturn(expectedVertex);
        //Verify
        mockMvc.perform(get("/vertex/vertexId")).andExpect(status().isOk()).andExpect(model().attribute("vertex", sameBeanAs(expectedVertex))).andExpect(view().name("vertexshow"));
    }

    @Test
    public void verifyThatAddVertexViewAndModelIsCorrect() throws Exception {
        //Set
        Vertex expectedVertex = new Vertex();
        //Verify
        mockMvc.perform(get("/vertex/new")).andExpect(status().isOk()).andExpect(model().attribute("vertex", sameBeanAs(expectedVertex))).andExpect(view().name("vertexadd"));
    }

    @Test
    public void verifyThatSaveVertexViewIsCorrect() throws Exception {
        //Set
        Vertex expectedVertex = new Vertex("A", "Earth");
        when(repositoryManagerService.vertexExist("A")).thenReturn(false);
        when(repositoryManagerService.saveVertex(expectedVertex)).thenReturn(expectedVertex);

        //Test
        mockMvc.perform(post("/vertex").param("vertexId", "A").param("name", "Earth"))
                .andExpect(status().isOk()).andExpect(view().name("redirect:/vertex/" + expectedVertex.getVertexId()));

        //Verify
        ArgumentCaptor<Vertex> formObjectArgument = ArgumentCaptor.forClass(Vertex.class);
        verify(repositoryManagerService, times(1)).saveVertex(formObjectArgument.capture());

        Vertex formObject = formObjectArgument.getValue();
        assertThat(formObjectArgument.getValue(), is(sameBeanAs(expectedVertex)));

        assertThat(formObject.getVertexId(), is("A"));
        assertThat(formObject.getName(), is("Earth"));
    }

    @Test
    public void verifyThatSaveExistingVertexViewAndModelIsCorrect() throws Exception {
        //Set
        Vertex expectedVertex = new Vertex("A", "Earth");
        when(repositoryManagerService.vertexExist("A")).thenReturn(true);
        when(repositoryManagerService.getVertexById("A")).thenReturn(expectedVertex);
        String message = "Planet A already exists as Earth";
        //Verify
        mockMvc.perform(post("/vertex").param("vertexId", "A").param("name", "Earth"))
                .andExpect(status().isOk()).andExpect(model().attribute("validationMessage", sameBeanAs(message))).andExpect(view().name("validation"));
    }

    @Test
    public void verifyThatEditVertexViewAndModelIsCorrect() throws Exception {
        //Set
        Vertex expectedVertex = new Vertex("A", "Earth");
        when(repositoryManagerService.getVertexById("vertexId")).thenReturn(expectedVertex);
        //Verify
        mockMvc.perform(get("/vertex/edit/vertexId")).andExpect(status().isOk()).andExpect(model().attribute("vertex", sameBeanAs(expectedVertex))).andExpect(view().name("vertexupdate"));
    }

    @Test
    public void verifyThatUpdateVertexViewIsCorrect() throws Exception {
        //Set
        Vertex expectedVertex = new Vertex("A", "Earth");
        when(repositoryManagerService.updateVertex(expectedVertex)).thenReturn(expectedVertex);
        //Verify
        mockMvc.perform(post("/vertexupdate").param("vertexId", "A").param("name", "Earth"))
                .andExpect(status().isOk()).andExpect(view().name("redirect:/vertex/" + expectedVertex.getVertexId()));
    }

    @Test
    public void verifyThatDeleteVertexViewIsCorrect() throws Exception {
        //Set
        when(repositoryManagerService.deleteVertex("vertexId")).thenReturn(true);
        //Verify
        mockMvc.perform(post("/vertex/delete/A")).andExpect(status().isOk()).andExpect(view().name("redirect:/vertices"));
    }

    @Test
    public void verifyThatShowEdgeViewAndModelIsCorrect() throws Exception {
        //Set
        Edge expectedEdge = new Edge(2, "2", "A", "C", 1.89f);
        long recordId = 2;
        when(repositoryManagerService.getEdgeById(recordId)).thenReturn(expectedEdge);
    }

}
