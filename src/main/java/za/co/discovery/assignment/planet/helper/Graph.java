package za.co.discovery.assignment.planet.helper;

import lombok.Data;
import za.co.discovery.assignment.planet.entity.Edge;
import za.co.discovery.assignment.planet.entity.Traffic;
import za.co.discovery.assignment.planet.entity.Vertex;

import java.util.ArrayList;
import java.util.List;

@Data
public class Graph {

    private List<Vertex> vertexes;
    private List<Edge> edges;
    private List<Traffic> traffics;
    private boolean undirectedGraph;
    private boolean trafficAllowed;

    public Graph(List<Vertex> vertexes, List<Edge> edges, List<Traffic> traffics) {
        this.vertexes = vertexes;
        this.edges = edges;
        this.traffics = traffics;
    }

    public void processTraffics() {
        if (traffics != null && !traffics.isEmpty()) {
            for (Traffic traffic : traffics) {
                for (Edge edge : edges) {
                    if (checkObjectsEqual(edge.getEdgeId(), traffic.getRouteId())) {
                        if (checkObjectsEqual(edge.getSource(), traffic.getSource()) && checkObjectsEqual(edge.getDestination(), traffic.getDestination())) {
                            edge.setTimeDelay(traffic.getDelay());
                        }
                    }
                }
            }
        }
    }

    public List<Edge> getUndirectedEdges() {
        List<Edge> undirectedEdges = new ArrayList();
        for (Edge fromEdge : edges) {
            Edge toEdge = copyAdjacentEdge(fromEdge);
            undirectedEdges.add(fromEdge);
            undirectedEdges.add(toEdge);
        }
        return undirectedEdges;
    }

    public Edge copyAdjacentEdge(Edge fromEdge) {
        Edge toEdge = new Edge();
        toEdge.setEdgeId(fromEdge.getEdgeId());
        toEdge.setSource(fromEdge.getDestination());
        toEdge.setDestination(fromEdge.getSource());
        toEdge.setDistance(fromEdge.getDistance());
        toEdge.setTimeDelay(fromEdge.getTimeDelay());
        return toEdge;
    }

    public boolean checkObjectsEqual(Object object, Object otherObject) {
        if (object == null && otherObject == null) {
            return true;
        } else if (object == null || otherObject == null) {
            return false;
        } else if (object instanceof String && otherObject instanceof String) {
            return ((String) object).equalsIgnoreCase((String) otherObject);
        } else {
            return object.equals(otherObject);
        }
    }
}