package com.untralvious.developdemo.dao;

import com.untralvious.developdemo.domain.User;
import com.untralvious.developdemo.util.SearchCriteria;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAO implements IUserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> searchUser(final List<SearchCriteria> params) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<User> query = builder.createQuery(User.class);
        final Root r = query.from(User.class);

        Predicate predicate = builder.conjunction();
        UserSearchQueryCriteriaConsumer searchConsumer = new UserSearchQueryCriteriaConsumer(predicate, builder, r);
        params.stream().forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.where(predicate);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void save(final User entity) {
        entityManager.persist(entity);
    }

    @Override
    public List<User> searchUserByEmailAndFirstname(String email, String firstname) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<User> query = builder.createQuery(User.class);
        final Root r = query.from(User.class);
        List<Predicate> predicates = new ArrayList<>();


        if (email != null) {
            predicates.add(builder.equal(r.get("email"), email));
        }
        if (firstname != null) {
            predicates.add(builder.like(r.get("firstname"), "%" + firstname + "%"));
        }
        query.where(predicates.toArray(new Predicate[0]));

//        Predicate emailPredicate = builder.equal(r.get("email"), email);
//        Predicate namePredicate = builder.equal(r.get("firstname"), firstname);
//        query.where(emailPredicate, namePredicate);

//        query.select(r.get("age"));
//
//        query.where(r.get("age").isNull());
//
//        query.orderBy(builder.desc(r.get("age")));
//
//        query.groupBy(r.get("age"));
        return entityManager.createQuery(query).getResultList();
    }

}
