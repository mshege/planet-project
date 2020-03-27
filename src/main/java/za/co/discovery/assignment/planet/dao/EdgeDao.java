package za.co.discovery.assignment.planet.dao;

import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.planet.entity.Edge;

import java.util.List;

@Repository
@Transactional
public class EdgeDao {

    private SessionFactory sessionFactory;

    @Autowired
    public EdgeDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Edge edge) {
        Session session = sessionFactory.getCurrentSession();
        session.save(edge);
    }

    public void update(Edge edge) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(edge);
    }

    public int delete(long recordId) {
        Session session = sessionFactory.getCurrentSession();
        String qry = "DELETE FROM edge AS E WHERE E.recordId = :recordIdParameter";
        Query query = session.createQuery(qry);
        query.setParameter("recordIdParameter", recordId);
        return query.executeUpdate();
    }

    public Edge selectUnique(long recordId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Edge.class);
        criteria.add(Restrictions.eq("recordId", recordId));
        return (Edge) criteria.uniqueResult();
    }

    public long selectMaxRecordId() {
        long maxId = (Long) sessionFactory.getCurrentSession()
                .createCriteria(Edge.class)
                .setProjection(Projections.max("recordId")).uniqueResult();
        return maxId;
    }

    public List<Edge> edgeExists(Edge edge) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Edge.class);
        criteria.add(Restrictions.ne("recordId", edge.getRecordId()));
        criteria.add(Restrictions.eq("source", edge.getSource()));
        criteria.add(Restrictions.eq("destination", edge.getDestination()));
        List<Edge> edges = (List<Edge>) criteria.list();
        return edges;
    }

    public List<Edge> selectAllByRecordId(long recordId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Edge.class);
        criteria.add(Restrictions.eq("recordId", recordId));
        List<Edge> edges = (List<Edge>) criteria.list();
        return edges;
    }

    public List<Edge> selectAllByEdgeId(String edgeId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Edge.class);
        criteria.add(Restrictions.eq("edgeId", edgeId));
        List<Edge> edges = (List<Edge>) criteria.list();
        return edges;
    }

    public List<Edge> selectAll() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Edge.class);
        List<Edge> edges = (List<Edge>) criteria.list();
        return edges;
    }

}
