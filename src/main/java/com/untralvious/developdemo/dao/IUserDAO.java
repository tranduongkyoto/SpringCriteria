package com.untralvious.developdemo.dao;

import com.untralvious.developdemo.domain.User;
import com.untralvious.developdemo.util.SearchCriteria;

import java.util.List;

public interface IUserDAO {
    List<User> searchUser(List<SearchCriteria> params);

    void save(User entity);

    List<User> searchUserByEmailAndFirstname(String email, String firstname);
}
