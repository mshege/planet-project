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
//import za.co.discovery.assignment.planet.entity.Traffic;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.shazam.shazamcrest.MatcherAssert.assertThat;
//import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
//
//@Transactional
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {Traffic.class, TrafficDao.class, DatasourceBean.class, PersistenceBean.class},
//        loader = AnnotationConfigContextLoader.class)
//public class TrafficDaoTest {
//
//    @Autowired
//    private SessionFactory sessionFactory;
//    private TrafficDao trafficDao;
//
//    @Before
//    public void setUp() throws Exception {
//        trafficDao = new TrafficDao(sessionFactory);
//    }
//
//    @Test
//    public void verifyThatSaveTrafficIsCorrect() throws Exception {
//        //Set
//        Session session = sessionFactory.getCurrentSession();
//        Traffic traffic = new Traffic("1", "A", "B", 4f);
//        List<Traffic> expectedTraffics = new ArrayList<>();
//        expectedTraffics.add(traffic);
//
//        //Test
//        trafficDao.save(traffic);
//        Criteria criteria = session.createCriteria(Traffic.class);
//        List<Traffic> persistedTraffics = (List<Traffic>) criteria.list();
//
//        //Verify
//        assertThat(persistedTraffics, sameBeanAs(expectedTraffics));
//        //Rollback for testing purpose
//        session.getTransaction().rollback();
//    }
//
//    @Test
//    public void verifyThatUpdateTrafficIsCorrect() throws Exception {
//        //Set
//        Session session = sessionFactory.getCurrentSession();
//        Traffic traffic = new Traffic("1", "A", "B", 4f);
//        session.save(traffic);
//
//        Traffic trafficToUpdate = new Traffic("1", "F", "M", 4f);
//
//        List<Traffic> expectedTraffic = new ArrayList<>();
//        expectedTraffic.add(trafficToUpdate);
//
//        //Test
//        trafficDao.update(trafficToUpdate);
//        Criteria criteria = session.createCriteria(Traffic.class);
//        List<Traffic> persistedTraffics = (List<Traffic>) criteria.list();
//
//        // Verify
//        assertThat(persistedTraffics, sameBeanAs(expectedTraffic));
//
//        //Rollback for testing purpose
//        session.getTransaction().rollback();
//    }
//
//    @Test
//    public void verifyThatDeleteTrafficIsCorrect() throws Exception {
//        //Set
//        Session session = sessionFactory.getCurrentSession();
//        Traffic traffic1 = new Traffic("1", "A", "B", 4f);
//        Traffic traffic2 = new Traffic("2", "G", "V", 2f);
//        List<Traffic> expectedTraffics = new ArrayList<>();
//        expectedTraffics.add(traffic1);
//        session.save(traffic1);
//        session.save(traffic2);
//
//        //Test
//        trafficDao.delete(traffic2.getRouteId());
//        Criteria criteria = session.createCriteria(Traffic.class);
//        List<Traffic> persistedTraffics = (List<Traffic>) criteria.list();
//
//        // Verify
//        assertThat(persistedTraffics, sameBeanAs(expectedTraffics));
//
//        //Rollback for testing purpose
//        session.getTransaction().rollback();
//    }
//
//    @Test
//    public void verifyThatSelectUniqueTrafficIsCorrect() throws Exception {
//        //Set
//        Session session = sessionFactory.getCurrentSession();
//        Traffic traffic = new Traffic("100", "A", "B", 4f);
//        Traffic expected = new Traffic("5", "M", "C", 4f);
//        session.save(traffic);
//        session.save(expected);
//
//        //Test
//        Traffic persisted = trafficDao.selectUnique(expected.getRouteId());
//
//        //Verify
//        assertThat(persisted, sameBeanAs(expected));
//        //Rollback for testing purpose
//        session.getTransaction().rollback();
//    }
//
//    @Test
//    public void verifyThatSelecteAllTrafficsIsCorrect() throws Exception {
//        //Set
//        Session session = sessionFactory.getCurrentSession();
//        Traffic traffic1 = new Traffic("1", "A", "B", 4f);
//        Traffic traffic2 = new Traffic("2", "C", "D", 4f);
//        Traffic traffic3 = new Traffic("3", "D", "F", 4f);
//        Traffic traffic4 = new Traffic("4", "B", "F", 4f);
//        session.save(traffic1);
//        session.save(traffic2);
//        session.save(traffic3);
//        session.save(traffic4);
//        List<Traffic> expectedTraffics = new ArrayList<>();
//        expectedTraffics.add(traffic1);
//        expectedTraffics.add(traffic2);
//        expectedTraffics.add(traffic3);
//        expectedTraffics.add(traffic4);
//
//        //Test
//        List<Traffic> persistedTraffics = trafficDao.selectAll();
//
//        //Verify
//        assertThat(persistedTraffics, sameBeanAs(expectedTraffics));
//        //Rollback for testing purpose
//        session.getTransaction().rollback();
//    }
//
//    @Test
//    public void verifyThatSelectEdgeMaxRecordIsCorrect() {
//        //Set
//        Session session = sessionFactory.getCurrentSession();
//        Traffic traffic1 = new Traffic("1", "A", "B", 4f);
//        Traffic traffic2 = new Traffic("2", "C", "D", 4f);
//        session.save(traffic1);
//        session.save(traffic2);
//        long expectedMax = 2;
//
//        //Test
//        long returnMax = trafficDao.selectMaxRecordId();
//
//        //Verify
//        assertThat(returnMax, sameBeanAs(expectedMax));
//        //Rollback for testing purpose
//        session.getTransaction().rollback();
//    }
//
//    @Test
//    public void verifyThatEdgeExistsSelectionIsCorrect() {
//        //Set
//        Session session = sessionFactory.getCurrentSession();
//        Traffic traffic1 = new Traffic("1", "A", "B", 4f);
//        Traffic traffic2 = new Traffic("2", "C", "D", 4f);
//        session.save(traffic1);
//        session.save(traffic2);
//
//        Traffic trafficToCommit = new Traffic("3", "A", "B", 1.9f);
//        List<Traffic> expectedTraffics = new ArrayList<>();
//        expectedTraffics.add(traffic1);
//
//        //Test
//        List<Traffic> returnedEdges = trafficDao.trafficExists(trafficToCommit);
//        //Verify
//        assertThat(returnedEdges, sameBeanAs(expectedTraffics));
//        //Rollback for testing purpose
//        session.getTransaction().rollback();
//    }
//
//}