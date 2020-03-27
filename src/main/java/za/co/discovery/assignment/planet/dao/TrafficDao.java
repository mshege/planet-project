package za.co.discovery.assignment.planet.dao;

import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.planet.entity.Traffic;

import java.util.List;

@Repository
@Transactional
public class TrafficDao {

    private SessionFactory sessionFactory;

    @Autowired
    public TrafficDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Traffic traffic) {
        Session session = sessionFactory.getCurrentSession();
        session.save(traffic);
    }

    public void update(Traffic traffic) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(traffic);
    }

    public int delete(String routeId) {
        Session session = sessionFactory.getCurrentSession();
        String qry = "DELETE FROM traffic AS T WHERE T.routeId = :routeIdParameter";
        Query query = session.createQuery(qry);
        query.setParameter("routeIdParameter", routeId);
        return query.executeUpdate();
    }

    public Traffic selectUnique(String routeId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Traffic.class);
        criteria.add(Restrictions.eq("routeId", routeId));
        return (Traffic) criteria.uniqueResult();
    }

    public List<Traffic> selectAll() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Traffic.class);
        List<Traffic> edges = (List<Traffic>) criteria.list();
        return edges;
    }

    public long selectMaxRecordId() {
        long maxId = (Long) sessionFactory.getCurrentSession()
                .createCriteria(Traffic.class)
                .setProjection(Projections.rowCount()).uniqueResult();
        return maxId;
    }

    public List<Traffic> trafficExists(Traffic traffic) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Traffic.class);
        criteria.add(Restrictions.ne("routeId", traffic.getRouteId()));
        criteria.add(Restrictions.eq("source", traffic.getSource()));
        criteria.add(Restrictions.eq("destination", traffic.getDestination()));
        List<Traffic> traffics = (List<Traffic>) criteria.list();
        return traffics;
    }
}