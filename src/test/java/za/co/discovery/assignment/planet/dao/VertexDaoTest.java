// This class should be executed individually
//package za.co.discovery.assignment.planet.dao;
//
//import org.hibernate.Criteria;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//import org.springframework.transaction.annotation.Transactional;
//import za.co.discovery.assignment.planet.config.DatasourceBean;
//import za.co.discovery.assignment.planet.config.PersistenceBean;
//import za.co.discovery.assignment.planet.entity.Vertex;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.shazam.shazamcrest.MatcherAssert.assertThat;
//import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
//import static org.junit.Assert.assertEquals;
//
//@Transactional
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {Vertex.class, VertexDao.class, DatasourceBean.class, PersistenceBean.class},
//        loader = AnnotationConfigContextLoader.class)
//public class VertexDaoTest {
//    @Autowired
//    private SessionFactory sessionFactory;
//    private VertexDao vertexDao;
//
//    @Before
//    public void setUp() throws Exception {
//        vertexDao = new VertexDao(sessionFactory);
//    }
//
//    @Test
//    public void verifyThatSaveVertexIsCorrect() throws Exception {
//        //Set
//        Session session = sessionFactory.getCurrentSession();
//        Vertex vertex = new Vertex("A", "Earth");
//        List<Vertex> expectedVertexes = new ArrayList<>();
//        expectedVertexes.add(vertex);
//
//        //Test
//        vertexDao.save(vertex);
//        Criteria criteria = session.createCriteria(Vertex.class);
//        List<Vertex> persistedVertexes = (List<Vertex>) criteria.list();
//
//        //Verify
//        assertThat(persistedVertexes, sameBeanAs(expectedVertexes));
//        assertEquals("Earth", persistedVertexes.get(0).getName());
//        //Rollback for testing purpose
//        session.getTransaction().rollback();
//    }
//
//    @Test
//    public void verifyThatUpdateVertexIsCorrect() throws Exception {
//        //Set
//        Session session = sessionFactory.getCurrentSession();
//        Vertex vertex = new Vertex("A", "Earth");
//        session.save(vertex);
//
//        Vertex vertexToUpdate = new Vertex("A", "Jupiter");
//
//        List<Vertex> expectedVertexes = new ArrayList<>();
//        expectedVertexes.add(vertexToUpdate);
//
//        //Test
//        vertexDao.update(vertexToUpdate);
//        Criteria criteria = session.createCriteria(Vertex.class);
//        List<Vertex> persistedVertexes = (List<Vertex>) criteria.list();
//
//        // Verify
//        assertThat(persistedVertexes, sameBeanAs(expectedVertexes));
//
//        //Rollback for testing purpose
//        session.getTransaction().rollback();
//    }
//
//    @Test
//    public void verifyThatDeleteVertexIsCorrect() throws Exception {
//        //Set
//        Session session = sessionFactory.getCurrentSession();
//        Vertex v1 = new Vertex("A", "Mars");
//        Vertex v2 = new Vertex("C", "Terre");
//        List<Vertex> expectedVertexes = new ArrayList<>();
//        expectedVertexes.add(v1);
//        session.save(v1);
//        session.save(v2);
//
//        //Test
//        vertexDao.delete(v2.getVertexId());
//        Criteria criteria = session.createCriteria(Vertex.class);
//        List<Vertex> persistedVertexes = (List<Vertex>) criteria.list();
//
//        // Verify
//        assertThat(persistedVertexes, sameBeanAs(expectedVertexes));
//        //Rollback for testing purpose
//        session.getTransaction().rollback();
//    }
//
//    @Test
//    public void verifyThatSelectUniqueVertexIsCorrect() throws Exception {
//        //Set
//        Session session = sessionFactory.getCurrentSession();
//        Vertex v1 = new Vertex("A", "Mars");
//        Vertex expected = new Vertex("C", "Terre");
//        session.save(v1);
//        session.save(expected);
//
//        //Test
//        Vertex actualVertex = vertexDao.selectUnique(expected.getVertexId());
//
//        //Verify
//        assertThat(actualVertex, sameBeanAs(expected));
//        //Rollback for testing purpose
//        session.getTransaction().rollback();
//    }
//
//    @Test
//    public void verifyThatSelectUniqueByNameVertexIsCorrect() throws Exception {
//        //Set
//        Session session = sessionFactory.getCurrentSession();
//        Vertex vertex1 = new Vertex("A", "Earth");
//        Vertex vertex2 = new Vertex("B", "Mars");
//        Vertex expected = new Vertex("C", "Moon");
//        session.save(vertex1);
//        session.save(vertex2);
//        session.save(expected);
//
//        //Test
//        Vertex persistedVertex = vertexDao.selectUniqueByName(expected.getName());
//
//        //Verify
//        assertThat(persistedVertex, sameBeanAs(expected));
//        assertThat(persistedVertex.getName(), sameBeanAs("Moon"));
//        //Rollback for testing purpose
//        session.getTransaction().rollback();
//    }
//
//    @Test
//    public void verifyThatSelectAllVertexIsCorrect() throws Exception {
//        //Set
//        Session session = sessionFactory.getCurrentSession();
//        Vertex v1 = new Vertex("A", "Jupiter");
//        Vertex v2 = new Vertex("F", "Pluto");
//        session.save(v1);
//        session.save(v2);
//        List<Vertex> expectedVertexes = new ArrayList<>();
//        expectedVertexes.add(v1);
//        expectedVertexes.add(v2);
//
//        //Test
//        List<Vertex> persistedVertexes = vertexDao.selectAll();
//
//        //Verify
//        assertThat(persistedVertexes, sameBeanAs(expectedVertexes));
//        //Rollback for testing purpose
//        session.getTransaction().rollback();
//    }
//
//}