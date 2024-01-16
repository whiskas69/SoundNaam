package com.example.membership.query;

import com.example.membership.core.data.UserSubscriptionEntity;
import com.example.membership.core.data.UserSubscriptionRepository;
import com.example.membership.core.events.UserSubscribedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserSubscriptionEventHandler {

    private final UserSubscriptionRepository userSubscriptionRepository;

    public UserSubscriptionEventHandler(UserSubscriptionRepository userSubscriptionRepository){
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    @EventHandler
    public void on(UserSubscribedEvent event){
        UserSubscriptionEntity userSubscriptionEntity = new UserSubscriptionEntity();
        BeanUtils.copyProperties(event, userSubscriptionEntity);
        userSubscriptionRepository.save(userSubscriptionEntity);
    }

}
