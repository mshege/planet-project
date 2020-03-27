package za.co.discovery.assignment.planet.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;
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

public class TrafficsControllerTest {
    @Mock
    View mockView;
    @InjectMocks
    private TrafficsController controller;
    @Mock
    private RepositoryManagerService repositoryManagerService;
    private List<Traffic> traffics;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
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
    public void verifyThatListTrafficsViewAndModelIsCorrect() throws Exception {
        //Set
        when(repositoryManagerService.getAllTraffics()).thenReturn(traffics);
        //Verify
        mockMvc.perform(get("/traffics")).andExpect(model().attribute("traffics", sameBeanAs(traffics))).andExpect(view().name("traffics"));
    }

    @Test
    public void verifyThatShowTrafficViewAndModelIsCorrect() throws Exception {
        //Set
        Traffic expectedTraffic = new Traffic("1", "A", "B", 0.30f);
        when(repositoryManagerService.getTrafficById("1")).thenReturn(expectedTraffic);
        //Verify
        mockMvc.perform(get("/traffic/1"))
                .andExpect(status().isOk()).andExpect(model().attribute("traffic", sameBeanAs(expectedTraffic))).andExpect(view().name("trafficshow"));
    }

    @Test
    public void verifyThatDeleteTrafficViewIsCorrect() throws Exception {
        //Set
        when(repositoryManagerService.deleteTraffic("1")).thenReturn(true);
        //Verify
        mockMvc.perform(post("/traffic/delete/1")).andExpect(status().isOk()).andExpect(view().name("redirect:/traffics"));
    }

    @Test
    public void verifyThatAddTrafficViewAndModelIsCorrect() throws Exception {
        //Set
        Traffic expectedTraffic = new Traffic();
        ShortestPathModel sh = new ShortestPathModel();
        //Verify
        mockMvc.perform(get("/traffic/new"))
                .andExpect(status().isOk()).andExpect(model().attribute("traffic", sameBeanAs(expectedTraffic))).andExpect(model().attribute("trafficModel", sameBeanAs(sh)))
                .andExpect(view().name("trafficadd"));
    }

    @Test
    public void verifyThatSaveTrafficViewAndModelIsCorrect() throws Exception {
        //Set
        Traffic expectedTraffic = new Traffic("2", "A", "B", 1.0f);
        long max = 1;
        when(repositoryManagerService.trafficExists(expectedTraffic)).thenReturn(false);
        when(repositoryManagerService.getTrafficMaxRecordId()).thenReturn(max);
        when(repositoryManagerService.saveTraffic(expectedTraffic)).thenReturn(expectedTraffic);

        //Test
        mockMvc.perform(post("/traffic").param("routeId", "1").param("delay", "1.0").param("sourceVertex", "A").param("destinationVertex", "B"))
                .andExpect(status().isOk()).andExpect(view().name("redirect:/traffic/" + expectedTraffic.getRouteId()));
    }

    @Test
    public void verifyThatSaveSameTrafficViewAndModelIsCorrect() throws Exception {
        //Set
        long max = 1;
        when(repositoryManagerService.getTrafficMaxRecordId()).thenReturn(max);
        String message = "You cannot add traffic on the same route origin and destination.";
        //Verify
        mockMvc.perform(post("/traffic").param("routeId", "1").param("delay", "1.0").param("sourceVertex", "A").param("destinationVertex", "A"))
                .andExpect(status().isOk()).andExpect(model().attribute("validationMessage", sameBeanAs(message))).andExpect(view().name("validation"));
    }

    @Test
    public void verifyThatSaveExistingTrafficViewAndModelIsCorrect() throws Exception {
        //Set
        Traffic expectedTraffic = new Traffic("2", "A", "B", 2.0f);
        Vertex source = new Vertex("A", "Earth");
        long recordId = 1;
        when(repositoryManagerService.getTrafficMaxRecordId()).thenReturn(recordId);
        when(repositoryManagerService.trafficExists(any(Traffic.class))).thenReturn(true);
        when(repositoryManagerService.getVertexById("A")).thenReturn(source);
        String message = "The traffic from Earth (A) to  (B) exists already.";
        //Verify
        mockMvc.perform(post("/traffic").param("routeId", "1").param("delay", "2.0").param("sourceVertex", "A").param("destinationVertex", "B"))
                .andExpect(status().isOk()).andExpect(model().attribute("traffic", sameBeanAs(expectedTraffic))).andExpect(model().attribute("validationMessage", sameBeanAs(message)))
                .andExpect(view().name("validation"));
    }

    @Test
    public void verifyThatEditTrafficViewAndModelIsCorrect() throws Exception {
        //Set
        Traffic expectedTraffic = new Traffic("2", "A", "B", 2.0f);
        ShortestPathModel sh = new ShortestPathModel();
        when(repositoryManagerService.getTrafficById(expectedTraffic.getRouteId())).thenReturn(expectedTraffic);
        sh.setSourceVertex(expectedTraffic.getSource());
        sh.setDestinationVertex(expectedTraffic.getDestination());
        //Verify
        mockMvc.perform(get("/traffic/edit/" + expectedTraffic.getRouteId())).andExpect(status().isOk()).andExpect(model().attribute("traffic", sameBeanAs(expectedTraffic)))
                .andExpect(model().attribute("trafficModel", sameBeanAs(sh))).andExpect(view().name("trafficupdate"));
    }

    @Test
    public void verifyThatUpdateTrafficViewAndModelIsCorrect() throws Exception {
        //Set
        Traffic expectedTraffic = new Traffic("2", "A", "B", 1.0f);
        when(repositoryManagerService.trafficExists(expectedTraffic)).thenReturn(false);
        when(repositoryManagerService.updateTraffic(expectedTraffic)).thenReturn(expectedTraffic);

        //Verify
        mockMvc.perform(post("/trafficupdate").param("routeId", "2").param("source", "A").param("destination", "C").param("sourceVertex", "A").param("destinationVertex", "B").param("delay", "1.0"))
                .andExpect(status().isOk()).andExpect(model().attribute("traffic", sameBeanAs(expectedTraffic))).andExpect(view().name("redirect:/traffic/" + expectedTraffic.getRouteId()));
    }

    @Test
    public void verifyThatUpdateSameTrafficViewAndModelIsCorrect() throws Exception {
        //Set
        String message = "You cannot add traffic on the same route origin and destination.";
        //Verify
        mockMvc.perform(post("/trafficupdate").param("routeId", "2").param("source", "A").param("destination", "C").param("sourceVertex", "A").param("destinationVertex", "A").param("delay", "1.0"))
                .andExpect(status().isOk()).andExpect(model().attribute("validationMessage", sameBeanAs(message))).andExpect(view().name("validation"));
    }

    @Test
    public void verifyThatUpdateExistingTrafficViewAndModelIsCorrect() throws Exception {
        //Set
        Traffic expectedTraffic = new Traffic("2", "A", "B", 1.0f);
        Vertex vertex = new Vertex("A", "Moon");
        when(repositoryManagerService.trafficExists(any(Traffic.class))).thenReturn(true);
        when(repositoryManagerService.getVertexById("A")).thenReturn(vertex);
        String message = "The traffic from Moon (A) to  (B) exists already.";
        //Verify
        mockMvc.perform(post("/trafficupdate").param("routeId", "2").param("source", "A").param("destination", "C").param("sourceVertex", "A").param("destinationVertex", "B").param("delay", "1.0"))
                .andExpect(status().isOk()).andExpect(model().attribute("traffic", sameBeanAs(expectedTraffic)))
                .andExpect(model().attribute("validationMessage", sameBeanAs(message))).andExpect(view().name("validation"));
    }

}
