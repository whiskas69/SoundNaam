package com.example.soundnaam.view;

import com.example.soundnaam.POJO.Podcast;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayInputStream;
import java.util.*;

@Route("HomePodcast")
public class HomePodcast extends HorizontalLayout {
    private TextField searchbar;
    private VerticalLayout layoutcol, podcastlist, leftnav;
    private HorizontalLayout searchcol;
    private Tabs tabs;
    private Avatar user;
    private H3 podcast, series;
    private List<Podcast> allpodcast;

    public HomePodcast() {
        allpodcast = new ArrayList<>();

        searchbar = new TextField();
        layoutcol = new VerticalLayout();
        tabs = new Tabs();
        user = new Avatar("Anpanprang");
        searchcol = new HorizontalLayout();
        podcast = new H3("PODCASTS");
        //series = new H3("SERIES");
        podcastlist = new VerticalLayout();
        allpodcast = new ArrayList<>();

        //left navigator
        leftnav = new VerticalLayout();

        Image logoImage = new Image("/images/Logo.png", "Logo");

        tabs.add(createTab(VaadinIcon.HOME, "Home", HomeSong.class),
                createTab(VaadinIcon.BOOKMARK, "Subscriptions", UserProfile.class),
                createTab(VaadinIcon.HEADPHONES, "Podcast", HomePodcast.class),
                createTab(VaadinIcon.PLAY_CIRCLE, "Playlist", UserProfile.class),
                createTab(VaadinIcon.TIME_BACKWARD, "History", HomeSong.class));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setSelectedTab(tabs.getTabAt(2));

        //logout button
        Button logout = new Button("LogOut");
        Button createplaylist = new Button("Create Playlist");
        Button createsong = new Button("Create Song");
        Button createpodcast = new Button("Create Podcast");
        createplaylist.getStyle().set("background-color", "#FFA62B");
        createsong.getStyle().set("background-color", "#FFA62B");
        createpodcast.getStyle().set("background-color", "#FFA62B");
        logout.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        createplaylist.setWidth("50%");
        createsong.setWidth("50%");
        createpodcast.setWidth("50%");
        logout.setWidth("50%");

        leftnav.add(logoImage, tabs, createsong, createpodcast, createplaylist,logout);
        leftnav.setWidth("20%");
        leftnav.setHeightFull();
        leftnav.setAlignSelf(Alignment.CENTER, logoImage);
        leftnav.setAlignSelf(Alignment.END, createsong, createpodcast, createplaylist,logout);


        user.addThemeVariants(AvatarVariant.LUMO_XLARGE);

        searchbar.setPlaceholder("Search");
        searchbar.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchbar.setWidth("90%");

        searchcol.setWidthFull();
        searchcol.setAlignItems(FlexComponent.Alignment.CENTER);
        searchcol.setSpacing(true);
        searchcol.add(searchbar, user);

        loadpodcast();

        for (int i=0; i < this.allpodcast.size(); i++) {
            HorizontalLayout card = createPodcastlist(this.allpodcast.get(i));

            podcastlist.add(card);

            int index = i;
            card.addClickListener(e -> {
                nextpage(allpodcast.get(index).get_id());
            });
        }


        //podcastlist.add(createPodcastlist(new Podcast("https://i.pinimg.com/originals/31/c0/0b/31c00b5afd3a9700111c094f8c0169f3.jpg", "Album Name 1", "Artist 1", "3.59")));
        layoutcol.setSizeFull();
        layoutcol.add(searchcol, podcast, podcastlist);
        layoutcol.getStyle().set("background-color", "#ECE7E3");
        this.add(leftnav,layoutcol);
        this.setSizeFull();

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

    public void loadpodcast(){
        this.allpodcast = WebClient.create()
                .get().uri("http://localhost:8080/getPodcast")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Podcast>>() {})
                .block();
        System.out.println("--------------------------------------------------------------------------------------------------------------");
        System.out.println(this.allpodcast);
    }

    private void nextpage(String podcastId) {
        System.out.println("songId next: " + podcastId);
        Map<String, List<String>> parameters = Collections.singletonMap("id", Collections.singletonList(podcastId));
        UI.getCurrent().navigate(
                PodcastPlayView.class,
                new QueryParameters(parameters)
        );
    }

    private HorizontalLayout createPodcastlist(Podcast podcast) {
        // Create components for the card
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                .maxInMemorySize(10 * 1024 * 1024)) // Set the buffer limit to 5 MB
                        .build());

        byte[] dataImage = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/getAudioSong/" + podcast.getImage())
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

        //แปลง image
        System.out.println("dataImage " + dataImage);
        StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(dataImage));
        Image cover = new Image(resource, "cover image");
        cover.setWidth("200px");

        H5 nameText = new H5("Name: " + podcast.getTitle());
        H6 artistText = new H6("Artist: " + podcast.getArtist());
        System.out.println(podcast.getArtist());

        // Create a vertical layout to hold the components
        HorizontalLayout cardLayout = new HorizontalLayout();
        VerticalLayout textLayout = new VerticalLayout();
        textLayout.add(nameText, artistText);

        Div spacer = new Div();
        spacer.setWidth("90%");

        cardLayout.add(cover, textLayout, spacer);
        cardLayout.setSpacing(true);
        cardLayout.setWidth("90%"); // Set the width as needed
        cardLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return cardLayout;
    }

    private void logout() {
        // Remove token from localStorage
        UI.getCurrent().getPage().executeJs("localStorage.removeItem($0)", "token");

        // Redirect to the login page or perform other logout actions
        UI.getCurrent().navigate(MainView.class);
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

}