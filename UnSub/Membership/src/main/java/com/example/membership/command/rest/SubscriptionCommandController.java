package com.example.membership.command.rest;


import com.example.membership.command.UserSubscriptionCommand;
import com.example.membership.core.service.SubscriptionService;
import com.example.membership.core.data.UserSubscriptionEntity;
import com.example.user.core.data.User;
import com.example.user.core.data.UserRepository;
import com.example.user.core.service.UserService;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/subscription")
public class SubscriptionCommandController {

    private final Environment env;

    private final CommandGateway commandGateway;

    private final UserService userService;

    private final UserRepository userRepository;

    private final SubscriptionService subscriptionService;

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionCommandController.class);

    private LocalDateTime startDate = LocalDateTime.now();
    private LocalDateTime endDate = LocalDateTime.now().plusMinutes(5);

    @Autowired
    public SubscriptionCommandController(Environment env, CommandGateway commandGateway, UserService userService, SubscriptionService subscriptionService, UserRepository userRepository) {
        this.env = env;
        this.commandGateway = commandGateway;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.userRepository = userRepository;
    }
    @PostMapping("/subscribe")
    public String subscribeArtist(@RequestParam Map<String, String> formData) {
        String useremail = formData.get("useremail");
        String artistname = formData.get("artistname");
        String code = formData.get("code");
        String results = "";

        System.out.println(useremail + " " + artistname + " " + code);
        UserSubscriptionEntity existingSubscription = subscriptionService.getByUserEmailAndArtistName(useremail, artistname);
        User entity = userService.getUserByEmail(useremail);



        if (existingSubscription != null && entity.getCredit() >= 35) {
            if (code == null) {
                entity.setCredit(entity.getCredit() - 35);
            }
            startDate = existingSubscription.getStartDate();
            endDate = existingSubscription.getEndDate().plusMinutes(5);
            existingSubscription.setStartDate(startDate);
            existingSubscription.setEndDate(endDate);
            subscriptionService.updateSubscription(existingSubscription);
            userRepository.save(entity);
            results = "Success update";

        } else if(existingSubscription == null && entity.getCredit() >= 35) {
            if(code == null){
                entity.setCredit(entity.getCredit() - 35);
                userRepository.save(entity);
            }
            UserSubscriptionCommand command = UserSubscriptionCommand.builder()
                    .subscriptionId(UUID.randomUUID().toString())
                    .useremail(useremail)
                    .artistname(artistname)
                    .subscribed(true)
                    .startDate(this.startDate)
                    .endDate(this.endDate)
                    .build();
            try {
                results = commandGateway.sendAndWait(command);
            } catch (Exception e){
                results = e.getLocalizedMessage();
            }
        } else {
            results = "You don't have enough credits";
        }
        User artist = userRepository.findByRoleAndAndUsername("Artist" ,artistname);
        List<UserSubscriptionEntity> subscriptionEntities  = subscriptionService.getArtistSubscription(artist.getUsername());
        System.out.println(subscriptionEntities.size());
        artist.setSubcount(subscriptionEntities.size());
        userRepository.save(artist);
        return results;
    }


    @PostMapping("/unsubscribe")
    public String unsubscribeArtist(@RequestParam Map<String, String> formData) {
        String useremail = formData.get("useremail");
        String artistname = formData.get("artistname");
        UserSubscriptionEntity entity = subscriptionService.getByUserEmailAndArtistName(useremail, artistname);
        entity.setSubscribed(false);
        if(LocalDateTime.now().equals(entity.getEndDate())){
            subscriptionService.unsubscribeFromArtist(entity);
        }
        return "You are not subscriber of " + artistname + "anymore";
    }

    @PostMapping("/redeemCode")
    public String redeemCode(@RequestParam Map<String, String> formData){
        subscribeArtist(formData);
        return "Send away";
    }

    @Scheduled(fixedRate = 10000) // run every minute
    public void autoSubscribeAndUnsubscribe() {
        LocalDateTime currentDate = LocalDateTime.now();
        List<UserSubscriptionEntity> subscriptions = subscriptionService.getAllsubscription();


        for (UserSubscriptionEntity subscription : subscriptions) {
            User artist = userService.getUserByRoleAndUsername("Artist", subscription.getArtistname());
            if (subscription.getEndDate().isBefore(currentDate)) {
                User autoSubuser = userService.getUserByEmail(subscription.getUseremail());
                if(autoSubuser.getCredit() >= 35){
                    LocalDateTime startDate = currentDate;
                    LocalDateTime endDate = currentDate.plusMinutes(5);
                    autoSubuser.setCredit(autoSubuser.getCredit() - 35);
                    subscription.setStartDate(startDate);
                    subscription.setEndDate(endDate);
                    subscriptionService.updateSubscription(subscription);
                    userRepository.save(autoSubuser);
                    logger.info("User: " + autoSubuser.getUsername() + " continue subscribing to Artist: " + artist.getUsername());
                } else {
                    subscription.setSubscribed(false);
                    subscriptionService.unsubscribeFromArtist(subscription);
                    List<UserSubscriptionEntity> subscriptionEntities = subscriptionService.getArtistSubscription(subscription.getArtistname());
                    artist.setSubcount(subscriptionEntities.size());
                    userRepository.save(artist);
                    logger.info("User: " + autoSubuser.getUsername() + " has unsubscribed from Artist: " + artist.getUsername());
                }
            }
            List<UserSubscriptionEntity> subscriptionEntities = subscriptionService.getArtistSubscription(subscription.getArtistname());
            artist.setSubcount(subscriptionEntities.size());
            userRepository.save(artist);
            logger.info("Nothing changes");
        }
    }

}
