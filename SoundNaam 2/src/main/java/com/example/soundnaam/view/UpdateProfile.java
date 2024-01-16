package com.example.soundnaam.view;

import com.example.soundnaam.POJO.Music;
import com.example.soundnaam.POJO.Song;
import com.example.soundnaam.POJO.User;
import com.example.soundnaam.service.AudioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

@Route("updateProfile")
public class UpdateProfile extends AppLayout {
    private AudioRepository audioRepository;
    private H1 textmain;
    private TextField usernameFeild, emailFeild;
    private TextField oldPasswordFeild;
    private TextField newPasswordFeild;
    private Upload  uploadImage;
    private Button update;
    private String audioId, imageId;
    private Tabs tabs;
    private AppLayout nav;
    private VerticalLayout main;

    public UpdateProfile(AudioRepository audioRepository){
        this.audioRepository = audioRepository;

        main = new VerticalLayout();
        textmain = new H1("Update Profile");

        usernameFeild = new TextField("Username");
        usernameFeild.setWidth("30%");
        usernameFeild.getStyle().set("--vaadin-input-field-border-width", "1px");

        oldPasswordFeild = new TextField("Old password");
        oldPasswordFeild.setWidth("30%");
        oldPasswordFeild.getStyle().set("--vaadin-input-field-border-width", "1px");

        newPasswordFeild = new TextField("New password");
        newPasswordFeild.setWidth("30%");
        newPasswordFeild.getStyle().set("--vaadin-input-field-border-width", "1px");

        emailFeild = new TextField("email");
        emailFeild.setWidth("30%");
        emailFeild.getStyle().set("--vaadin-input-field-border-width", "1px");

        uploadImage = new Upload();

        update = new Button("Update");
        update.getStyle().set("background-color", "#FFA62B");

        //left navigator
        tabs = new Tabs();
        // drawer
        tabs.add(createTab(VaadinIcon.HOME, "Home", HomeSong.class),
                createTab(VaadinIcon.BOOKMARK, "Subscriptions", UserProfile.class),
                createTab(VaadinIcon.HEADPHONES, "Podcast", HomePodcast.class),
                createTab(VaadinIcon.PLAY_CIRCLE, "Playlist", UserProfile.class),
                createTab(VaadinIcon.TIME_BACKWARD, "History", HomeSong.class));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setSelectedTab(tabs.getTabAt(0));

        addToDrawer(tabs);

        uploadImage = new Upload(new FileBuffer());
        uploadImage.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        uploadImage.addSucceededListener(event -> {
            InputStream inputStream = ((FileBuffer) uploadImage.getReceiver()).getInputStream();
            saveAudioToMongoDB(event.getFileName(), event.getMIMEType(), inputStream);
        });
        uploadImage.setWidth("30%");
        main.add(textmain, emailFeild, usernameFeild, oldPasswordFeild, newPasswordFeild, uploadImage, update);
        main.setSizeFull();
        main.getStyle().set("background-color", "#F5F4F4");
        main.setAlignItems(FlexComponent.Alignment.CENTER);
        setContent(main);

        loadPage();

        update.addClickListener(event -> {
            System.out.println("submit");
            String key = "token";
            UI.getCurrent().getPage().executeJs("return localStorage.getItem($0)", key)
                    .then(String.class, this::fetchUserData);

//            new Notification(output, 10000).open();
        });




    }

    private void fetchUserData(String token){
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


            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

            String username = usernameFeild.getValue();
            String oldPassword = oldPasswordFeild.getValue();
            String newPassword = newPasswordFeild.getValue();
            String email = emailFeild.getValue();

            String image =imageId;

//
//            if(username == "" || image == null){
//                 username = user.getUsername();
//                 image = user.getImage();


//            }

            if(image == null){
                image = user.getImage();
            }

            if(newPassword == ""){
                newPassword = user.getPassword();
            }



            formData.add("email", email);
            formData.add("oldPassword", oldPassword);
            formData.add("newPassword", newPassword);
            formData.add("username", username);
            formData.add("image", image);

            Map<String, Object> userList = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateUser")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .block();

            System.out.println(userList);



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


    private void loadPage(){

        String key = "token";
        UI.getCurrent().getPage().executeJs("return localStorage.getItem($0)", key)
                .then(String.class, this::loadData);

    }


    public void loadData(String token){
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

            usernameFeild.setValue(user.getUsername());
//            oldPasswordFeild.setValue(user.getPassword());


        } catch (Exception e) {
            // Handle the exception appropriately
            System.err.println("Error fetching user data: " + e.getMessage());
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
/*
        link.setTabIndex(-1);
*/

        return new Tab(link);
    }
}