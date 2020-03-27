package za.co.discovery.assignment.planet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import za.co.discovery.assignment.planet.service.RepositoryManagerService;
import za.co.discovery.assignment.planet.entity.Traffic;
import za.co.discovery.assignment.planet.helper.ValidationCodes;
import za.co.discovery.assignment.planet.model.ShortestPathModel;

import java.util.List;

@Controller
public class TrafficsController {

    private static final String DUPLICATE_TRAFFIC = "You cannot add traffic on the same route origin and destination.";
    private static final String INVALID_CODE = "Failed to find the validation code. Please start again.";
    private RepositoryManagerService repositoryManagerService;

    @Autowired
    public TrafficsController(RepositoryManagerService repositoryManagerService) {
        this.repositoryManagerService = repositoryManagerService;
    }

    @RequestMapping(value = "/traffics", method = RequestMethod.GET)
    public String listTraffics(Model model) {
        List<Traffic> allTraffics = repositoryManagerService.getAllTraffics();
        model.addAttribute("traffics", allTraffics);
        return "traffics";
    }

    @RequestMapping("traffic/{routeId}")
    public String showTraffic(@PathVariable String routeId, Model model) {
        model.addAttribute("traffic", repositoryManagerService.getTrafficById(routeId));
        return "trafficshow";
    }

    @RequestMapping("traffic/delete/{routeId}")
    public String deleteTraffic(@PathVariable String routeId) {
        repositoryManagerService.deleteTraffic(routeId);
        return "redirect:/traffics";
    }

    @RequestMapping(value = "traffic/new", method = RequestMethod.GET)
    public String addTraffic(Model model) {
        ShortestPathModel sh = new ShortestPathModel();
        List allVertices = repositoryManagerService.getAllVertices();
        model.addAttribute("traffic", new Traffic());
        model.addAttribute("trafficModel", sh);
        model.addAttribute("trafficList", allVertices);
        return "trafficadd";
    }

    @RequestMapping(value = "traffic", method = RequestMethod.POST)
    public String saveTraffic(Traffic traffic, @ModelAttribute ShortestPathModel pathModel, Model model) {
        int id = (int) repositoryManagerService.getTrafficMaxRecordId() + 1;
        traffic.setRouteId(String.valueOf(id));
        traffic.setSource(pathModel.getSourceVertex());
        traffic.setDestination(pathModel.getDestinationVertex());

        if (pathModel.getSourceVertex().equals(pathModel.getDestinationVertex())) {
            buildTrafficValidation(pathModel, model, ValidationCodes.TRAFFIC_TO_SELF.toString());
            return "validation";
        }

        if (repositoryManagerService.trafficExists(traffic)) {
            buildTrafficValidation(pathModel, model, ValidationCodes.TRAFFIC_EXISTS.toString());
            return "validation";
        }
        repositoryManagerService.saveTraffic(traffic);
        return "redirect:/traffic/" + traffic.getRouteId();
    }

    @RequestMapping(value = "traffic/edit/{routeId}", method = RequestMethod.GET)
    public String editTraffic(@PathVariable String routeId, Model model) {
        ShortestPathModel pathModel = new ShortestPathModel();
        List allVertices = repositoryManagerService.getAllVertices();
        Traffic trafficToEdit = repositoryManagerService.getTrafficById(routeId);
        pathModel.setSourceVertex(trafficToEdit.getSource());
        pathModel.setDestinationVertex(trafficToEdit.getDestination());
        model.addAttribute("traffic", trafficToEdit);
        model.addAttribute("trafficModel", pathModel);
        model.addAttribute("trafficList", allVertices);
        return "trafficupdate";
    }

    @RequestMapping(value = "trafficupdate", method = RequestMethod.POST)
    public String updateTraffic(Traffic traffic, @ModelAttribute ShortestPathModel pathModel, Model model) {
        traffic.setSource(pathModel.getSourceVertex());
        traffic.setDestination(pathModel.getDestinationVertex());

        if (pathModel.getSourceVertex().equals(pathModel.getDestinationVertex())) {
            buildTrafficValidation(pathModel, model, ValidationCodes.TRAFFIC_TO_SELF.toString());
            return "validation";
        }

        if (repositoryManagerService.trafficExists(traffic)) {
            buildTrafficValidation(pathModel, model, ValidationCodes.TRAFFIC_EXISTS.toString());
            return "validation";
        }
        repositoryManagerService.updateTraffic(traffic);
        return "redirect:/traffic/" + traffic.getRouteId();
    }

    public void buildTrafficValidation(@ModelAttribute ShortestPathModel pathModel, Model model, String code) {
        String message = "";
        ValidationCodes mode = ValidationCodes.fromString(code);
        if (mode != null) {
            switch (mode) {
                case TRAFFIC_EXISTS:
                    String sourceName = repositoryManagerService.getVertexById(pathModel.getSourceVertex()) == null ? "" : repositoryManagerService.getVertexById(pathModel.getSourceVertex()).getName();
                    String sourceDestination = repositoryManagerService.getVertexById(pathModel.getDestinationVertex()) == null ? "" : repositoryManagerService.getVertexById(pathModel.getDestinationVertex()).getName();
                    message = "The traffic from " + sourceName + " (" + pathModel.getSourceVertex() + ") to " + sourceDestination + " (" + pathModel.getDestinationVertex() + ") exists already.";
                    break;
                case TRAFFIC_TO_SELF:
                    message = DUPLICATE_TRAFFIC;
                    break;
                default:
                    message = INVALID_CODE;
                    break;
            }
        }
        model.addAttribute("validationMessage", message);
    }
}
