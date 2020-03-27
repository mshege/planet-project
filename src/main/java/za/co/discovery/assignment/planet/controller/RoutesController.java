package za.co.discovery.assignment.planet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import za.co.discovery.assignment.planet.entity.Edge;
import za.co.discovery.assignment.planet.helper.ValidationCodes;
import za.co.discovery.assignment.planet.model.ShortestPathModel;
import za.co.discovery.assignment.planet.service.RepositoryManagerService;

import java.util.List;

@Controller
public class RoutesController {

    private static final String DUPLICATE_ROUTE = "You cannot link a route to itself.";
    private static final String INVALID_CODE = "Failed to find the validation code. Please start again.";

    private RepositoryManagerService repositoryManagerService;

    @Autowired
    public RoutesController(RepositoryManagerService repositoryManagerService) {
        this.repositoryManagerService = repositoryManagerService;
    }

    @RequestMapping(value = "/edges", method = RequestMethod.GET)
    public String listEdges(Model model) {
        List allEdges = repositoryManagerService.getAllEdges();
        model.addAttribute("edges", allEdges);
        return "edges";
    }

    @RequestMapping("edge/{recordId}")
    public String showEdge(@PathVariable long recordId, Model model) {
        model.addAttribute("edge", repositoryManagerService.getEdgeById(recordId));
        return "edgeshow";
    }

    @RequestMapping("edge/delete/{recordId}")
    public String deleteEdge(@PathVariable long recordId) {
        repositoryManagerService.deleteEdge(recordId);
        return "redirect:/edges";
    }

    @RequestMapping(value = "edge/new", method = RequestMethod.GET)
    public String addEdge(Model model) {
        ShortestPathModel sh = new ShortestPathModel();
        List allVertices = repositoryManagerService.getAllVertices();
        model.addAttribute("edge", new Edge());
        model.addAttribute("edgeModel", sh);
        model.addAttribute("routeList", allVertices);
        return "edgeadd";
    }

    @RequestMapping(value = "edge", method = RequestMethod.POST)
    public String saveEdge(Edge edge, @ModelAttribute ShortestPathModel pathModel, Model model) {
        int id = (int) repositoryManagerService.getEdgeMaxRecordId() + 1;
        edge.setRecordId(id);
        edge.setEdgeId(String.valueOf(id));
        edge.setSource(pathModel.getSourceVertex());
        edge.setDestination(pathModel.getDestinationVertex());

        if (pathModel.getSourceVertex().equals(pathModel.getDestinationVertex())) {
            buildEdgeValidation(pathModel, model, ValidationCodes.ROUTE_TO_SELF.toString());
            return "validation";
        }

        if (repositoryManagerService.edgeExists(edge)) {
            buildEdgeValidation(pathModel, model, ValidationCodes.ROUTE_EXISTS.toString());
            return "validation";
        }
        repositoryManagerService.saveEdge(edge);
        return "redirect:/edge/" + edge.getRecordId();
    }

    @RequestMapping(value = "edge/edit/{recordId}", method = RequestMethod.GET)
    public String editEdge(@PathVariable long recordId, Model model) {
        ShortestPathModel pathModel = new ShortestPathModel();
        List allVertices = repositoryManagerService.getAllVertices();
        Edge edgeToEdit = repositoryManagerService.getEdgeById(recordId);
        pathModel.setSourceVertex(edgeToEdit.getSource());
        pathModel.setDestinationVertex(edgeToEdit.getDestination());
        model.addAttribute("edge", edgeToEdit);
        model.addAttribute("edgeModel", pathModel);
        model.addAttribute("routeList", allVertices);
        return "edgeupdate";
    }

    @RequestMapping(value = "edgeupdate", method = RequestMethod.POST)
    public String updateEdge(Edge edge, @ModelAttribute ShortestPathModel pathModel, Model model) {
        edge.setSource(pathModel.getSourceVertex());
        edge.setDestination(pathModel.getDestinationVertex());
        if (pathModel.getSourceVertex().equals(pathModel.getDestinationVertex())) {
            buildEdgeValidation(pathModel, model, ValidationCodes.ROUTE_TO_SELF.toString());
            return "validation";
        }

        if (repositoryManagerService.edgeExists(edge)) {
            buildEdgeValidation(pathModel, model, ValidationCodes.ROUTE_EXISTS.toString());
            return "validation";
        }
        repositoryManagerService.updateEdge(edge);
        return "redirect:/edge/" + edge.getRecordId();
    }

    public void buildEdgeValidation(@ModelAttribute ShortestPathModel pathModel, Model model, String code) {
        String message = "";
        ValidationCodes mode = ValidationCodes.fromString(code);
        if (mode != null) {
            switch (mode) {
                case ROUTE_EXISTS:
                    String sourceName = repositoryManagerService.getVertexById(pathModel.getSourceVertex()) == null ? "" : repositoryManagerService.getVertexById(pathModel.getSourceVertex()).getName();
                    String sourceDestination = repositoryManagerService.getVertexById(pathModel.getDestinationVertex()) == null ? "" : repositoryManagerService.getVertexById(pathModel.getDestinationVertex()).getName();
                    message = "The route from " + sourceName + " (" + pathModel.getSourceVertex() + ") to " + sourceDestination + "(" + pathModel.getDestinationVertex() + ") exists already.";
                    break;
                case ROUTE_TO_SELF:
                    message = DUPLICATE_ROUTE;
                    break;
                default:
                    message = INVALID_CODE;
                    break;
            }
        }
        model.addAttribute("validationMessage", message);
    }
}
