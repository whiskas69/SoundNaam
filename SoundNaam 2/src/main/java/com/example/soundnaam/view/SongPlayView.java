package com.example.soundnaam.view;


import com.example.soundnaam.POJO.Music;
import com.example.soundnaam.POJO.Podcast;
import com.example.soundnaam.POJO.Song;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;


import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Route("/song")
public class SongPlayView extends VerticalLayout implements BeforeEnterObserver {
    //    attribute
    private List<Song> songs;

    private int currentSongIndex = 0;
    private Song song;
    private Music music;
    private String textId;
    //    private List<Comment> comments;
    private String rating;

    private byte[] dataMusic, dataImage;




    //    ui component
    private TextField search;
    private Button ad, update, delete, clear, playPauseButton;

    //    like and dislike
    private Button likeBtn, dislikeBtn;
    private H5 Crating;
    private Tabs tabs;
    Icon lumoIcon = LumoIcon.SEARCH.create();

    Icon heartIcon;
    TabSheet tabSheet;
    VerticalLayout rightColumn;
    HorizontalLayout leftColumn;
    HorizontalLayout h1, h2, listSong;
    Image cover, test;
    Div upNext = new Div();
    Div lyrics = new Div();
    Div comment = new Div();
    Div player = new Div();



    private AdvancedPlayer playerAudio;


    private Button playButton, previousButton, nextButton;
    private Button stopButton;
    private boolean isPlaying = false;
//    private Button pauseButton;
//    private boolean isPaused = false;
//    private int pausedFrame;

    public void beforeEnter(BeforeEnterEvent event) {
        // Retrieve the email parameter from the URL
        System.out.println("1234email: aslkdfjklasjdfklasjdfkjaskldfjkasjfdk");
        QueryParameters queryParameters = event.getLocation().getQueryParameters();
        String nId = queryParameters.getParameters().get("id").get(0);
        this.textId = nId;
        if (nId.isEmpty() || nId.isBlank()){
            this.textId = "6551464ffd9e1c5683b66051";
        }
        System.out.println("1234email: " + nId);
        System.out.println("this.textId: " + textId);
        loadSong();
    }

    public SongPlayView() {
//        data
        songs = new ArrayList<>();




//        set layout
        h1 = new HorizontalLayout();
        h2 = new HorizontalLayout();
        playButton = new Button("play");
        stopButton = new Button("stop");
        nextButton = new Button("Next", new Icon(VaadinIcon.ARROW_RIGHT));
        previousButton = new Button("Previous", new Icon(VaadinIcon.ARROW_LEFT));
//        h1.setWidth("100%");

        leftColumn = new HorizontalLayout();
        leftColumn.setWidth("250px");
        leftColumn.add(previousButton, playButton, stopButton, nextButton);

        rightColumn = new VerticalLayout();
        //rightColumn.setWidth("80%");




        search = new TextField();
        search.setPlaceholder("search");
        search.setSuffixComponent(lumoIcon);
        search.setWidth("1000px");



        System.out.println("dataImage " + this.dataImage);
        StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(this.dataImage));
        Image cover = new Image(resource, "cover image");
        cover.setWidth("720px");
        cover.setHeight("500px");

        heartIcon = new Icon(VaadinIcon.HEART);
        playPauseButton = new Button(new Icon(VaadinIcon.PLAY));



        TabSheet tabSheet = new TabSheet();
        tabSheet.add("UP NEXT", upNext);
        tabSheet.add("LYRICS", lyrics);
        tabSheet.add("COMMENTS", comment);


        tabSheet.setHeight("500px");
        tabSheet.setWidth("320px");
        h2.add(cover, tabSheet);


        rightColumn.add(search, h2, leftColumn);


        playButton.addClickListener(buttonClickEvent -> {
            if (!isPlaying) {
                isPlaying = true;
                CompletableFuture.runAsync(() -> playAudio(dataMusic));
            }
        });
        stopButton.addClickListener(event -> {
            stopAudio();
            System.out.println("stop" + this.playerAudio);
        });



//        like and dislike
        likeBtn = new Button("like");
        dislikeBtn = new Button("dislike");
        likeBtn.addClickListener(event -> {
            List<Song> info = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getSongById/" + this.textId)
                    .retrieve().bodyToMono(new ParameterizedTypeReference<List<Song>>() {
                    })
                    .block();

            System.out.println("like bt : " + info);

            Song data = new Song(info.get(0).get_id(),
                    info.get(0).getTitle(),
                    info.get(0).getLyrics(),
                    info.get(0).getArtist(),
                    info.get(0).getDataAudio(),
                    info.get(0).getImage(),
                    info.get(0).getDate(),
                    info.get(0).getAlbum(),
                    info.get(0).getLike() + 1,
                    info.get(0).getDislike(),
                    info.get(0).getView() + 1,
                    info.get(0).getEmail(),
                    info.get(0).getUsername()
            );

            Boolean output = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateSong")
                    .bodyValue(data)
                    .retrieve().bodyToMono(Boolean.class).block();

        });
        dislikeBtn.addClickListener(event -> {

            List<Song> info = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getSongById/" + this.textId)
                    .retrieve().bodyToMono(new ParameterizedTypeReference<List<Song>>() {
                    })
                    .block();

            System.out.println("like bt : " + info);

            Song data = new Song(info.get(0).get_id(),
                    info.get(0).getTitle(),
                    info.get(0).getLyrics(),
                    info.get(0).getArtist(),
                    info.get(0).getDataAudio(),
                    info.get(0).getImage(),
                    info.get(0).getDate(),
                    info.get(0).getAlbum(),
                    info.get(0).getLike(),
                    info.get(0).getDislike() + 1,
                    info.get(0).getView() + 1,
                    info.get(0).getEmail(),
                    info.get(0).getUsername()
            );

            Boolean output = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateSong")
                    .bodyValue(data)
                    .retrieve().bodyToMono(Boolean.class).block();
        });
        leftColumn.add(likeBtn, dislikeBtn);

//        จบตรงนี้ like and dislike
        //left navigator
        VerticalLayout leftnav = new VerticalLayout();

        Image logoImage = new Image("/images/Logo.png", "Logo");

        tabs = new Tabs();
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




        System.out.println(this.songs.size());
//        h1.add(leftnav, leftColumn, rightColumn);
        h1.add(leftnav, rightColumn);
        rightColumn.getStyle().set("background-color", "#ECE7E3");
        rightColumn.setSizeFull();
        add(h1);
        h1.setSizeFull();
        this.setSizeFull();
        this.setPadding(false);


        nextButton.addClickListener(event -> {

            playNextSong();

        });

        previousButton.addClickListener(event -> {
            playPreviousSong();
        });

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

        loadSong();
        loadComment();
        updateUpNextTabContent();

        rightColumn.add(Crating);

    }

    public void playAudio(byte[] audioData) {

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(audioData);
            playerAudio = new AdvancedPlayer(inputStream);

            // Add a listener to handle events (optional)
            playerAudio.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    isPlaying = false;
                    // Handle playback finished event if needed
                }
            });

            // Start playing
            playerAudio.play();

        } catch (JavaLayerException e) {
            e.printStackTrace();
            isPlaying = false;
        }
    }
    public void stopAudio() {
        if (playerAudio != null && isPlaying) {
            playerAudio.close();
            playerAudio = null;
            isPlaying = false;
        }
    }

    public void loadComment(){
//        this.comments = WebClient.create()
//                .get()
//                .uri("http://localhost:8080/getComment")
//                .retrieve().bodyToMono(new ParameterizedTypeReference<List<Comment>>() {})
//                .block();
        Image imageProfile = new Image("https://f.ptcdn.info/237/077/000/rbpkfh1n811fuK3R1MbUf-o.jpg", "profile user");
        imageProfile.getStyle().set("border-radius", "50%");
        imageProfile.setHeight("50px");
        imageProfile.setWidth("50px");


        VerticalLayout content = new VerticalLayout();
        Text usernameText = new Text("username - 20-10-2010\n");
        Text descriptionText = new Text("\nNeque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit");
//        descriptionText.getStyle().set("font-weight", "bold");
        content.add(usernameText, descriptionText);

        HorizontalLayout h = new HorizontalLayout();
        h.getStyle().set("border-bottom", "1px solid #ccc");
        h.setPadding(false);
        h.add(imageProfile, content);


        comment.add(h);
    }
    public void loadSong(){
        System.out.println("this.textId  in loading :  " + this.textId);
        if (this.textId != null){
            this.songs = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getSongById/" + this.textId)
//                .uri("http://localhost:8080/getSong")
                    .retrieve().bodyToMono(new ParameterizedTypeReference<List<Song>>() {})
                    .block();
        }else {
            this.songs = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getSong")
//                .uri("http://localhost:8080/getSong")
                    .retrieve().bodyToMono(new ParameterizedTypeReference<List<Song>>() {})
                    .block();
        }
        System.out.println(this.songs + "songsssssssss");
//        System.out.println(this.song + "songgggggggg");

        System.out.println(this.songs.get(0).getDataAudio());
        String DataAudioId = this.songs.get(0).getDataAudio();
        String imageId = this.songs.get(0).getImage();

        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                .maxInMemorySize(5 * 1024 * 1024)) // Set the buffer limit to 5 MB
                        .build());

        byte[] audioData = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/getAudioSong/" + DataAudioId)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

        this.dataImage = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/getAudioSong/" + imageId)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();


//        rating

        this.rating = CountRating(this.songs.get(0).getTitle(), this.songs.get(0).getArtist());
        Crating = new H5("this.rating " + this.rating);
        Crating.getStyle().set("font-size", "50px");
        Crating.getStyle().set("color", "magenta");
        this.dataMusic = audioData;

// Now 'audioData' contains the raw audio data
//        System.out.println("music data: " + Arrays.toString(audioData));
//        System.out.println(this.music);
        System.out.println(this.songs.get(0).getTitle());


    }

    private void updateUpNextTabContent() {
        System.out.println("song in update up next" + this.songs);
        listSong = new HorizontalLayout();
        Image cover = new Image(songs.get(0).getImage(), "cover image");
        cover.setWidth("100px");
        listSong.add(cover,
                new Text(songs.get(0).getTitle() + '\n' + songs.get(0).getArtist()),
                new Text("Time")
        );
        listSong.setWidth("350px");
        if (songs != null && !songs.isEmpty()) {
            VerticalLayout upNextContent = new VerticalLayout();

            for (Song song : songs) {
                System.out.println("song in next " + song.getImage());

                StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(getImageData(song.getImage())));
                Image coverImage = new Image(resource, "cover image");

                coverImage.setWidth("80px");

                HorizontalLayout songInfo = new HorizontalLayout(
                        coverImage,
                        new Text(song.getTitle() + '\n' + song.getArtist()),
                        new Text("Time")
                );
                HorizontalLayout songEntry = new HorizontalLayout(songInfo);
                songEntry.setWidth("100%");
                upNextContent.add(songEntry);
            }
            upNext.removeAll();
            upNext.add(upNextContent);
            lyrics.removeAll();
            lyrics.add(songs.get(0).getLyrics());
        } else {
            upNext.removeAll();
            upNext.add(new Text("No data"));
        }
    }

    private void playSelectedSong() {

        if (songs != null && !songs.isEmpty()) {

            Song selectedSong = songs.get(currentSongIndex);


            String selectedSongId = selectedSong.getDataAudio();
            System.out.println("Selected Podcast ID: " + selectedSongId);


            WebClient.Builder webClientBuilder = WebClient.builder()
                    .exchangeStrategies(ExchangeStrategies.builder()
                            .codecs(configurer -> configurer.defaultCodecs()
                                    .maxInMemorySize(10 * 1024 * 1024)) // Set the buffer limit to 10 MB
                            .build());

            byte[] audioData = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8080/getAudioPodcast/" + selectedSongId)
                    .accept(MediaType.APPLICATION_OCTET_STREAM)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
            System.out.println("Fetched Audio Data: " + audioData);


            CompletableFuture.runAsync(() -> playAudio(audioData));
            this.dataMusic = audioData;
            isPlaying = true;
            System.out.println("Updated dataMusic: " + dataMusic);
        }
    }

    private void playNextSong() {
        if (!songs.isEmpty()) {
            if (currentSongIndex < songs.size() - 1) {
                currentSongIndex++;
            } else {
                currentSongIndex = 0;
            }
            stopAudio();
            playSelectedSong();
            updateUpNextTabContent();
        }
        isPlaying = true;
        System.out.println(currentSongIndex);
    }

    private void playPreviousSong() {
        if (!songs.isEmpty()) {
            if (currentSongIndex > 0) {
                currentSongIndex--;
            } else {
                currentSongIndex = songs.size() - 1;
            }
            stopAudio();
            playSelectedSong();
            updateUpNextTabContent();
        }
        isPlaying = true;
        System.out.println(currentSongIndex);
    }

    private byte[] getImageData(String imageId) {
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                .maxInMemorySize(5 * 1024 * 1024))
                        .build());

        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/getAudioSong/" + imageId)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
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

    //    rating
    public String CountRating(String title, String artist){
        System.out.println("name : " + title);
        System.out.println("school : " + artist);
        String Rating = WebClient.create()
                .get()
                .uri("http://127.0.0.1:5000/song/"+title + "/" + artist)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println("rating : " + Rating);
        return Rating;
    }
}