package com.example.soundnaam.view;

import com.example.soundnaam.POJO.Playlist;
import com.example.soundnaam.POJO.Song;
import com.example.soundnaam.POJO.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
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

@Route(value = "HomeSong")
public class HomeSong extends HorizontalLayout {
    private List<Song> songs;
    private TextField searchbar;
    private HorizontalLayout horizontalLayout, artistlist;
    private HorizontalLayout  albumlist = new HorizontalLayout();
    private VerticalLayout layoutcol, albumcol, leftnav;
    private Tabs tabs;
    private Avatar user;
    private H3 playlists, musics, artist;
    private Image img;
    private List<Album> displayalbums;
    private HorizontalLayout albumsLayout;
    private List<Playlist> playlistsHome;
    private Playlist pl;
    private String playlistName;



    public HomeSong() {

        loading();
        loadPlaylist();
        searchbar = new TextField();
        horizontalLayout = new HorizontalLayout();
        layoutcol = new VerticalLayout();
        tabs = new Tabs();
        user = new Avatar("Anpanprang");
        playlists = new H3("PLAYLISTS");
        musics = new H3("MUSICS");
        artist = new H3("ARTISTS");
        displayalbums = new ArrayList<>();

        img = new Image();
        albumcol = new VerticalLayout();
        artistlist = new HorizontalLayout();

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

        img.setSrc("https://i.pinimg.com/originals/31/c0/0b/31c00b5afd3a9700111c094f8c0169f3.jpg");


        displayalbums = new ArrayList<>();

        // Create a vertical layout to hold the list of albums
        albumsLayout = new HorizontalLayout();

        // Create a layout to hold the list of albums
        albumsLayout = new HorizontalLayout();
        albumsLayout.setWidthFull();
        albumcol.add(albumsLayout);

        for (int i = 0; i < this.songs.size(); i++) {
            HorizontalLayout card = createAlbumCard(this.songs.get(i));

            // Add the card to the current column layout
            albumsLayout.add(card);

            // Check if three cards have been added to the current column
            if ((i + 1) % 4 == 0) {
                // Add spacing between columns
                albumsLayout.setSpacing(true);

                // Create a new column layout
                albumsLayout = new HorizontalLayout();
                albumsLayout.setWidthFull();
                albumcol.add(albumsLayout);
            }
            int index = i;
            card.addClickListener(e -> {
                nextpage(songs.get(index).get_id());
            });

            albumlist.addClickListener(e -> {
                navigateToNextPage(playlistsHome.get(index).getPlaylistName());
            });


        }


        getArtist();

        horizontalLayout.setWidthFull();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        horizontalLayout.setSpacing(true);
        horizontalLayout.add(searchbar, user);
        layoutcol.add(horizontalLayout, playlists, albumlist, musics, albumcol, artist, artistlist);
        layoutcol.getStyle().set("background-color", "#ECE7E3");
        this.add(leftnav, layoutcol);
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
/*
        link.setTabIndex(-1);
*/

        return new Tab(link);
    }

    private Div createCard(String imageUrl, String name, String artist) {



        Image img = new Image();
        if(imageUrl.length() == 24) {
            WebClient.Builder webClientBuilder = WebClient.builder()
                    .exchangeStrategies(ExchangeStrategies.builder()
                            .codecs(configurer -> configurer.defaultCodecs()
                                    .maxInMemorySize(10 * 1024 * 1024)) // Set the buffer limit to 5 MB
                            .build());

            byte[] dataImage = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8080/getAudioSong/" + imageUrl)
                    .accept(MediaType.APPLICATION_OCTET_STREAM)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();

            StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(dataImage));

            System.out.println("User : " + resource);

            img.setSrc(resource);

        }
        else{

            img.setSrc(imageUrl);
        }

        img.setWidth("150px");
        H5 displayname = new H5(name);
        H6 displayartist = new H6(artist);
        VerticalLayout col = new VerticalLayout();
        col.add(img);
        col.add(displayname);
        col.add(displayartist);
        col.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return new Div(col);
    }

    private void navigateToNextPage(String playlistName) {
//        UI.getCurrent().navigate(PlaylistView.class);
        System.out.println("songId next: " + playlistName);
        Map<String, List<String>> parameters = Collections.singletonMap("playlistName", Collections.singletonList(playlistName));
        UI.getCurrent().navigate(
                PlaylistView.class,
                new QueryParameters(parameters)
        );

    }
    private void loadPlaylist(){
        playlistsHome = WebClient.create()
                .get()
                .uri("http://localhost:8080/getAllPlaylist")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Playlist>>() {})
                .block();
        System.out.println("-------------------------------------------MY playlists-------------------------------------------------------------------");
        System.out.println(playlistsHome);

        if(playlistsHome != null){
            for(Playlist playlistPub : playlistsHome){
                if(playlistPub.getStatus().equals("Public")){
                    String imgPlaylist = playlistPub.getCover();
                    String playName = playlistPub.getPlaylistName();
                    String username = playlistPub.getUsername();
                    Div playlistALL = createCard(imgPlaylist, playName,username);


                    albumlist.add(playlistALL);

                }

                System.out.println("===="+playlistPub);
            }
        }else {
            System.out.println("No playlists found");
        }
    }




    private HorizontalLayout createAlbumCard(Song song) {
        // Create components for the card
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                .maxInMemorySize(10 * 1024 * 1024)) // Set the buffer limit to 5 MB
                        .build());

        byte[] dataImage = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/getAudioSong/" + song.getImage())
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

        Image img = new Image();
        img.setWidth("140px");
        img.getElement().getStyle().set("background-image", "url('data:image/jpeg;base64," + Base64.getEncoder().encodeToString(dataImage) + "')");
        img.getElement().getStyle().set("background-size", "cover");



        H5 nameText = new H5("Name: " + song.getTitle());
        H6 artistText = new H6("Artist: " + song.getArtist());
        System.out.println(song.getArtist());

        // Create a vertical layout to hold the components
        HorizontalLayout cardLayout = new HorizontalLayout();
        VerticalLayout textLayout = new VerticalLayout();
        textLayout.add(nameText, artistText);
        cardLayout.add(img, textLayout);
        cardLayout.setWidth("280px"); // Set the width as needed

        return cardLayout;
    }

    private void loading(){

        this.songs = WebClient.create()
                .get()
                .uri("http://localhost:8080/getSong")
                .retrieve().bodyToMono(new ParameterizedTypeReference<List<Song>>() {})
                .block();
        System.out.println("songs" + this.songs);
    }
    private void nextpage(String songId) {
        System.out.println("songId next: " + songId);
        Map<String, List<String>> parameters = Collections.singletonMap("id", Collections.singletonList(songId));
        UI.getCurrent().navigate(
                SongPlayView.class,
                new QueryParameters(parameters)
        );
    }

    private Div createArtistCard(String imageUrl, String artist, String email, String role) {
        Image img = new Image();

        if(imageUrl.length() == 24) {
            WebClient.Builder webClientBuilder = WebClient.builder()
                    .exchangeStrategies(ExchangeStrategies.builder()
                            .codecs(configurer -> configurer.defaultCodecs()
                                    .maxInMemorySize(10 * 1024 * 1024)) // Set the buffer limit to 5 MB
                            .build());

            byte[] dataImage = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8080/getAudioSong/" + imageUrl)
                    .accept(MediaType.APPLICATION_OCTET_STREAM)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();

            StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(dataImage));

            System.out.println("User : " + resource);

            img.setSrc(resource);

        }
        else{

            img.setSrc(imageUrl);
        }
        img.setWidth("150px");
        img.getStyle().set("border-radius", "50%");
        H6 displayartist = new H6(artist);

        displayartist.addClickListener(event ->{
            System.out.println(artist);
            System.out.println(email);

            navigateToNextPage(artist, email, imageUrl, role);
        });

        VerticalLayout col = new VerticalLayout();
        col.add(img);
        col.add(displayartist);
        col.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return new Div(col);
    }

    private void getArtist(){
        List<User> user =  WebClient.create()
                .get()
                .uri("http://localhost:8080/AllUser")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<User>>() {})
                .block();

        System.out.println(user);


        if(user != null){
            for(User singleUser : user){

                if(singleUser.getRole().equals("Artist")){
                    System.out.println(singleUser.getUsername());
                    String image = singleUser.getImage();
                    String role = singleUser.getRole();


                    String username = singleUser.getUsername();
                    String email = singleUser.getEmail();

                    Div artistCard = createArtistCard(image, username, email, role);
                    artistlist.add(artistCard);
                }
            }
        }
        else{
            System.out.println("No users found");
        }
    }

    private void navigateToNextPage(String artist, String email,String image, String role){
        Map<String, List<String>> parameters = new HashMap<>();
        parameters.put("email", Collections.singletonList(email));
        parameters.put("artist", Collections.singletonList(artist));
        parameters.put("image", Collections.singletonList(image));
        parameters.put("role", Collections.singletonList(role));


        UI.getCurrent().navigate(
                ArtistView.class,
                new QueryParameters(parameters)
        );
    }

    private static class Album {
        private String imageUrl;
        private String name;
        private String artist;

        public Album(String imageUrl, String name, String artist) {
            this.imageUrl = imageUrl;
            this.name = name;
            this.artist = artist;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getName() {
            return name;
        }

        public String getArtist() {
            return artist;
        }
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

