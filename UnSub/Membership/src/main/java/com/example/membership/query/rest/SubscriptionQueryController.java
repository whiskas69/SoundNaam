package com.example.membership.query.rest;

import com.example.membership.core.data.UserSubscriptionEntity;
import com.example.membership.core.service.SubscriptionService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/subscription")
public class SubscriptionQueryController {

    private SubscriptionService subscriptionServicel;

    public SubscriptionQueryController(SubscriptionService subscriptionServicel) {
        this.subscriptionServicel = subscriptionServicel;
    }

    @PostMapping("/getUserSubscription")
    public List<UserSubscriptionEntity> getUserSubscription(@RequestParam String useremail){
        System.out.println(useremail + " /getUserSubscription");
        List<UserSubscriptionEntity> subscriptionEntities  = subscriptionServicel.getUserSubscription(useremail);

        return  subscriptionEntities;
    }

    @PostMapping("/getChannelSubscription")
    public List<UserSubscriptionEntity> getArtistSubscription(@RequestParam Map<String, String> formData){
        String artistname = formData.get("artistname");
        List<UserSubscriptionEntity> subscriptionEntities  = subscriptionServicel.getArtistSubscription(artistname);

        return  subscriptionEntities;
    }

    @PostMapping("/getChannelSubscriptionTotal")
    public String getArtistSubscriptionTotal(@RequestParam Map<String, String> formData){
        String artistname = formData.get("artistname");
        List<UserSubscriptionEntity> subscriptionEntities  = subscriptionServicel.getArtistSubscription(artistname);

        return  "Total subscribe: " + subscriptionEntities.size();
    }

    @PostMapping("/getAllsubscription")
    public List<UserSubscriptionEntity> getArtistSubscriptionTotal(){
        List<UserSubscriptionEntity> subscriptionEntities  = subscriptionServicel.getAllsubscription();

        return  subscriptionEntities;
    }

    @PostMapping("/getSubsciptionDuration")
    public String getSubsciptionDuration(@RequestParam Map<String, String> formData){
        String useremail = formData.get("useremail");
        String artistname = formData.get("artistname");
        UserSubscriptionEntity subscriptionEntity = subscriptionServicel.getByUserEmailAndArtistName(useremail, artistname);

        LocalDateTime startDate = subscriptionEntity.getStartDate();
        LocalDateTime endDate = subscriptionEntity.getEndDate();

        // Calculate the period between start and end dates
        Period period = calculatePeriod(startDate, endDate);

        System.out.println("Years " + period.getYears() + "Months " + period.getMonths());

        // Build the result String
        String result = buildResultString(period);

        return result;
    }

    private Period calculatePeriod(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDate startLocalDate = startDate.toLocalDate();
        LocalDate endLocalDate = endDate.toLocalDate();

        return Period.between(startLocalDate, endLocalDate);
    }

    private String buildResultString(Period period) {
        int years = period.getYears();
        int months = period.getMonths();

        if (years > 0) {
            return String.format("Expires in %d years and %d months", years, months);
        } else if (years == 0 && months > 0) {
            return String.format("Expires in %d months", months);
        } else {
            return "Subscription has expired";
        }
    }

}
