package com.example.user.core.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query(value = "{_id: ?0}")
    User findByuserId(String userId);

    @Query(value = "{username: ?0}")
    User findByusername(String username);


    @Query(value = "{email: ?0}")
    User findByEmail(String email);

    @Query(value = "{role:  ?0, username: ?1}")
    User findByRoleAndAndUsername(String role, String username);

    @Query(value = "{role: ?0}")
    User findByRole(String role);

}
