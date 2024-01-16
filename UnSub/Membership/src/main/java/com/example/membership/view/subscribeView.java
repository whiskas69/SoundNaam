package com.example.membership.view;


import com.example.membership.core.data.UserSubscriptionEntity;
import com.example.user.core.data.User;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Membership")
@Route(value = "subscript")
public class subscribeView extends VerticalLayout {
    Icon lumoIcon = LumoIcon.SEARCH.create();
    Icon like = VaadinIcon.HEART_O.create();
    Icon heart = VaadinIcon.HEART_O.create();
    Icon left = VaadinIcon.CHEVRON_CIRCLE_LEFT.create();
    Icon right = VaadinIcon.CHEVRON_CIRCLE_RIGHT.create();
    Icon play = VaadinIcon.PLAY_CIRCLE.create();
    Icon stop = VaadinIcon.STOP.create();


    //
    private H1 name;
    private Image profileImage;
    private Image profileImg;
    private Div playlist, subsription, redeem;
    private TabSheet tabSheet;
    private TextField text;
    private TextArea ta1;

    private List<UserSubscriptionEntity> subscriptionEntities;

    private Button b1;

    private User user;

    private List<User> allArtist = new ArrayList<>();
    private String useremail = "Meen@gmail.com"; // get user from here



    public subscribeView() {
        createUI();
    }

    private void createUI() {


        Div mainContainer = new Div();
        mainContainer.getStyle().set("display", "flex");
        mainContainer.getStyle().set("height", "100vh");
        mainContainer.getStyle().set("flex", "1");// 100% of the viewport height


        Div leftSection = new Div();
        Image logoImage = new Image("/images/Logo.png", "Logo");

        logoImage.getStyle().set("width" ,"300px").set("height", "150px");


        Div logo = new Div();

        logo.getStyle().set("text-align", "center");

        logo.add(logoImage);
        leftSection.getStyle().set("background-color", "#5F9DB2");
        leftSection.getStyle().set("flex", "1");
        leftSection.getStyle().set("height", "100%"); // 1/3 of the height
        leftSection.getStyle().set("width", "5%");

        leftSection.add(logo);
        Div rightSection = new Div();

        rightSection.getStyle().set("background-color", "white");
        rightSection.getStyle().set("flex", "3");
        rightSection.getStyle().set("height", "100%");

        Div searchBar = new Div();
        searchBar.getStyle().set("display" , "flex");
        TextField search = new TextField();
        profileImage = new Image();
        search.getStyle().set("margin", "50px").set("width", "800px");
        profileImage.getStyle().set("width", "50px").set("height", "50px").set("border-radius", "50px").set("margin-top", "50px");


        searchBar.add(search, profileImage);


        Div profileBar = new Div();
        profileBar.getStyle().set("display", "flex");

        profileImg = new Image();
        profileImg.getStyle().set("width", "200px").set("height", "200px").set("border-radius", "50%").set("margin" , "20px");
        Div nameBar = new Div();
        name = new H1();

        H6 text = new H6("playList 6");

        nameBar.add(name, text);


        profileBar.add(profileImg, nameBar);
        profileBar.getStyle().set("alignItems", "center");

        playlist = new Div();
        subsription = new Div();
        redeem = new Div();
        TabSheet tabSheet = new TabSheet();
        tabSheet.add("Playlist", playlist);
        tabSheet.add("Subsription", subsription);
        tabSheet.add("Redeem", redeem);
        tabSheet.setWidthFull();



        rightSection.add(searchBar, profileBar, tabSheet);
        mainContainer.add(leftSection,rightSection);
        addSubscriptionContent();

        // Add the main container to the view
        add(mainContainer);


    }
    private void addSubscriptionContent() {
        getSubscriptionEntities();
        HorizontalLayout artistLayout = new HorizontalLayout();

        for (User artist : allArtist) {
            Div subs = new Div();
            Image img = new Image(artist.getImage(), "image");
            img.getStyle()
                    .set("width", "150px")
                    .set("height", "150px")
                    .set("border-radius", "50%")
                    .set("margin", "10px")
                    .set("background-color", "black");

            Text artistName = new Text(artist.getUsername());
            subs.getStyle()
                    .set("display", "flex")
                    .set("flexDirection", "column")
                    .set("alignItems", "center")
                    .set("textAlign", "center");

            subs.add(img, artistName);
            artistLayout.add(subs);
        }

        // Add the HorizontalLayout to your main layout
        subsription.add(artistLayout);
    }

    public List<User> getAllArtistfromSubscription() {

        for (UserSubscriptionEntity subscription : subscriptionEntities) {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("artistname", subscription.getArtistname());
            formData.add("role", "Artist");


            List<User> artists = WebClient.create()
                    .post()
                    .uri("http://localhost:8082/user-service/user/getArtist")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToFlux(User.class)
                    .collectList()  // Collect the responses into a list
                    .block();

            if (artists != null) {
                System.out.println(artists);
                this.allArtist.addAll(artists);
            }
        }
        System.out.println(allArtist);
        return allArtist;
    }

    public List<UserSubscriptionEntity> getSubscriptionEntities(){
        MultiValueMap<String, String> Package = new LinkedMultiValueMap<>();
        Package.add("useremail", this.useremail);
        this.subscriptionEntities = WebClient.create()
                .post()
                .uri("http://localhost:8082/membership-service/subscription/getUserSubscription")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(Package))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserSubscriptionEntity>>() {
                })
                .block();
        System.out.println(subscriptionEntities);
        getAllArtistfromSubscription();
    return subscriptionEntities;
    }

}
