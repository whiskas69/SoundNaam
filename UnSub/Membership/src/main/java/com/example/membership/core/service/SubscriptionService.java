package com.example.membership.core.service;


import com.example.membership.core.data.UserSubscriptionEntity;
import com.example.membership.core.data.UserSubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {
    private UserSubscriptionRepository repository;

    public SubscriptionService(UserSubscriptionRepository repository) {
        this.repository = repository;
    }

    public List<UserSubscriptionEntity> getUserSubscription(String email){
        return repository.findByEmail(email);
    }

    public List<UserSubscriptionEntity> getArtistSubscription(String artistname){
        return repository.findByArtistname(artistname);
    }

    public UserSubscriptionEntity getByUserEmailAndArtistName(String email, String name){
        return repository.findUseremailAndArtistname(email, name);
    }

    public List<UserSubscriptionEntity> getAllsubscription(){
        return repository.findAll();
    }


    public String unsubscribeFromArtist(UserSubscriptionEntity entity) {
        try{
            repository.deleteById(entity.get_id());
            return "Successfully unsubscribe from " + entity.getArtistname();
         }catch(Exception e){
            return e.getLocalizedMessage();
         }
    }

    public void updateSubscription(UserSubscriptionEntity entity){
        repository.save(entity);
    }
}
