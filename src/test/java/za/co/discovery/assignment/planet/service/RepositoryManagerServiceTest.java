package za.co.discovery.assignment.planet.service;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.planet.config.DatasourceBean;
import za.co.discovery.assignment.planet.config.PersistenceBean;
import za.co.discovery.assignment.planet.dao.EdgeDao;
import za.co.discovery.assignment.planet.dao.TrafficDao;
import za.co.discovery.assignment.planet.dao.VertexDao;
import za.co.discovery.assignment.planet.entity.Edge;
import za.co.discovery.assignment.planet.entity.Traffic;
import za.co.discovery.assignment.planet.entity.Vertex;
import za.co.discovery.assignment.planet.helper.Graph;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TrafficDao.class, EdgeDao.class, VertexDao.class, DatasourceBean.class, PersistenceBean.class},
        loader = AnnotationConfigContextLoader.class)
public class RepositoryManagerServiceTest {
    private static final String EXCEL_FILENAME = "/test.xlsx";
    @Autowired
    private SessionFactory sessionFactory;
    private EdgeDao edgeDao;
    private VertexDao vertexDao;
    private TrafficDao trafficDao;
    private RepositoryManagerService repositoryManagerService;
    private File file;

    @Before
    public void setUp() throws Exception {
        URL resource = getClass().getResource(EXCEL_FILENAME);
        file = new File(resource.toURI());
        edgeDao = new EdgeDao(sessionFactory);
        trafficDao = new TrafficDao(sessionFactory);
        vertexDao = new VertexDao(sessionFactory);
        repositoryManagerService = new RepositoryManagerService(vertexDao, edgeDao, trafficDao);
    }

    @Test
    public void verifyThatGraphPersistIsCorrect() throws Exception {
        Session session = sessionFactory.getCurrentSession();
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

        repositoryManagerService.persistGraph(file);
        Graph graph = repositoryManagerService.selectGraph();

        List<Edge> readEdges = graph.getEdges();
        List<Vertex> readVertices = graph.getVertexes();
        List<Traffic> readTraffics = graph.getTraffics();

        assertThat(expectedVertexes, sameBeanAs(readVertices));
        assertThat(expectedEdges, sameBeanAs(readEdges));
        assertThat(expectedTraffics, sameBeanAs(readTraffics));

        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatSaveVertexIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Vertex vertex = new Vertex("A", "Earth");
        List<Vertex> expectedVertexes = new ArrayList<>();
        expectedVertexes.add(vertex);
        //Test
        Vertex returned = repositoryManagerService.saveVertex(vertex);
        Criteria criteria = session.createCriteria(Vertex.class);
        List<Vertex> persistedVertexes = (List<Vertex>) criteria.list();

        //Verify
        assertThat(vertex, sameBeanAs(returned));
        assertThat(persistedVertexes, sameBeanAs(expectedVertexes));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatUpdateVertexIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Vertex vertex = new Vertex("A", "Earth");

        Vertex vertexToUpdate = new Vertex("A", "Jupiter");
        List<Vertex> expectedVertexes = new ArrayList<>();
        expectedVertexes.add(vertexToUpdate);

        Vertex persistedVertex = repositoryManagerService.updateVertex(vertexToUpdate);

        List<Vertex> persistedVertexes = new ArrayList<>();
        persistedVertexes.add(persistedVertex);

        assertThat(expectedVertexes, sameBeanAs(persistedVertexes));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatDeleteVertexIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Vertex v1 = new Vertex("A", "Mars");
        Vertex v2 = new Vertex("C", "Terre");
        List<Vertex> expectedVertexes = new ArrayList<>();
        expectedVertexes.add(v1);
        session.save(v1);
        session.save(v2);
        boolean expected = true;

        //Test
        boolean returned = repositoryManagerService.deleteVertex(v2.getVertexId());

        // Verify
        assertThat(expected, sameBeanAs(returned));

        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatGetUniqueByNameVertexIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Vertex vertex1 = new Vertex("A", "Earth");
        Vertex vertex2 = new Vertex("B", "Mars");
        Vertex expected = new Vertex("C", "Moon");
        session.save(vertex1);
        session.save(vertex2);
        session.save(expected);

        //Test
        Vertex persistedVertex = repositoryManagerService.getVertexByName(expected.getName());

        //Verify
        assertThat(persistedVertex, sameBeanAs(expected));
        assertThat(persistedVertex.getName(), sameBeanAs("Moon"));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatGetUniqueByIdVertexIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Vertex vertex1 = new Vertex("A", "Earth");
        Vertex expected = new Vertex("C", "Moon");
        session.save(vertex1);
        session.save(expected);

        //Test
        Vertex persistedVertex = repositoryManagerService.getVertexById(expected.getVertexId());

        //Verify
        assertThat(persistedVertex, sameBeanAs(expected));
        assertThat(persistedVertex.getVertexId(), sameBeanAs("C"));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatGetAllVertexIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Vertex v1 = new Vertex("A", "Jupiter");
        Vertex v2 = new Vertex("F", "Pluto");
        session.save(v1);
        session.save(v2);
        List<Vertex> expectedVertexes = new ArrayList<>();
        expectedVertexes.add(v1);
        expectedVertexes.add(v2);

        //Test
        List<Vertex> persistedVertexes = repositoryManagerService.getAllVertices();

        //Verify
        assertThat(persistedVertexes, sameBeanAs(expectedVertexes));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatVertexExistsIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Vertex vertex1 = new Vertex("A", "Earth");
        session.save(vertex1);

        boolean expected = true;

        //Test
        boolean returned = repositoryManagerService.vertexExist(vertex1.getVertexId());

        //Verify
        assertThat(returned, sameBeanAs(expected));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    //Edges

    @Test
    public void verifyThatSaveEdgeIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Edge edge = new Edge(1, "2", "A", "B", 2f);
        List<Edge> expectedEdges = new ArrayList<>();
        expectedEdges.add(edge);
        //Test
        Edge returned = repositoryManagerService.saveEdge(edge);
        Criteria criteria = session.createCriteria(Edge.class);
        List<Edge> persistedEdges = (List<Edge>) criteria.list();

        //Verify
        assertThat(edge, sameBeanAs(returned));
        assertThat(persistedEdges, sameBeanAs(expectedEdges));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatUpdateEdgeIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Edge edge = new Edge(1, "2", "A", "B", 2f);
        session.save(edge);

        Edge edgeToUpdate = new Edge(1, "2", "A", "C", 5.5f);
        List<Edge> expectedEdges = new ArrayList<>();
        expectedEdges.add(edgeToUpdate);

        Edge persistedEdge = repositoryManagerService.updateEdge(edgeToUpdate);

        List<Edge> persistedEdges = new ArrayList<>();
        persistedEdges.add(persistedEdge);

        assertThat(expectedEdges, sameBeanAs(persistedEdges));
        assertThat(persistedEdge.getDestination(), sameBeanAs("C"));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatDeleteEdgeIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Edge edge1 = new Edge(1, "1", "A", "B", 2f);
        Edge edge2 = new Edge(2, "2", "A", "C", 1.89f);
        List<Edge> expectedEdges = new ArrayList<>();
        expectedEdges.add(edge1);
        session.save(edge1);
        session.save(edge2);
        boolean expected = true;

        //Test
        boolean returned = repositoryManagerService.deleteEdge(edge2.getRecordId());

        // Verify
        assertThat(expected, sameBeanAs(returned));

        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatGetEdgeByIdIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Edge edge1 = new Edge(1, "1", "A", "B", 2f);
        Edge expected = new Edge(2, "2", "A", "C", 1.89f);
        session.save(edge1);
        session.save(expected);

        //Test
        Edge persistedEdge = repositoryManagerService.getEdgeById(expected.getRecordId());

        //Verify
        assertThat(persistedEdge, sameBeanAs(expected));
        assertThat(persistedEdge.getDestination(), sameBeanAs("C"));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatGetAllEdgesIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Edge edge1 = new Edge(1, "1", "A", "B", 2f);
        Edge edge2 = new Edge(2, "2", "A", "C", 1.89f);
        session.save(edge1);
        session.save(edge2);
        List<Edge> expectedEdges = new ArrayList<>();
        expectedEdges.add(edge1);
        expectedEdges.add(edge2);

        //Test
        List<Edge> persistedEdges = repositoryManagerService.getAllEdges();

        //Verify
        assertThat(persistedEdges, sameBeanAs(expectedEdges));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatEdgeExistsIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Edge edge1 = new Edge(1, "1", "A", "B", 2f);
        Edge edge2 = new Edge(2, "2", "A", "C", 0.9f);
        session.save(edge1);
        session.save(edge2);

        Edge edgeToCommit = new Edge(3, "3", "A", "C", 1.83f);

        boolean expected = true;

        //Test
        boolean returned = repositoryManagerService.edgeExists(edgeToCommit);

        //Verify
        assertThat(returned, sameBeanAs(expected));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatGetEdgeMaxRecordIdIsCorrect() {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Edge e1 = new Edge(1, "30", "A", "B", 0.17f);
        Edge e2 = new Edge(2, "19", "B", "C", 0.19f);
        session.save(e1);
        session.save(e2);
        long expectedMax = 2;

        //Test
        long returnMax = repositoryManagerService.getEdgeMaxRecordId();

        //Verify
        assertThat(returnMax, sameBeanAs(expectedMax));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    //Traffcs

    @Test
    public void verifyThatSaveTrafficIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Traffic traffic = new Traffic("1", "A", "B", 4f);
        List<Traffic> expectedTraffics = new ArrayList<>();
        expectedTraffics.add(traffic);
        //Test
        Traffic returned = repositoryManagerService.saveTraffic(traffic);
        Criteria criteria = session.createCriteria(Traffic.class);
        List<Traffic> persistedTraffics = (List<Traffic>) criteria.list();

        //Verify
        assertThat(traffic, sameBeanAs(returned));
        assertThat(persistedTraffics, sameBeanAs(expectedTraffics));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatUpdateTrafficIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Traffic traffic = new Traffic("1", "A", "B", 4f);
        session.save(traffic);

        Traffic trafficToUpdate = new Traffic("1", "A", "Z", 4f);
        List<Traffic> expectedTraffics = new ArrayList<>();
        expectedTraffics.add(trafficToUpdate);

        Traffic persistedTraffic = repositoryManagerService.updateTraffic(trafficToUpdate);

        List<Traffic> persistedTraffics = new ArrayList<>();
        persistedTraffics.add(persistedTraffic);

        assertThat(expectedTraffics, sameBeanAs(persistedTraffics));
        assertThat(persistedTraffic.getDestination(), sameBeanAs("Z"));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatDeleteTrafficIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Traffic traffic1 = new Traffic("1", "A", "B", 4f);
        Traffic traffic2 = new Traffic("2", "G", "J", 1.9f);
        List<Traffic> expectedTraffics = new ArrayList<>();
        expectedTraffics.add(traffic1);
        session.save(traffic1);
        session.save(traffic2);
        boolean expected = true;

        //Test
        boolean returned = repositoryManagerService.deleteTraffic(traffic2.getRouteId());

        // Verify
        assertThat(expected, sameBeanAs(returned));

        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatGetTrafficByIdIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Traffic traffic1 = new Traffic("1", "A", "B", 4f);
        Traffic expected = new Traffic("2", "F", "K", 1.89f);
        session.save(traffic1);
        session.save(expected);

        //Test
        Traffic persistedTraffic = repositoryManagerService.getTrafficById(expected.getRouteId());

        //Verify
        assertThat(persistedTraffic, sameBeanAs(expected));
        assertThat(persistedTraffic.getDelay(), sameBeanAs(1.89f));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatGetAllTrafficsIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Traffic traffic1 = new Traffic("1", "A", "B", 4f);
        Traffic traffic2 = new Traffic("2", "G", "J", 1.9f);
        session.save(traffic1);
        session.save(traffic2);
        List<Traffic> expectedTraffics = new ArrayList<>();
        expectedTraffics.add(traffic1);
        expectedTraffics.add(traffic2);

        //Test
        List<Traffic> persistedTraffics = repositoryManagerService.getAllTraffics();

        //Verify
        assertThat(persistedTraffics, sameBeanAs(expectedTraffics));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatTrafficExistsIsCorrect() throws Exception {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Traffic traffic1 = new Traffic("1", "A", "B", 4f);
        Traffic traffic2 = new Traffic("2", "G", "J", 5.9f);
        session.save(traffic1);
        session.save(traffic2);

        Traffic trafficToCommit = new Traffic("5", "A", "J", 0.1f);

        boolean expected = false;

        //Test
        boolean returned = repositoryManagerService.trafficExists(trafficToCommit);

        //Verify
        assertThat(returned, sameBeanAs(expected));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void verifyThatGetTrafficMaxRecordIdIsCorrect() {
        //Set
        Session session = sessionFactory.getCurrentSession();
        Traffic traffic1 = new Traffic("1", "A", "C", 1.3f);
        Traffic traffic2 = new Traffic("2", "F", "M", 2.9f);
        session.save(traffic1);
        session.save(traffic2);
        long expectedMax = 2;

        //Test
        long returnMax = repositoryManagerService.getTrafficMaxRecordId();

        //Verify
        assertThat(returnMax, sameBeanAs(expectedMax));
        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

}
