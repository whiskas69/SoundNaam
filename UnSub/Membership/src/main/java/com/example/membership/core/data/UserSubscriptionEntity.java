package com.example.membership.core.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document("Subscription")
@Entity
public class UserSubscriptionEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 690789922146209703L;

    private String _id;
    @Id
    private String subscriptionId;
    private String useremail;
    private String artistname;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean subscribed;
}
