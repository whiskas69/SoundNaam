package com.example.soundnaam.view;

import com.example.soundnaam.POJO.Music;
import com.example.soundnaam.POJO.Song;
import com.example.soundnaam.POJO.User;
import com.example.soundnaam.service.AudioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Route("createSong")
public class CreateSong extends HorizontalLayout {
    private AudioRepository audioRepository;
    private String audioId, imageId;
    private Text audioFile, coverFile;
    private TextField titile, album;
    private TextArea description, lyrics;
    private Button submit;
    private Upload uploadAudio, uploadCover;
    private H1 textmain;
    private boolean audio, image;
    private VerticalLayout main, leftnav;
    private Tabs tabs;


    @Autowired
    public CreateSong(AudioRepository audioRepository) {
        this.setSizeFull();

        audio = false;
        image = false;

        this.audioRepository = audioRepository;

        textmain = new H1("Upload Song");
        titile = new TextField("Title");
        titile.setWidth("30%");
        titile.getStyle().set("--vaadin-input-field-border-width", "1px");

        description = new TextArea("Description");
        description.setWidth("30%");
        description.getStyle().set("--vaadin-input-field-border-width", "1px");

        lyrics = new TextArea("Lyrics");
        lyrics.setWidth("30%");
        lyrics.getStyle().set("--vaadin-input-field-border-width", "1px");

        album = new TextField("Album");
        album.setWidth("30%");
        album.getStyle().set("--vaadin-input-field-border-width", "1px");

        submit = new Button("Upload New Song");
        submit.getStyle().set("background-color", "#FFA62B");

        audioFile = new Text("Audio");
        coverFile = new Text("Cover Image");

        main = new VerticalLayout();
        main.setSizeFull();
        main.getStyle().set("background-color", "#ECE7E3");

        //left navigator
        leftnav = new VerticalLayout();

        Image logoImage = new Image("/images/Logo.png", "Logo");

        tabs = new Tabs();

        tabs.add(createTab(VaadinIcon.HOME, "Home", HomeSong.class),
                createTab(VaadinIcon.BOOKMARK, "Subscriptions", UserProfile.class),
                createTab(VaadinIcon.HEADPHONES, "Podcast", HomePodcast.class),
                createTab(VaadinIcon.PLAY_CIRCLE, "Playlist", UpdateProfile.class),
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

        leftnav.add(logoImage, tabs, createsong, createpodcast, createplaylist,logout);
        leftnav.setWidth("20%");
        leftnav.setHeightFull();
        leftnav.setAlignSelf(Alignment.CENTER, logoImage);
        leftnav.setAlignSelf(Alignment.END, createsong, createpodcast, createplaylist,logout);

        // Set up the Upload component with FileBuffer as the receiver
        uploadAudio = new Upload(new FileBuffer());
        uploadAudio.setAcceptedFileTypes("audio/*");
        uploadAudio.addSucceededListener(event -> {
            audio = true;
            InputStream inputStream = ((FileBuffer) uploadAudio.getReceiver()).getInputStream();
            saveAudioToMongoDB(event.getFileName(), event.getMIMEType(), inputStream);
        });
        uploadAudio.setWidth("30%");

        uploadCover = new Upload(new FileBuffer());
        uploadCover.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        uploadCover.addSucceededListener(event -> {
            image = true;
            InputStream inputStream = ((FileBuffer) uploadCover.getReceiver()).getInputStream();
            saveAudioToMongoDB(event.getFileName(), event.getMIMEType(), inputStream);
        });
        uploadCover.setWidth("30%");

        submit.addClickListener(event -> {
            System.out.println("submit");
            String key = "token";
            UI.getCurrent().getPage().executeJs("return localStorage.getItem($0)", key)
                    .then(String.class, this::fetchUserData);

//            new Notification(output, 10000).open();
        });







        main.add(textmain, titile, description, lyrics, album, audioFile, uploadAudio, coverFile, uploadCover, submit);
        main.setAlignItems(Alignment.CENTER);

        this.add(leftnav, main);

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
            System.out.println(imageId);

            Song data = new Song(null, titile.getValue(), lyrics.getValue(), user.getUsername(), audioId, imageId, String.valueOf(new Date()), album.getValue(), 0, 0, 0, user.getEmail(), user.getUsername());


            if (audio && image){
                Boolean output = WebClient.create()
                        .post()
                        .uri("http://localhost:8080/addSong")
                        .bodyValue(data)
                        .retrieve().bodyToMono(Boolean.class).block();

                System.out.println(output);

                new Notification("success", 10000).open();
            } else {
                new Notification("Don't has image cover or audio", 10000).open();
            }







        } catch (Exception e) {
            // Handle the exception appropriately
            System.err.println("Error fetching user data: " + e.getMessage());
        }
    }



    private void saveAudioToMongoDB(String fileName, String mimeType, InputStream inputStream) {
        byte[] data = new byte[0];
        try {
            data = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Music media = new Music();
        // Set other metadata fields
        media.setData(data);
        media.setFileName(fileName);
        media.setMimeType(mimeType);

        audioRepository.save(media);
        System.out.println(media.getId());
        if (media.getMimeType().equals("audio/mpeg")){
            this.audioId = media.getId();
        }else{
            this.imageId = media.getId();
        }
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

}