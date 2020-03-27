package za.co.discovery.assignment.planet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import za.co.discovery.assignment.planet.entity.Vertex;
import za.co.discovery.assignment.planet.helper.Graph;
import za.co.discovery.assignment.planet.service.RepositoryManagerService;
import za.co.discovery.assignment.planet.service.ShortestPathService;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class ShortestPathRepository {

    private static final String PATH_NOT_AVAILABLE = "There is no path to ";
    private static final String PATH_NOT_NEEDED = "Not needed. You are already on planet ";
    private static final String NO_PLANET_FOUND = "No planet found.";
    private static final String PLANET_DOES_NOT_EXIST = " does not exist in the Interstellar Transport System.";
    protected PlatformTransactionManager platformTransactionManager;
    private Graph graph;
    private RepositoryManagerService repositoryManagerService;

    @Autowired
    public ShortestPathRepository(@Qualifier("transactionManager") PlatformTransactionManager platformTransactionManager, RepositoryManagerService repositoryManagerService) {
        this.platformTransactionManager = platformTransactionManager;
        this.repositoryManagerService = repositoryManagerService;
    }

    @PostConstruct
    public void initData() {

        TransactionTemplate template = new TransactionTemplate(platformTransactionManager);
        template.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                repositoryManagerService.persistGraph();
            }
        });
    }

    public String getShortestPath(String name) {
        StringBuilder path = new StringBuilder();
        graph = repositoryManagerService.selectGraph();
        ShortestPathService shortestPathService = new ShortestPathService(graph);

        if (graph == null || graph.getVertexes() == null || graph.getVertexes().isEmpty()) {
            return NO_PLANET_FOUND;
        }
        Vertex source = graph.getVertexes().get(0);
        Vertex destination = repositoryManagerService.getVertexByName(name);
        if (destination == null) {
            destination = repositoryManagerService.getVertexById(name);
            if (destination == null) {
                return name + PLANET_DOES_NOT_EXIST;
            }
        } else if (source != null && destination != null && source.getVertexId().equals(destination.getVertexId())) {
            return PATH_NOT_NEEDED + source.getName() + ".";
        }

        shortestPathService.run(source);
        LinkedList<Vertex> paths = shortestPathService.getPath(destination);
        if (paths != null) {
            for (Vertex vertex : paths) {
                path.append(vertex.getName() + " (" + vertex.getVertexId() + ")");
                path.append("\t");
            }
        } else {
            path.append(PATH_NOT_AVAILABLE + destination.getName());
            path.append(".");
        }

        return path.toString();
    }
}
