package za.co.discovery.assignment.planet.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShortestPathModel implements Serializable {

    private String selectedVertex;
    private String selectedVertexName;
    private String vertexId;
    private String vertexName;
    private String thePath;
    private String sourceVertex;
    private String destinationVertex;
    private boolean undirectedGraph;
    private boolean trafficAllowed;
}
