package com.example.soundnaam.view;

import com.example.soundnaam.POJO.Playlist;
import com.example.soundnaam.POJO.Song;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Route(value = "playlist")
public class PlaylistView extends VerticalLayout implements BeforeEnterObserver {
    private List<Playlist> playlists;
    private List<Song> songs;
    private String playlistNamePage;
    Div playlistSong = new Div();

    private Playlist playName;
    TextField nameField;
    RadioButtonGroup<String> status;

    private String _id;
    private String email;
    private String username;
    private String playlistName;
    private String coverPl;
    private String statusPl;
    private Date date;
    private List[] song;

    public void beforeEnter(BeforeEnterEvent event) {
        // Retrieve the email parameter from the URL
        QueryParameters queryParameters = event.getLocation().getQueryParameters();
        playlistNamePage = queryParameters.getParameters().get("playlistName").get(0);

        System.out.println("1234playlistName: "+ playlistNamePage);
        loadPlaylist();
    }

    public PlaylistView() {
        playlists = new ArrayList<>();
        songs = new ArrayList<>();
    }
    public void loadPlaylist(){
        this.playName = WebClient.create().get()
                .uri("http://localhost:8080/getNamePlaylist/"+ this.playlistNamePage)
                .retrieve()
                .bodyToMono(Playlist.class)
                .block();
        System.out.println("11209394934949" + playName);
        System.out.println("112eee2342343222222" + this.playName.getPlaylistName());

        //headPlaylist
        HorizontalLayout hori1 = new HorizontalLayout();
        Image cover = new Image(playName.getCover(), "coverPlaylist");
        cover.setHeight(200, Unit.PIXELS);
        cover.setWidth(200, Unit.PIXELS);

        VerticalLayout veri = new VerticalLayout();
        H1 title = new H1(playName.getPlaylistName());
        H4 status = new H4((playName.getStatus()));

        HorizontalLayout hori2 =new HorizontalLayout();
        Text numSong = new Text("50 Songs");
        Text time = new Text("50 minutes");
        hori2.add(numSong, new Text("  "), time);

        HorizontalLayout hori3 =new HorizontalLayout();
        Button play = new Button("play");
        Button editPlay = new Button("editPlaylist");
        hori3.add(play, editPlay);

        veri.add(title, status, hori2, hori3);
        hori1.add(cover, veri);


        if (songs != null && !songs.isEmpty()) {
            this.songs = this.playName.getSong();
            //songPlaylist
            HorizontalLayout listSong = new HorizontalLayout();
            Image coverSong = new Image(songs.get(0).getImage(), "cover image");
            coverSong.setWidth("100px");

            VerticalLayout veriSong = new VerticalLayout();
            H4 songName = new H4(songs.get(0).getTitle());
            Text artist = new Text(songs.get(0).getArtist());
            veriSong.add(songName, artist);

            Text timeSong = new Text("3:40");

            listSong.setSizeFull();
            listSong.setAlignItems(Alignment.CENTER);
            listSong.add(coverSong, veriSong, timeSong);

            System.out.println("Song:  " + playName.getSong());

            VerticalLayout allSongs = new VerticalLayout();
            for (Song song : songs) {
                Image coverImage = new Image(song.getImage(), "cover image");
                coverImage.setWidth("80px");

                HorizontalLayout listSongs = new HorizontalLayout();
                coverImage.setWidth("100px");

                VerticalLayout veriSongs = new VerticalLayout();
                H4 songNames = new H4(songs.get(0).getTitle());
                Text artists = new Text(songs.get(0).getArtist());
                veriSongs.add(songNames, artists);

                Text timeSongs = new Text("3:40");

                listSongs.setSizeFull();
                listSongs.setAlignItems(Alignment.CENTER);
                listSongs.add(coverImage, veriSongs, timeSongs);

                allSongs.add(listSongs);
            }
            playlistSong.add(allSongs);
            playlistSong.setSizeFull();
        }
        else {
            playlistSong.removeAll();
            playlistSong.add(new Text("No data"));
        }
        // Show UI
        this.add(hori1, playlistSong);

        //POPUP EditPlaylist
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add note");

        VerticalLayout dialogLayout = createDialogLayout(dialog);
        dialog.add(dialogLayout);
        dialog.setHeaderTitle("Edit Playlist");

        Button closeButton = new Button(new Icon("lumo", "cross"),
                (e) -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getHeader().add(closeButton);

        editPlay.addClickListener(e -> dialog.open());
    }
    //POPUP EditPlaylist
    private VerticalLayout createDialogLayout(Dialog dialog) {
        System.out.println("--------------------------------------------------------------------------------------------------------------");
        System.out.println("0099999999"+this.playlists);

        nameField = new TextField("Name",
                "Playlist name");
//        nameField.setReadOnly(true);
        nameField.getStyle().set("padding-top", "0");

        status = new RadioButtonGroup<>("Status", "Private", "Public");
        status.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);

        System.out.println(this.username);
        // Move the following code before or after the return statement
        this.playName = WebClient.create().get().uri("http://localhost:8080/getNamePlaylist/"+ this.playlistNamePage)
                .retrieve()
                .bodyToMono(Playlist.class)
                .block();
        System.out.println("112" + playName);
        System.out.println("112eee" + this.playName.getPlaylistName());

        if (this.playName != null) {
            this._id = this.playName.get_id();
            this.email = this.playName.getEmail();
            this.username = this.playName.getUsername();
            this.nameField.setValue(this.playName.getPlaylistName());
            this.coverPl = this.playName.getCover();
            this.status.setValue(this.playName.getStatus());
            this.date = this.playName.getDate();
            this.songs = this.playName.getSong();
        }
//        emailField.setReadOnly(true);
        Button update = new Button("Update");
        Button delete = new Button("Delete");
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);

        VerticalLayout fieldLayout = new VerticalLayout(nameField, status);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "300px").set("max-width", "100%");
        dialog.getFooter().add(delete, update);

        update.addClickListener(event -> {
            System.out.println("--------------------------------------------------------------------------------------------------------------");
            System.out.println("123456"+ this.playName);
            System.out.println("pppped,wemfiwe:  "+ nameField.getValue());

            Playlist data = new Playlist(this.playName.get_id(), this.playName.getEmail(), this.playName.getUsername(), this.nameField.getValue(), this.playName.getCover(), this.status.getValue(),this.playName.getDate(),this.playName.getSong() );
            Boolean output = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updatePlaylist")
                    .bodyValue(data)
                    .retrieve().bodyToMono(Boolean.class).block();

            System.out.println("Piiiiiiiiii"+output);
            String text = output ? "Added" : "something not found";
            new Notification(text, 5000).open();
            System.out.println("W)WWWWW"+ data);
            dialog.close();
            UI.getCurrent().navigate(UserProfile.class);
        });

        delete.addClickListener(evevt -> {
            Playlist data = new Playlist(this.playName.get_id(), this.playName.getEmail(), this.playName.getUsername(), this.nameField.getValue(), this.playName.getCover(), this.status.getValue(),this.playName.getDate(),this.playName.getSong() );
            boolean status = WebClient.create().post().uri("http://localhost:8080/deletePlaylist")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(data)
                    .retrieve().bodyToMono(boolean.class).block();
            String text = status ? "Deleted" : "something not found";
            new Notification(text, 5000).open();
            dialog.close();
            UI.getCurrent().navigate(UserProfile.class);
        });

        return fieldLayout;
    }
}