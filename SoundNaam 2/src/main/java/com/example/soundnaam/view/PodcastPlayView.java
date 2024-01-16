package com.example.soundnaam.view;

import com.example.soundnaam.POJO.Music;
import com.example.soundnaam.POJO.Podcast;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoIcon;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;


import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Route("/podcast")
public class PodcastPlayView extends VerticalLayout {


    private List<Podcast> podcasts;
    private Music music;
    private String imageSelected;

    private byte[] dataMusic, dataImage;


    private int currentPodcastIndex = 0;

    private boolean isLoadingAudio = false;
    private TextField search;
    private Button  playPauseButton;
    private H5 Crating;
    private Tabs tabs;
    Icon lumoIcon = LumoIcon.SEARCH.create();
    private String rating;
    Icon heartIcon;
    TabSheet tabSheet;
    private String textId;
    VerticalLayout  rightColumn;
    HorizontalLayout leftColumn;
    HorizontalLayout h1, h2, listPodcast;
    Image coverImage;
    Div upNext = new Div();
    Div Details = new Div();
    Div comment = new Div();
    Div player = new Div();
    private Button likeBtn, dislikeBtn;


    private AdvancedPlayer playerAudio;


    private Button playButton;
    private Button stopButton;
    private boolean isPlaying = false;


    public void beforeEnter(BeforeEnterEvent event) {
        // Retrieve the email parameter from the URL
        System.out.println("1234email: aslkdfjklasjdfklasjdfkjaskldfjkasjfdk");
        QueryParameters queryParameters = event.getLocation().getQueryParameters();
        String nId = queryParameters.getParameters().get("id").get(0);
        this.textId = nId;
        System.out.println("1234email: " + nId);
        System.out.println("this.textId: " + textId);
        loadPodcast();
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
    public PodcastPlayView() {
        likeBtn = new Button("like");
        dislikeBtn = new Button("dislike");
        podcasts = new ArrayList<>();




        h1 = new HorizontalLayout();
        h2 = new HorizontalLayout();
        playButton = new Button("play");
        stopButton = new Button("stop");

        Button nextButton = new Button("Next", new Icon(VaadinIcon.ARROW_RIGHT));
        Button previousButton = new Button("Previous", new Icon(VaadinIcon.ARROW_LEFT));
        leftColumn = new HorizontalLayout();
        leftColumn.setWidth("250px");
        leftColumn.add(previousButton, playButton, stopButton, nextButton, likeBtn, dislikeBtn);

        rightColumn = new VerticalLayout();

        search = new TextField();
        search.setPlaceholder("search");
        search.setSuffixComponent(lumoIcon);
        search.setWidth("1000px");
        StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(dataImage));





        coverImage = new Image(resource, "asdfjalksdfjklasdfj");

        coverImage.setWidth("720px");
        coverImage.setHeight("500px");

        heartIcon = new Icon(VaadinIcon.HEART);
        playPauseButton = new Button(new Icon(VaadinIcon.PLAY));



        loadPodcast();
        loadComment();
        updateUpNextTabContent();

        TabSheet tabSheet = new TabSheet();
        tabSheet.add("UP NEXT", upNext);
        tabSheet.add("Details", Details);
        tabSheet.add("COMMENTS", comment);


        tabSheet.setHeight("500px");
        tabSheet.setWidth("320px");
        h2.add(coverImage, tabSheet);
        rightColumn.add(search, h2, leftColumn);


        playButton.addClickListener(buttonClickEvent -> {
            if (!isPlaying) {
                isPlaying = true;
                CompletableFuture.runAsync(() -> playAudio(dataMusic));
            }
            System.out.println("start" + this.playerAudio);
        });
        stopButton.addClickListener(event -> {
            stopAudio();
            System.out.println("stop" + this.playerAudio);
        });
        likeBtn = new Button("like");
        dislikeBtn = new Button("dislike");
        likeBtn.addClickListener(event -> {
            List<Podcast> info = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getPodcastById/" + this.textId)
                    .retrieve().bodyToMono(new ParameterizedTypeReference<List<Podcast>>() {
                    })
                    .block();

            System.out.println("like bt : " + info);

            Podcast data = new Podcast(info.get(0).get_id(),
                    info.get(0).getTitle(),
                    info.get(0).getArtist(),
                    info.get(0).getDataAudio(),
                    info.get(0).getImage(),
                    info.get(0).getDate(),
                    info.get(0).getSeries(),
                    info.get(0).getLike() + 1,
                    info.get(0).getDislike(),
                    info.get(0).getView() + 1,
                    info.get(0).getEmail(),
                    info.get(0).getUsername()
            );

            Boolean output = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updatePodcast")
                    .bodyValue(data)
                    .retrieve().bodyToMono(Boolean.class).block();

        });
        dislikeBtn.addClickListener(event -> {

            List<Podcast> info = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getPodcastById/" + this.textId)
                    .retrieve().bodyToMono(new ParameterizedTypeReference<List<Podcast>>() {
                    })
                    .block();

            System.out.println("like bt : " + info);

            Podcast data = new Podcast(info.get(0).get_id(),
                    info.get(0).getTitle(),

                    info.get(0).getArtist(),
                    info.get(0).getDataAudio(),
                    info.get(0).getImage(),
                    info.get(0).getDate(),
                    info.get(0).getSeries(),
                    info.get(0).getLike(),
                    info.get(0).getDislike() + 1,
                    info.get(0).getView() + 1,
                    info.get(0).getEmail(),
                    info.get(0).getUsername()
            );

            Boolean output = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updatePodcast")
                    .bodyValue(data)
                    .retrieve().bodyToMono(Boolean.class).block();
        });
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

        h1.add(leftnav, rightColumn);
        rightColumn.getStyle().set("background-color", "#ECE7E3");
        rightColumn.setSizeFull();
        add(h1);
        h1.setSizeFull();
        this.setSizeFull();
        this.setPadding(false);
        nextButton.addClickListener(event -> {

            playNextPodcast();

        });

        previousButton.addClickListener(event -> {
            playPreviousPodcast();
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
        loadPodcast();
        loadComment();
        updateUpNextTabContent();

        rightColumn.add(Crating);
    }

    public void playAudio(byte[] audioData) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(audioData);
            playerAudio = new AdvancedPlayer(inputStream);


            playerAudio.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    isPlaying = false;
                    playNextPodcast();
                }
            });


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

        Image imageProfile = new Image("https://f.ptcdn.info/237/077/000/rbpkfh1n811fuK3R1MbUf-o.jpg", "profile user");
        imageProfile.getStyle().set("border-radius", "50%");
        imageProfile.setHeight("50px");
        imageProfile.setWidth("50px");


        VerticalLayout content = new VerticalLayout();
        Text usernameText = new Text("username - 20-10-2010\n");
        Text descriptionText = new Text("\nNeque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit");

        content.add(usernameText, descriptionText);

        HorizontalLayout h = new HorizontalLayout();
        h.getStyle().set("border-bottom", "1px solid #ccc");
        h.setPadding(false);
        h.add(imageProfile, content);


        comment.add(h);
    }

    public void loadPodcast(){
        System.out.println("this.textId  in loading :  " + this.textId);
        if (this.textId != null){
            this.podcasts = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getPodcast/" + this.textId)
//
                    .retrieve().bodyToMono(new ParameterizedTypeReference<List<Podcast>>() {})
                    .block();
        }else {
            this.podcasts = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getPodcast")
//
                    .retrieve().bodyToMono(new ParameterizedTypeReference<List<Podcast>>() {})
                    .block();
        }


        System.out.println(this.podcasts.get(0).getDataAudio());
        String DataAudioId = this.podcasts.get(0).getDataAudio();
        String imageId = this.podcasts.get(0).getImage();

        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                .maxInMemorySize(5 * 1024 * 1024)) // Set the buffer limit to 5 MB
                        .build());

        byte[] audioData = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/getAudioPodcast/" + DataAudioId)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

        this.dataImage = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/getAudioPodcast/" + imageId)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();


        this.rating = CountRating(this.podcasts.get(0).getTitle(), this.podcasts.get(0).getArtist());
        Crating = new H5("this.rating " + this.rating);
        Crating.getStyle().set("font-size", "50px");
        Crating.getStyle().set("color", "magenta");
        this.dataMusic = audioData;


// Now 'audioData' contains the raw audio data
//        System.out.println("music data: " + Arrays.toString(audioData));
//        System.out.println(this.music);
        System.out.println(this.podcasts.get(0).getTitle());


    }

    private void updateUpNextTabContent() {
        StreamResource placeholderResource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(dataImage));
        listPodcast = new HorizontalLayout();
        Image cover = new Image(podcasts.get(0).getImage(), "cover image");
        cover.getElement().getStyle().set("width", "10px");
        cover.getElement().getStyle().set("height", "10px");
        listPodcast.add(cover,
                new Text(podcasts.get(0).getTitle() + '\n' + podcasts.get(0).getArtist()),
                new Text("Time")
        );
        listPodcast.setWidth("350px");

        if (podcasts != null && !podcasts.isEmpty()) {
            VerticalLayout upNextContent = new VerticalLayout();

            for (Podcast podcast : podcasts) {
                StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(getImageData(podcast.getImage())));
                Image coverImage = new Image(resource, "cover image");
                coverImage.setWidth("80px");
                coverImage.setHeight("80px");

                HorizontalLayout podcastInfo = new HorizontalLayout(
                        coverImage,
                        new Text(podcast.getTitle() + '\n' + podcast.getArtist()),
                        new Text("Time")
                );
                HorizontalLayout podcastEntry = new HorizontalLayout(podcastInfo);
                podcastEntry.setWidth("100%");

                podcastEntry.addClickListener(event -> {
                    currentPodcastIndex = podcasts.indexOf(podcast);
                    stopAudio();
                    playSelectedPodcast();
                    updateUpNextTabContent();
                });

                upNextContent.add(podcastEntry);
            }

            upNext.removeAll();
            upNext.add(upNextContent);
            Details.add(podcasts.get(0).getDate() + '\n');
            Details.add(podcasts.get(0).getTitle() + '\n');
            Details.add("Description" + '\n');
            Details.add(podcasts.get(0).getDescription());
        } else {
            upNext.removeAll();
            upNext.add(new Text("No data"));
        }
    }
    private byte[] getImageData(String imageId) {
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                .maxInMemorySize(5 * 1024 * 1024))
                        .build());

        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/getAudioPodcast/" + imageId)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }
    private void playNextPodcast() {
        if (!podcasts.isEmpty()) {
            if (currentPodcastIndex < podcasts.size() - 1) {
                currentPodcastIndex++;
            } else {
                currentPodcastIndex = 0;
            }
            stopAudio();
            playSelectedPodcast();
            updateUpNextTabContent();
        }
        isPlaying = true;
        System.out.println(currentPodcastIndex);
    }

    private void playPreviousPodcast() {
        if (!podcasts.isEmpty()) {
            if (currentPodcastIndex > 0) {
                currentPodcastIndex--;
            } else {
                currentPodcastIndex = podcasts.size() - 1;
            }
            stopAudio();
            playSelectedPodcast();
            updateUpNextTabContent();
        }
        isPlaying = true;
        System.out.println(currentPodcastIndex);
    }
    private void playSelectedPodcast() {

        if (podcasts != null && !podcasts.isEmpty()) {

            Podcast selectedPodcast = podcasts.get(currentPodcastIndex);


            String selectedPodcastId = selectedPodcast.getDataAudio();
            String selectImageId = selectedPodcast.getImage();
            System.out.println("Selected Podcast ID: " + selectedPodcastId);


            WebClient.Builder webClientBuilder = WebClient.builder()
                    .exchangeStrategies(ExchangeStrategies.builder()
                            .codecs(configurer -> configurer.defaultCodecs()
                                    .maxInMemorySize(10 * 1024 * 1024)) // Set the buffer limit to 10 MB
                            .build());

//            change image cover
//            imageSelected
            System.out.println("image + 1  " + this.dataImage);
            byte[] imageData = getImageData(selectImageId);
            this.dataImage = getImageData(selectImageId);
            updateCoverImage(imageData);

            System.out.println("image + 2  " + this.dataImage);



            byte[] audioData = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8080/getAudioPodcast/" + selectedPodcastId)
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
    private void updateCoverImage(byte[] imageData) {
        StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(imageData));
        coverImage.setSrc(resource);
    }
    public String CountRating(String title, String artist){
        System.out.println("name : " + title);
        System.out.println("school : " + artist);
        String Rating = WebClient.create()
                .get()
                .uri("http://127.0.0.1:5000/podcast/"+title + "/" + artist)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println("rating : " + Rating);
        return Rating;
    }

}
