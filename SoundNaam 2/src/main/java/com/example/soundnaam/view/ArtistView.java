package com.example.soundnaam.view;


import com.example.soundnaam.POJO.ContentMembership;
import com.example.soundnaam.POJO.Song;
import com.example.soundnaam.POJO.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoIcon;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@PageTitle("Artist Song")
@Route(value = "artist")
public class ArtistView extends VerticalLayout implements BeforeEnterObserver{
    private String email;
    private  String artist;

    private String image;

    private String role;
    public void beforeEnter(BeforeEnterEvent event) {
        QueryParameters queryParameters = event.getLocation().getQueryParameters();

        // Retrieve the email and artist parameters from the URL
        List<String> emailValues = queryParameters.getParameters().get("email");
        List<String> artistValues = queryParameters.getParameters().get("artist");
        List<String> imageValues = queryParameters.getParameters().get("image");
        List<String> roleValues = queryParameters.getParameters().get("role");


        // Check if parameters are present and retrieve their values
        email = (emailValues != null && !emailValues.isEmpty()) ? emailValues.get(0) : null;
        artist = (artistValues != null && !artistValues.isEmpty()) ? artistValues.get(0) : null;
        image = (imageValues != null && !imageValues.isEmpty()) ? imageValues.get(0) : null;
        role = (roleValues != null && !roleValues.isEmpty()) ? roleValues.get(0) : null;

        System.out.println("EmailArtist : " + email);
        System.out.println("NameArtist : " + artist);
        System.out.println("Image : " + image);
        System.out.println("Role : " + role);


        // Use the email and artist parameters as needed in your view
        // ...
    }
    // attribute
    private ContentMembership content;
    private List<ContentMembership> contents;

    // ui
    Icon lumoIcon = LumoIcon.SEARCH.create();
    Icon like = VaadinIcon.HEART_O.create();
    Icon heart = VaadinIcon.HEART_O.create();
    Icon textcomment = VaadinIcon.COMMENT_ELLIPSIS_O.create();
    Icon left = VaadinIcon.CHEVRON_CIRCLE_LEFT.create();
    Icon right = VaadinIcon.CHEVRON_CIRCLE_RIGHT.create();
    Icon play = VaadinIcon.PLAY_CIRCLE.create();
    Icon stop = VaadinIcon.STOP.create();

    private Div divPost, mysong, myalbum, mymember;
    private TextArea mypost;
    private VerticalLayout Third, mepost, Sidebar, Main, Artist, verisong, verialbum, createpost, space, textinfobottom;
    private HorizontalLayout Mymepost, secondmain, SearchandAvatar, iconlike, iconcomment, icon, Secornd, tab, horisong,
            horialbum, Me, mainbottom, iconbottom;
    private Text date, mename2, numberlike, numbercomment, album, timesong, artistalbum, countalbum, mename;
    private H6 textpost, artistbottom;
    private Avatar avatar, avatar3, avatar2;
    private TextField search;
    private H1 name;
    private Button follow, subscription, Postpost;
    private Image coverSong, coverAlbum, imagebottom;
    private H4 songname, albumname, namebottom;
    private FlexLayout mybuttonpost;
    private Tabs tabs;

    private List<Song>  song;
    TabSheet tabSheet;

    // Div MySong = new Div();

    public ArtistView() {
        song = new ArrayList<>();
        loading();
        loadPage();
        secondmain = new HorizontalLayout();
        secondmain.setSizeFull();
        secondmain.setPadding(false);
        secondmain.setMargin(false);

        this.setSizeFull();
        this.setPadding(false);
        this.setMargin(false);

        // nav ข้าง ๆ
        Sidebar = new VerticalLayout();
        Sidebar.setPadding(false);
        Sidebar.setMargin(false);

        //logo
        Image logoImage = new Image("/images/Logo.png", "Logo");

        //left navigator
        tabs = new Tabs();
        // drawer
        tabs.add(createTab(VaadinIcon.HOME, "Home", UpdateProfile.class),
                createTab(VaadinIcon.BOOKMARK, "Subscriptions", UpdateProfile.class),
                createTab(VaadinIcon.HEADPHONES, "Podcast", UpdateProfile.class),
                createTab(VaadinIcon.PLAY_CIRCLE, "Playlist", UpdateProfile.class),
                createTab(VaadinIcon.TIME_BACKWARD, "History", UpdateProfile.class));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setSelectedTab(tabs.getTabAt(0));
        tabs.getStyle().set("color", "black");

        Sidebar.add(logoImage, tabs);
        Sidebar.setAlignSelf(Alignment.CENTER, logoImage);
        Sidebar.setWidth(20, Unit.PERCENTAGE);
        Sidebar.getStyle().set("Height" , "100%");
        Sidebar.getStyle().set("background-color", "#5F9DB2");

        // main
        Main = new VerticalLayout();
        Main.setPadding(false);
        Main.setSpacing(false);

        // cover and name artist
        Artist = new VerticalLayout();
        Artist.setWidthFull();
        Artist.setHeight(100, Unit.PERCENTAGE);
        Artist.getElement().getStyle().set("background-image",
                "url('https://centaur-wp.s3.eu-central-1.amazonaws.com/creativereview/prod/content/uploads/2018/10/13.jpg?auto=compress,format&q=60&w=1200&h=1217')");

        Main.add(Artist);

        // Search and Avatar
        SearchandAvatar = new HorizontalLayout();
        SearchandAvatar.setWidthFull();

        avatar = new Avatar();
        avatar.setImage("https://th.bing.com/th/id/OIP.Z4UUr7rXPvOvoALaQfeEnAHaFj?pid=ImgDet&rs=1");

        search = new TextField();
        search.setPlaceholder("search");
        search.setSuffixComponent(lumoIcon);
        search.setWidth("1000px");
        search.getStyle().set("background-color", "white");
        SearchandAvatar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        SearchandAvatar.add(search, avatar);

        // Text and Sub
        Secornd = new HorizontalLayout();
        Secornd.setSizeFull();
        name = new H1("Artist Name");
        name.getStyle().set("color", "white");
        name.getStyle().set("font-size", "100px");

        follow = new Button("follow");
        follow.getStyle().set("background-color", "white");
        follow.getStyle().set("color", "black");
        follow.getStyle().set("border-radius", "50px");
        subscription = new Button("subscription");
        subscription.getStyle().set("background-color", "white");
        subscription.getStyle().set("color", "black");
        subscription.getStyle().set("border-radius", "50px");

        Secornd.add(name, follow, subscription);
        Secornd.setAlignSelf(Alignment.END, name, follow, subscription);

        Artist.add(SearchandAvatar, Secornd);

        // song album
        Third = new VerticalLayout();
        Third.setSizeFull();
        Third.getStyle().set("background-color", "#ECE7E3");
        Main.add(Third);

        // Tabsheet
        tab = new HorizontalLayout();
        tab.setSizeFull();

        // mock song
        mysong = new Div();
//        horisong = new HorizontalLayout();
//        horisong.setSizeFull();
//        horisong.setAlignItems(Alignment.CENTER);
//        coverSong = new Image(
//                "https://cdn.discordapp.com/attachments/979676845228318740/1163524636257030254/Awesome_Heaven_Officials_Blessing_Wallpapers_-_WallpaperAccess.png?ex=655b9342&is=65491e42&hm=06cdf1826b3dae56d43b59f478413c03080ce1c8331551e9e087c85f4bd990cb&",
//                "coverSong");
//        coverSong.setHeight(100, Unit.PIXELS);
//        coverSong.setWidth(100, Unit.PIXELS);
//
//        verisong = new VerticalLayout();
//        songname = new H4("name");
//        album = new Text("album");
//        verisong.add(songname, album);
//
//        timesong = new Text("3:40");
//
//        horisong.add(coverSong, verisong, timesong);
//
//        mysong = new Div();
//        mysong.add(horisong);

        // mock album
        myalbum = new Div();
//        horialbum = new HorizontalLayout();
//        horialbum.setSizeFull();
//        horialbum.setAlignItems(Alignment.CENTER);
//        coverAlbum = new Image(
//                "https://cdn.discordapp.com/attachments/979676845228318740/1163524636257030254/Awesome_Heaven_Officials_Blessing_Wallpapers_-_WallpaperAccess.png?ex=655b9342&is=65491e42&hm=06cdf1826b3dae56d43b59f478413c03080ce1c8331551e9e087c85f4bd990cb&",
//                "coverSong");
//        coverAlbum.setHeight(100, Unit.PIXELS);
//        coverAlbum.setWidth(100, Unit.PIXELS);
//
//        verialbum = new VerticalLayout();
//        albumname = new H4("name");
//        artistalbum = new Text("artist");
//        verialbum.add(albumname, artistalbum);
//
//        countalbum = new Text("50 Songs");
//
//        horialbum.add(coverAlbum, verialbum, countalbum);
//        myalbum.add(horialbum);

        // membership
        mymember = new Div();
        // createpost
        createpost = new VerticalLayout();
        createpost.setWidthFull();
        createpost.setHeightFull();
        createpost.getStyle().set("background-color", "white");

        Me = new HorizontalLayout();
        mename = new Text("Folk");
        mypost = new TextArea();
        avatar2 = new Avatar();
        avatar2.setImage("https://th.bing.com/th/id/OIP.Z4UUr7rXPvOvoALaQfeEnAHaFj?pid=ImgDet&rs=1");

        Me.add(avatar2, mename);
        Me.setAlignItems(Alignment.CENTER);

        mypost.setPlaceholder("อยากโพสอะไร");
        mypost.setSizeFull();

        Postpost = new Button("POST");
        Postpost.getStyle().set("background-color", "#F2AA49");
        Postpost.addClickListener(event -> submitPost());
        mybuttonpost = new FlexLayout(Postpost);
        mybuttonpost.setJustifyContentMode(JustifyContentMode.END);
        mybuttonpost.setSizeFull();
        createpost.add(Me, mypost, mybuttonpost);

        // Space
        space = new VerticalLayout();

        // post
        mepost = new VerticalLayout();
        mepost.setSizeFull();
        mepost.getStyle().set("background-color", "white");

        Mymepost = new HorizontalLayout();
        date = new Text("110 ปีที่แล้ว");

        mename2 = new Text("namae");
        avatar3 = new Avatar();
        avatar3.setImage("https://th.bing.com/th/id/OIP.Z4UUr7rXPvOvoALaQfeEnAHaFj?pid=ImgDet&rs=1");

        Mymepost.add(avatar3, mename2, date);
        Mymepost.setAlignItems(Alignment.CENTER);

        textpost = new H6("มีเรื่องอยากจะบอกแต่นึกไม่ออกจะบอกอะไร");

        iconlike = new HorizontalLayout();
        numberlike = new Text("1.1k");
        iconlike.add(like, numberlike);

        iconcomment = new HorizontalLayout();
        numbercomment = new Text("1k");
        iconcomment.add(textcomment, numbercomment);

        icon = new HorizontalLayout();
        icon.add(iconlike, iconcomment);

        post();
        mepost.add(Mymepost, textpost, icon);
        mymember.add(createpost, space, divPost);

        tabSheet = new TabSheet();
        tabSheet.add("SONG", mysong);
        tabSheet.add("ALBUM", myalbum);
        tabSheet.add("MEMBERSHIP", mymember);
        tabSheet.setWidthFull();

        tab.add(tabSheet);

        Third.add(tab);

//        // tab bottom
//        mainbottom = new HorizontalLayout();
//        mainbottom.setWidthFull();
//        mainbottom.setHeight(8, Unit.PERCENTAGE);
//        mainbottom.getStyle().set("background-color", "#82C0CC");
//
//        imagebottom = new Image("https://th.bing.com/th/id/OIP.Z4UUr7rXPvOvoALaQfeEnAHaFj?pid=ImgDet&rs=1","cover");
//        imagebottom.setWidth(60, Unit.PIXELS);
//        imagebottom.setHeight(60, Unit.PIXELS);
//        imagebottom.getStyle().set("margin-left", "50px");
//        imagebottom.getStyle().set("margin-top", "10px");
//        imagebottom.getStyle().set("border-radius", "5px");
//
//        textinfobottom = new VerticalLayout();
//        namebottom = new H4("name");
//        artistbottom = new H6("artist");
//        textinfobottom.add(namebottom, artistbottom);
//        textinfobottom.setWidth("15%");
//
//        iconbottom = new HorizontalLayout();
//        iconbottom.setWidth("40%");
//
//        iconbottom.add(left, stop, right);
//        iconbottom.setJustifyContentMode(JustifyContentMode.CENTER);
//
//        heart.getStyle().set("margin-top", "25px");
//        iconbottom.getStyle().set("margin-top", "25px");
//        iconbottom.getStyle().set("margin-left", "50px");
//
//        mainbottom.add(imagebottom, textinfobottom, heart, iconbottom);
//
        secondmain.add(Sidebar, Main);

        // main add
        this.add(secondmain);
    }

    public void post() {
        divPost = new Div();
        if (contents != null && !contents.isEmpty()) {
            mepost = new VerticalLayout();
            mepost.setSizeFull();
            mepost.getStyle().set("background-color", "white");
            mepost.getStyle().set("margin-top", "10px");
            for (ContentMembership content : contents) {

                Mymepost = new HorizontalLayout();
                date = new Text("");
                date.setText(String.valueOf(content.getDate()));

                mename2 = new Text("");
                mename2.setText(content.getArtist());
                avatar3 = new Avatar();
                avatar3.setImage("https://th.bing.com/th/id/OIP.Z4UUr7rXPvOvoALaQfeEnAHaFj?pid=ImgDet&rs=1");




                Mymepost.add(avatar3, mename2, date);
                Mymepost.setAlignItems(Alignment.CENTER);

                textpost = new H6("");
                textpost.setText(content.getContent());

                textpost.addClickListener(event ->{
                    System.out.println(content.getContent());
                    UI.getCurrent().navigate(ArtistView2.class);

                });


                iconlike = new HorizontalLayout();
                numberlike = new Text("1.1k");
                numberlike.setText(String.valueOf(content.getLike()));
                iconlike.add(VaadinIcon.HEART_O.create(), numberlike);

                icon = new HorizontalLayout();
                icon.add(iconlike);

                mepost.add(Mymepost, textpost, icon);

            }
        } else {
            mepost.add(new Text("ไม่มีจ้า"));

        }
        divPost.add(mepost);



        // mymember.add(createpost,space, mepost); for add to tab
    }

    public List<ContentMembership> loading() {
        this.contents = WebClient.create()
                .get()
                .uri("http://localhost:8080/getContent")
                .retrieve().bodyToMono(new ParameterizedTypeReference<List<ContentMembership>>() {
                })
                .block();
        System.out.println("content is " + this.contents);
        return this.contents;
    }







    public void submitPost() {

            String key = "token";
            UI.getCurrent().getPage().executeJs("return localStorage.getItem($0)", key)
                    .then(String.class, this::subPost);






    }

    private boolean subPost(String token){
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




            System.out.println("submit post");
            content = new ContentMembership(mypost.getValue(), user.getUsername(), user.getEmail(), new Date(), 0);

            Boolean output = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addContent")
                    .bodyValue(content)
                    .retrieve().bodyToMono(Boolean.class).block();
            mypost.setValue("");

            return output;
        } catch (Exception e) {
            // Handle the exception appropriately
            System.err.println("Error fetching user data: " + e.getMessage());
            return false;
        }

    }
    // public boolean updatePost(){
    // System.out.println("submit post");
    // content = new ContentMembership(mypost.getValue(), "artistName", "email", new
    // Date(), 0);
    // Boolean output = WebClient.create()
    // .post()
    // .uri("http://localhost:8080/updateContent")
    // .bodyValue(content)
    // .retrieve().bodyToMono(Boolean.class).block();
    //
    // return output;
    // }

    public boolean deletePost() {
        System.out.println("submit post");
        // this mypost is change get data from loop
        content = new ContentMembership(mypost.getValue(), "artistName", "email", new Date(), 0);
        Boolean output = WebClient.create()
                .post()
                .uri("http://localhost:8080/deleteContent")
                .bodyValue(content)
                .retrieve().bodyToMono(Boolean.class).block();
        return output;
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




            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());

            name.setText(artist);
            mename.setText(user.getUsername());


            getSongByEmail(email);
//
            String uri = image;
            if(!email.equals(user.getEmail())){
                createpost.getStyle().set("display", "none");
            }



//            Artist.getElement().getStyle().set("background-image", "url('" + uri  + "')");
//            avatar.setImage(uri);



            if(image.length() == 24) {
                WebClient.Builder webClientBuilder = WebClient.builder()
                        .exchangeStrategies(ExchangeStrategies.builder()
                                .codecs(configurer -> configurer.defaultCodecs()
                                        .maxInMemorySize(10 * 1024 * 1024)) // Set the buffer limit to 5 MB
                                .build());

                byte[] dataImage = webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8080/getAudioSong/" + uri)
                        .accept(MediaType.APPLICATION_OCTET_STREAM)
                        .retrieve()
                        .bodyToMono(byte[].class)
                        .block();

//                StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(dataImage));
//                String imageUrl = new BrowserWindowOpener(resource).getURL();
                Artist.getElement().getStyle().set("background-image", "url('data:image/jpeg;base64," + Base64.getEncoder().encodeToString(dataImage) + "')");
                Artist.getElement().getStyle().set("background-size", "cover"); // Optional: Set background size

                String dataUrl = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(dataImage);
                avatar.setImage(dataUrl);
                avatar2.setImage(dataUrl);
                avatar3.setImage(dataUrl);
            }
            else{

                Artist.getElement().getStyle().set("background-image", "url('" + uri  + "')");
                Artist.getElement().getStyle().set("background-size", "cover"); // Optional: Set background size
                avatar.setImage(uri);
                avatar2.setImage(uri);
                avatar3.setImage(uri);
            }





        } catch (Exception e) {
            // Handle the exception appropriately
            System.err.println("Error fetching user data: " + e.getMessage());
        }
    }



    private void getSongByEmail(String email){
        System.out.println("getsongByEmail :  " + email);
        try{
            this.song =  WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getSongByEmail/" + email)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Song>>() {})
                    .block();


            System.out.println(song);




            if (song != null) {
                VerticalLayout verticalLayoutsong = new VerticalLayout();
                VerticalLayout verticalLayoutalbum = new VerticalLayout();

                for (Song singleSong : song) {
                    HorizontalLayout horisong = new HorizontalLayout();
                    horisong.setSizeFull();
                    horisong.setAlignItems(Alignment.CENTER);

                    HorizontalLayout horialbum = new HorizontalLayout();
                    horialbum.setSizeFull();
                    horialbum.setAlignItems(Alignment.CENTER);

                    System.out.println(singleSong);
                    Image coverSong = new Image("https://cdn.discordapp.com/attachments/979676845228318740/1163524636257030254/Awesome_Heaven_Officials_Blessing_Wallpapers_-_WallpaperAccess.png?ex=655b9342&is=65491e42&hm=06cdf1826b3dae56d43b59f478413c03080ce1c8331551e9e087c85f4bd990cb&"
                            , "coverSong");
                    coverSong.setHeight(100, Unit.PIXELS);
                    coverSong.setWidth(100, Unit.PIXELS);
                    coverSong.getStyle().set("border-radius", "10px");

                    VerticalLayout verisong = new VerticalLayout();
                    H4 songname = new H4(singleSong.getTitle());
                    Text album = new Text(singleSong.getAlbum());

                    Image coverAlbum = new Image("https://cdn.discordapp.com/attachments/979676845228318740/1163524636257030254/Awesome_Heaven_Officials_Blessing_Wallpapers_-_WallpaperAccess.png?ex=655b9342&is=65491e42&hm=06cdf1826b3dae56d43b59f478413c03080ce1c8331551e9e087c85f4bd990cb&"
                            , "coverSong");
                    coverAlbum.setHeight(100, Unit.PIXELS);
                    coverAlbum.setWidth(100, Unit.PIXELS);
                    coverAlbum.getStyle().set("border-radius", "10px");


                    VerticalLayout verialbum = new VerticalLayout();
                    H4 albumname = new H4(singleSong.getAlbum());
                    Text artistalbum = new Text(singleSong.getUsername());
                    verialbum.add(albumname, artistalbum);

                    Text countalbum = new Text("50 Songs");

                    horialbum.add(coverAlbum, verialbum, countalbum);


                    verisong.add(songname, album);

                    Text timesong = new Text("3:40");

                    horisong.add(coverSong, verisong, timesong);

                    verticalLayoutsong.add(horisong);
                    verticalLayoutalbum.add(horialbum);
                    // Perform any other operations with the individual song
                }

                verticalLayoutsong.setSizeFull();
                verticalLayoutalbum.setSizeFull();
                mysong.add(verticalLayoutsong);
                myalbum.add(verticalLayoutalbum);

            } else {
                System.out.println("No songs found for the specified email");
            }
        }catch(Exception e){
            System.out.println(e);
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