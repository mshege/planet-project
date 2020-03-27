package za.co.discovery.assignment.planet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import za.co.discovery.assignment.planet.entity.Vertex;
import za.co.discovery.assignment.planet.service.RepositoryManagerService;

import java.util.List;

@Controller
public class PlanetsController {

    private RepositoryManagerService repositoryManagerService;

    @Autowired
    public PlanetsController(RepositoryManagerService repositoryManagerService) {
        this.repositoryManagerService = repositoryManagerService;
    }

    @RequestMapping(value = "/vertices", method = RequestMethod.GET)
    public String listVertices(Model model) {
        List allVertices = repositoryManagerService.getAllVertices();
        model.addAttribute("vertices", allVertices);
        return "vertices";
    }

    @RequestMapping("vertex/{vertexId}")
    public String showVertex(@PathVariable String vertexId, Model model) {
        model.addAttribute("vertex", repositoryManagerService.getVertexById(vertexId));
        return "vertexshow";
    }

    @RequestMapping("vertex/new")
    public String addVertex(Model model) {
        model.addAttribute("vertex", new Vertex());
        return "vertexadd";
    }

    @RequestMapping(value = "vertex", method = RequestMethod.POST)
    public String saveVertex(Vertex vertex, Model model) {
        if (repositoryManagerService.vertexExist(vertex.getVertexId())) {
            buildVertexValidation(vertex.getVertexId(), model);
            return "validation";
        }
        repositoryManagerService.saveVertex(vertex);
        return "redirect:/vertex/" + vertex.getVertexId();
    }

    @RequestMapping("vertex/edit/{vertexId}")
    public String editVertex(@PathVariable String vertexId, Model model) {
        model.addAttribute("vertex", repositoryManagerService.getVertexById(vertexId));
        return "vertexupdate";
    }

    @RequestMapping(value = "vertexupdate", method = RequestMethod.POST)
    public String updateVertex(Vertex vertex) {
        repositoryManagerService.updateVertex(vertex);
        return "redirect:/vertex/" + vertex.getVertexId();
    }

    @RequestMapping("vertex/delete/{vertexId}")
    public String deleteVertex(@PathVariable String vertexId) {
        repositoryManagerService.deleteVertex(vertexId);
        return "redirect:/vertices";
    }

    public void buildVertexValidation(String vertexId, Model model) {
        String vertexName = repositoryManagerService.getVertexById(vertexId) == null ? "" : repositoryManagerService.getVertexById(vertexId).getName();
        String message = "Planet " + vertexId + " already exists as " + vertexName;
        model.addAttribute("validationMessage", message);
    }
}
