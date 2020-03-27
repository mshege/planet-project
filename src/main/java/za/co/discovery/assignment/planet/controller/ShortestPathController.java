package za.co.discovery.assignment.planet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import za.co.discovery.assignment.planet.service.RepositoryManagerService;
import za.co.discovery.assignment.planet.service.ShortestPathService;
import za.co.discovery.assignment.planet.entity.Vertex;
import za.co.discovery.assignment.planet.helper.Graph;
import za.co.discovery.assignment.planet.model.ShortestPathModel;

import java.util.LinkedList;
import java.util.List;

@RestController
public class ShortestPathController {

    private static final String PATH_NOT_AVAILABLE = "Unavailable.";
    private static final String PATH_NOT_NEEDED = "Not needed. You are already on planet ";
    private static final String NO_PLANET_FOUND = "No planet found.";
    private RepositoryManagerService repositoryManagerService;
    private ShortestPathService shortestPathService;

    @Autowired
    public ShortestPathController(RepositoryManagerService repositoryManagerService, ShortestPathService shortestPathService) {
        this.repositoryManagerService = repositoryManagerService;
        this.shortestPathService = shortestPathService;
    }

    @RequestMapping(value = "/shortest", method = RequestMethod.GET)
    public String shortestForm(Model model) {
        ShortestPathModel pathModel = new ShortestPathModel();
        List<Vertex> allVertices = repositoryManagerService.getAllVertices();

        if (allVertices == null || allVertices.isEmpty()) {
            model.addAttribute("validationMessage", NO_PLANET_FOUND);
            return "validation";
        }

        Vertex origin = allVertices.get(0);
        pathModel.setVertexName(origin.getName());
        model.addAttribute("shortest", pathModel);
        model.addAttribute("pathList", allVertices);
        return "shortest";
    }

    @RequestMapping(value = "/shortest", method = RequestMethod.POST)
    public String shortestSubmit(@ModelAttribute ShortestPathModel pathModel, Model model) {

        StringBuilder path = new StringBuilder();
        Graph graph = repositoryManagerService.selectGraph();
        if (pathModel.isTrafficAllowed()) {
            graph.setTrafficAllowed(true);
        }

        if (pathModel.isUndirectedGraph()) {
            graph.setUndirectedGraph(true);
        }

        shortestPathService.initializePlanets(graph);
        Vertex source = repositoryManagerService.getVertexByName(pathModel.getVertexName());
        Vertex destination = repositoryManagerService.getVertexById(pathModel.getSelectedVertex());

        shortestPathService.run(source);
        LinkedList<Vertex> paths = shortestPathService.getPath(destination);

        if (paths != null) {
            for (Vertex vertex : paths) {
                path.append(vertex.getName() + " (" + vertex.getVertexId() + ")");
                path.append("\t");
            }
        } else if (source != null && destination != null && source.getVertexId().equals(destination.getVertexId())) {
            path.append(PATH_NOT_NEEDED + source.getName());
        } else {
            path.append(PATH_NOT_AVAILABLE);
        }

        pathModel.setThePath(path.toString());
        pathModel.setSelectedVertexName(destination.getName());
        model.addAttribute("shortest", pathModel);
        return "result";
    }
}
