package com.example.historyservice6.view;

import com.example.historyservice6.core.data.HistoryEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Route(value = "history")
public class HistoryViewNotUse extends VerticalLayout {
    private Text headtext, songname, artistname, test;
    private TextField searchbar;
    private SideNav drawer, library;
    private HorizontalLayout hori1, horizontalLayout, albumlist;
    private VerticalLayout verti1, layoutcol;
    private DrawerToggle toggle;
    private Tabs tabs;
    private Avatar user;
    private H3 albums, musics;
    private H2 hstr;
    private Image img;
    private List<HistoryEntity> histories;
    private List<Song> songs;

    public HistoryViewNotUse() {
        histories = new ArrayList<>();
        songs = new ArrayList<>();
        hstr = new H2("History");
        this.add(hstr);
        loadHistory();
    }
//-------------------เพื่อนไม่ได้เอาไฟล์viewไปรวม--------------------------

    //---------------------ต้องใช้อันนี้จากไวน์------------------------------
    private void loadPage() {
        String key = "token";
        UI.getCurrent().getPage().executeJs("return localStorage.getItem($0)", key)
                .then(String.class, this::fetchUserData);
    }

    private void fetchUserData(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        if (token == null) {
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

            ConvertOperators.Convert JSON string to User object using Jackson
            User user = objectMapper.readValue(jsonResponse, User.class);

            Store the username in a class variable


            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());

            name.setText(user.getUsername());
            profileImage.setSrc(user.getImage());
            profileImg.setSrc(user.getImage());


        } catch (Exception e) {
            Handle the exception appropriately
            System.err.println("Error fetching user data: " + e.getMessage());
        }
    }

    private void logout() {
        // Remove token from localStorage
        UI.getCurrent().getPage().executeJs("localStorage.removeItem($0)", "token");

        // Redirect to the login page or perform other logout actions
        UI.getCurrent().navigate(MainView.class);
    }
    public void loadHistory() {
        this.histories = WebClient.create()
                .get()
                .uri("http://localhost:8082/history-service/gethistory")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<HistoryEntity>>() {})
                .block();
        for (int i = 0; i < this.histories.size(); i++) {
            System.out.println(this.histories.get(i).getSongId());
            if (this.histories.get(i).getEmail().equals(user.getEmail())) {
                String name = this.histories.get(i).getSongName();
                this.songs = WebClient.create()
                        .get()
                        .uri("http://localhost:8080/getSong")
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<Song>>() {})
                        .block();
                for (int j = 0; j < this.songs.size(); j++) {
//                    System.out.println(this.histories.get(i).getSongId());
                    if (this.songs.get(j).getTitle().equals(name)) {
                        String artist = this.songs.get(j).getArtist();
                        String songImg = this.songs.get(j).getImage();
                        HorizontalLayout listSong = new HorizontalLayout();
                        Image coverSong = new Image(songImg, "song");
                        coverSong.setWidth("100px");
                        VerticalLayout veriSong = new VerticalLayout();
                        H4 songName = new H4("");
                        Text txtartist = new Text("");
                        songName.setText("SongName: " + name);
                        txtartist.setText("artist: " + artist);
                        veriSong.add(songName, txtartist);
                        listSong.add(coverSong, veriSong);
                        this.add(listSong);
                    } else {
                        System.out.println("wtf2");
                    }
                }
            } else {
                System.out.println("wtf");
            }
        }
    }

}
