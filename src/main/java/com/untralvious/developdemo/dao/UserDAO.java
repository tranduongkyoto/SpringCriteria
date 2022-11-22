package com.untralvious.developdemo.dao;

import com.untralvious.developdemo.domain.User;
import com.untralvious.developdemo.repository.UserRepository;
import com.untralvious.developdemo.util.SearchCriteria;
import com.untralvious.developdemo.util.SearchOperation;
import com.untralvious.developdemo.util.SpecSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAO implements IUserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private UserRepository userRepository;

    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        Predicate emailPredicate = builder.equal(r.get("email"), email);
        Predicate namePredicate = builder.equal(r.get("firstname"), firstname);
        query.where(emailPredicate, namePredicate);
        //query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<User> searchUserWithSpecification(String email, String firstname) {
        UserSpecification emailSpec = new UserSpecification(new SpecSearchCriteria( null,"email", SearchOperation.EQUALITY, email ));
        UserSpecification firstnameSpec = new UserSpecification(new SpecSearchCriteria( null,"firstname", SearchOperation.EQUALITY, firstname ));
        return userRepository.findAll(Specification.where(emailSpec).and(firstnameSpec));
    }

    public List<User> searchUserByEmailAndFirstname2(String email, String firstname) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<User> query = builder.createQuery(User.class);
        final Root r = query.from(User.class);
        query.select(r);
        query.select(r.get("fistname"));
        query.select(r.get("email"));
        Predicate emailPredicate = builder.equal(r.get("email"), email);
        Predicate emailNotNullPredicate = r.get("email").isNotNull();
        Predicate namePredicate = builder.like(r.get("firstname"), firstname);
        Predicate nameInGroupPredicate = r.get("firstname").in("John", "Mark", "David");
        Predicate lastNamePredicate = builder.like(r.get("lastname"), "%joe%");
        Predicate ageGreaterThanPredicate = builder.gt(r.get("age"), 21);
        Predicate ageLessThanPredicate = builder.lt(r.get("age"), 30);
        Predicate ageBetweenPredicate = builder.between(r.get("age"), 25, 35);


        //query.where(builder.and(emailPredicate, ageGreaterThanPredicate));
        query.where(builder.or(emailPredicate, namePredicate));

        query.orderBy(builder.desc(r.get("age")));

        query.groupBy(r.get("age"));

        return entityManager.createQuery(query).getResultList();
    }

}
