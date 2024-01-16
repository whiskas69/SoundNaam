package com.example.membership.core.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends MongoRepository<UserSubscriptionEntity, String> {

    @Query(value = "{useremail:  '?0'}")
    List<UserSubscriptionEntity> findByEmail(String email);

    @Query(value = "{artistname:  '?0'}")
    List<UserSubscriptionEntity> findByArtistname(String artistname);

    @Query("{useremail : '?0', artistname : '?1'}")
    UserSubscriptionEntity findUseremailAndArtistname(String email, String artistname);


//    @Query(value = "{ 'userId' : ?0, 'channelId' : ?1 }")
//    UserSubscriptionEntity findByUserIdAndChannelId(String userId, String channelId);



}
