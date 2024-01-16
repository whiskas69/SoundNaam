package com.example.soundnaam.view;

import com.example.soundnaam.POJO.Artist;
import com.example.soundnaam.POJO.Playlist;
import com.example.soundnaam.POJO.Song;
import com.example.soundnaam.POJO.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayInputStream;
import java.util.*;

@Route("UserProfile")
public class UserProfile extends Div {
    private H1 name;
    private Image profileImage;
    private Image profileImg;

    private Div playlist, subsription, redeem;
    private List<Playlist> playlists;
    private List<Song> songs;
    private Playlist pl;

    private String _id;
    private String email;
    private String username;
    private String playlistName;
    private String coverPl;
    private String statusPl;
    private Date date;
    private Tabs tabs;

    private List[] song;
    //    private List<UserSubscriptionEntity> subscriptionEntities;
    private List<Artist> allArtist;

    public UserProfile() {
        playlists = new ArrayList<>();
        songs = new ArrayList<>();
        createUI();
    }

    private void createUI() {


        Div mainContainer = new Div();
        mainContainer.getStyle().set("display", "flex");
        mainContainer.getStyle().set("height", "100vh");
        mainContainer.getStyle().set("flex", "1");// 100% of the viewport height


        Div leftSection = new Div();
        Image logoImage = new Image("/images/Logo.png", "Logo");
        Button logout = new Button("LogOut");

//        logout.getStyle().set("width", "200px");
        logoImage.getStyle().set("width" ,"300px").set("height", "150px");

        //left navigator
        VerticalLayout leftnav = new VerticalLayout();

//        Image logoImage = new Image("/images/Logo.png", "Logo");

        tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.HOME, "Home", HomeSong.class),
                createTab(VaadinIcon.BOOKMARK, "Subscriptions", UserProfile.class),
                createTab(VaadinIcon.HEADPHONES, "Podcast", HomePodcast.class),
                createTab(VaadinIcon.PLAY_CIRCLE, "Playlist", UserProfile.class),
                createTab(VaadinIcon.TIME_BACKWARD, "History", HomeSong.class));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setSelectedTab(tabs.getTabAt(2));

        //logout button
        Button createplaylist = new Button("Create Playlist");
        Button createsong = new Button("Create Song");
        Button createpodcast = new Button("Create Podcast");
        createplaylist.getStyle().set("background-color", "#FFA62B");
        createsong.getStyle().set("background-color", "#FFA62B");
        createpodcast.getStyle().set("background-color", "#FFA62B");
        logout.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

//        createplaylist.setWidth("50%");
//        createsong.setWidth("50%");
//        createpodcast.setWidth("50%");
//        logout.setWidth("50%");


        Div logo = new Div();

        logo.getStyle().set("text-align", "center");

        logo.add(logoImage, tabs, createsong, createpodcast, createplaylist, logout);
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
        rightSection.getStyle().set("background-color", "#ECE7E3");
        mainContainer.add(leftSection,rightSection);



        // Add the main container to the view
        add(mainContainer);
        loadPage();


        logout.addClickListener(event ->{
            logout();
        });
        createplaylist.addClickListener(event ->{
            navigateToCeatePlaylist();
        });
        createsong.addClickListener(event ->{
            navigateToCeateSong();
        });
        createpodcast.addClickListener(event ->{
            navigateToCeatePodcast();
        });

    }


    private void loadPage() {
        String key = "token";
        UI.getCurrent().getPage().executeJs("return localStorage.getItem($0)", key)
                .then(String.class, this::fetchUserData);
    }

    private void fetchUserData(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        if(token == null){
            UI.getCurrent().navigate(MainView.class);
        }
        try {
            String jsonResponse = WebClient.builder()
                    .baseUrl("http://localhost:8080")
                    .defaultHeaders(header -> header.addAll(headers))
                    .build()
                    .get()
                    .uri("/me")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("JSON Response: " + jsonResponse);


            ObjectMapper objectMapper = new ObjectMapper();

            // Convert JSON string to User object using Jackson
            User user = objectMapper.readValue(jsonResponse, User.class);

            // Store the username in a class variable


            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());

            name.setText(user.getUsername());

            myPlaylist(user.getEmail());
            System.out.println(user.getImage().length());

            if(user.getImage().length() == 24) {



                WebClient.Builder webClientBuilder = WebClient.builder()
                        .exchangeStrategies(ExchangeStrategies.builder()
                                .codecs(configurer -> configurer.defaultCodecs()
                                        .maxInMemorySize(10 * 1024 * 1024)) // Set the buffer limit to 5 MB
                                .build());

                byte[] dataImage = webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8080/getAudioSong/" + user.getImage())
                        .accept(MediaType.APPLICATION_OCTET_STREAM)
                        .retrieve()
                        .bodyToMono(byte[].class)
                        .block();

                StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(dataImage));

                System.out.println("User : " + resource);

                profileImage.setSrc(resource);
                profileImg.setSrc(resource);
            }
            else{
                profileImage.setSrc(user.getImage());
                profileImg.setSrc(user.getImage());
            }
        } catch (Exception e) {
            // Handle the exception appropriately
            System.err.println("Error fetching user data: " + e.getMessage());
        }
    }

    private void logout() {
        // Remove token from localStorage
        UI.getCurrent().getPage().executeJs("localStorage.removeItem($0)", "token");

        // Redirect to the login page or perform other logout actions
        UI.getCurrent().navigate(MainView.class);
    }

    private void myPlaylist(String email){
        this.playlists = WebClient.create()
                .get()
                .uri("http://localhost:8080/getPlaylistByEmail/" + email)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Playlist>>() {})
                .block();
        System.out.println("-------------------------------------------MY playlists-------------------------------------------------------------------");
        System.out.println(this.playlists);


        VerticalLayout veri = new VerticalLayout();

        if (playlists != null && !playlists.isEmpty()) {
            HorizontalLayout allSongs = new HorizontalLayout();
            for (Playlist myPlaylist : playlists) {



                Text myStatus = new Text((myPlaylist.getStatus()));

                H4 myTitle = new H4(myPlaylist.getPlaylistName());
                myTitle.addClickListener(e -> navigateToNextPage(myPlaylist.getPlaylistName()));

                VerticalLayout veriSongs = new VerticalLayout();
                veriSongs.add( myTitle, myStatus);
                allSongs.add(veriSongs);
            }

            playlist.add(allSongs);
            playlist.setSizeFull();
        }
        else {
            playlist.removeAll();
            playlist.add(new Text("No data"));
        }

        System.out.println("===========================================================================");
        System.out.println("PLAYLIST"+playlist);
    }

    private void navigateToNextPage(String playlistName) {
//        UI.getCurrent().navigate(PlaylistView.class);
        this.pl = WebClient.create().get().uri("http://localhost:8080/getNamePlaylist/"+ this.playlists.get(0).getPlaylistName())
                .retrieve()
                .bodyToMono(Playlist.class)
                .block();
        System.out.println("pl" + pl);
        System.out.println("this.pl.getPlaylistName" + this.pl.getPlaylistName());

        if (this.pl != null) {
            this._id = this.pl.get_id();
            this.email = this.pl.getEmail();
            this.username = this.pl.getUsername();
            this.playlistName = this.pl.getPlaylistName();
            this.coverPl = this.pl.getCover();
            this.statusPl = this.pl.getStatus();
            this.date = this.pl.getDate();
            this.songs = this.pl.getSong();
        }
        System.out.println("playlistName" + this.pl.getPlaylistName());

        Map<String, List<String>> parameters = Collections.singletonMap("playlistName", Collections.singletonList(playlistName));

        UI.getCurrent().navigate(
                PlaylistView.class,
                new QueryParameters(parameters)
        );
    }
    private Tab createTab(VaadinIcon viewIcon, String viewName, Class content) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        // Demo has no routes
        link.setRoute(content);
        link.setTabIndex(-1);

        return new Tab(link);
    }

    private void navigateToCeatePlaylist(){
        UI.getCurrent().navigate(CreatePlaylist.class);
    }

    private void navigateToCeateSong(){
        UI.getCurrent().navigate(CreateSong.class);
    }

    private void navigateToCeatePodcast(){
        UI.getCurrent().navigate(CreatePodcast.class);
    }

    // ===============================================================================================================
//
//    public List<Artist> getAllArtistfromSubscription() {
//
//        for (UserSubscriptionEntity subscription : subscriptionEntities) {
//            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//            formData.add("artistname", subscription.getArtistname());
//
//            List<Artist> artists = WebClient.create()
//                    .post()
//                    .uri("http://localhost:8082/artist-service/artist/getChannel")
//                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                    .body(BodyInserters.fromFormData(formData))
//                    .retrieve()
//                    .bodyToFlux(Artist.class)
//                    .collectList()  // Collect the responses into a list
//                    .block();
//
//            if (artists != null) {
//                this.allArtist.addAll(artists);
//            }
//        }
//        System.out.println(allArtist);
//        return allArtist;
//    }
//
//    public List<UserSubscriptionEntity> getSubscriptionEntities(String useremail){
//        MultiValueMap<String, String> Package = new LinkedMultiValueMap<>();
//        Package.add("useremail", useremail);
//        this.subscriptionEntities = WebClient.create()
//                .post()
//                .uri("http://localhost:8082/membership-service/subscription/getUserSubscription")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .body(BodyInserters.fromFormData(Package))
//                .retrieve()
//                .bodyToMono(new ParameterizedTypeReference<List<UserSubscriptionEntity>>() {
//                })
//                .block();
//        System.out.println(subscriptionEntities);
//        getAllArtistfromSubscription();
//        return subscriptionEntities;
//    }


}