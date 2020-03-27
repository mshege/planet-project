package za.co.discovery.assignment.planet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.discovery.assignment.planet.dao.EdgeDao;
import za.co.discovery.assignment.planet.dao.TrafficDao;
import za.co.discovery.assignment.planet.dao.VertexDao;
import za.co.discovery.assignment.planet.entity.Edge;
import za.co.discovery.assignment.planet.entity.Traffic;
import za.co.discovery.assignment.planet.entity.Vertex;
import za.co.discovery.assignment.planet.helper.Graph;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Service
public class RepositoryManagerService {

    private static final String EXCEL_FILENAME = "/interstellar.xlsx";
    private VertexDao vertexDao;
    private EdgeDao edgeDao;
    private TrafficDao trafficDao;

    @Autowired
    public RepositoryManagerService(VertexDao vertexDao, EdgeDao edgeDao, TrafficDao trafficDao) {
        this.vertexDao = vertexDao;
        this.edgeDao = edgeDao;
        this.trafficDao = trafficDao;
    }

    public void persistGraph() {
        URL resource = getClass().getResource(EXCEL_FILENAME);
        File file1;
        try {
            file1 = new File(resource.toURI());
            persistGraph(file1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void persistGraph(File file) {
        XLSXHandler handler = new XLSXHandler(file);

        List<Vertex> vertices = handler.readVertexes();
        if (vertices != null && !vertices.isEmpty()) {
            for (Vertex vertex : vertices) {
                vertexDao.save(vertex);
            }
        }
        List<Edge> edges = handler.readEdges();
        if (edges != null && !edges.isEmpty()) {
            for (Edge edge : edges) {
                edgeDao.save(edge);
            }
        }
        List<Traffic> traffic = handler.readTraffics();
        if (edges != null && !edges.isEmpty()) {
            for (Traffic trf : traffic) {
                trafficDao.save(trf);
            }
        }
    }

    public Graph selectGraph() {
        List<Vertex> vertices = vertexDao.selectAll();
        List<Edge> edges = edgeDao.selectAll();
        List<Traffic> traffics = trafficDao.selectAll();

        Graph graph = new Graph(vertices, edges, traffics);
        return graph;
    }

    public Vertex saveVertex(Vertex vertex) {
        vertexDao.save(vertex);
        return vertex;
    }

    public Vertex updateVertex(Vertex vertex) {
        vertexDao.update(vertex);
        return vertex;
    }

    public boolean deleteVertex(String vertexId) {
        vertexDao.delete(vertexId);
        return true;
    }

    public List<Vertex> getAllVertices() {
        return vertexDao.selectAll();
    }

    public Vertex getVertexByName(String name) {
        return vertexDao.selectUniqueByName(name);
    }

    public Vertex getVertexById(String vertexId) {
        return vertexDao.selectUnique(vertexId);
    }

    public boolean vertexExist(String vertexId) {
        Vertex vertex = vertexDao.selectUnique(vertexId);
        return vertex != null;
    }

    public Edge saveEdge(Edge edge) {
        edgeDao.save(edge);
        return edge;
    }

    public Edge updateEdge(Edge edge) {
        edgeDao.update(edge);
        return edge;
    }

    public boolean deleteEdge(long recordId) {
        edgeDao.delete(recordId);
        return true;
    }

    public List<Edge> getAllEdges() {
        return edgeDao.selectAll();
    }

    public Edge getEdgeById(long recordId) {
        return edgeDao.selectUnique(recordId);
    }

    public long getEdgeMaxRecordId() {
        return edgeDao.selectMaxRecordId();
    }

    public boolean edgeExists(Edge edge) {
        List<Edge> edges = edgeDao.edgeExists(edge);
        return !edges.isEmpty();
    }

    public Traffic saveTraffic(Traffic traffic) {
        trafficDao.save(traffic);
        return traffic;
    }

    public Traffic updateTraffic(Traffic traffic) {
        trafficDao.update(traffic);
        return traffic;
    }

    public boolean deleteTraffic(String routeId) {
        trafficDao.delete(routeId);
        return true;
    }

    public List<Traffic> getAllTraffics() {
        return trafficDao.selectAll();
    }

    public Traffic getTrafficById(String routeId) {
        return trafficDao.selectUnique(routeId);
    }

    public long getTrafficMaxRecordId() {
        return trafficDao.selectMaxRecordId();
    }

    public boolean trafficExists(Traffic traffic) {
        List<Traffic> traffics = trafficDao.trafficExists(traffic);
        return !traffics.isEmpty();
    }
}