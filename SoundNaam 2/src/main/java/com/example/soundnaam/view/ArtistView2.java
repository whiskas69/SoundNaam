package com.example.soundnaam.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;

@PageTitle("Membership comment")
@Route(value = "artist2")
public class ArtistView2 extends HorizontalLayout {
    Icon lumoIcon = LumoIcon.SEARCH.create();
    Icon like = VaadinIcon.HEART_O.create();
    Icon textcomment = VaadinIcon.COMMENT_ELLIPSIS_O.create();

//    Div MySong = new Div();

    public ArtistView2(){
        this.setSizeFull();
        this.setPadding(false);
        this.setMargin(false);

        //nav ข้าง ๆ
        VerticalLayout Sidebar = new VerticalLayout();
        Sidebar.setWidth(20, Unit.PERCENTAGE);
        Sidebar.setHeightFull();
        Sidebar.getStyle().set("background-color", "#5F9DB2");

        //main
        VerticalLayout Main = new VerticalLayout();
        Main.getStyle().set("background-color", "red");
        Main.setPadding(false);
        Main.setSpacing(false);

        //cover and name artist
        VerticalLayout Artist = new VerticalLayout();
        Artist.setWidthFull();
        Artist.setHeight(100, Unit.PERCENTAGE);
        Artist.getElement().getStyle().set("background-image", "url('https://centaur-wp.s3.eu-central-1.amazonaws.com/creativereview/prod/content/uploads/2018/10/13.jpg?auto=compress,format&q=60&w=1200&h=1217')");

        Main.add(Artist);

        //Search and Avatar
        HorizontalLayout SearchandAvatar = new HorizontalLayout();
        SearchandAvatar.setWidthFull();

        Avatar avatar = new Avatar();
        avatar.setImage("https://th.bing.com/th/id/OIP.Z4UUr7rXPvOvoALaQfeEnAHaFj?pid=ImgDet&rs=1");

        TextField search = new TextField();
        search.setPlaceholder("search");
        search.setSuffixComponent(lumoIcon);
        search.setWidth("1000px");
        search.getStyle().set("background-color", "white");
        SearchandAvatar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        SearchandAvatar.add(search, avatar);

        //Text and Sub
        HorizontalLayout Secornd = new HorizontalLayout();
        Secornd.setSizeFull();
        H1 name = new H1("Artist Name");

        Button follow = new Button("follow");
        Button subscription = new Button("subscription");

        Secornd.add(name, follow, subscription);
        Secornd.setAlignSelf(Alignment.END, name, follow, subscription);

        Artist.add(SearchandAvatar, Secornd);

        //song album
        VerticalLayout Third = new VerticalLayout();
        Third.setSizeFull();
        Third.getStyle().set("background-color", "#ECE7E3");
        Main.add(Third);

        //Tabsheet
        HorizontalLayout tab = new HorizontalLayout();
        tab.setSizeFull();

        //mock song
        HorizontalLayout horisong = new HorizontalLayout();
        horisong.setSizeFull();
        horisong.setAlignItems(Alignment.CENTER);
        Image coverSong = new Image("https://cdn.discordapp.com/attachments/979676845228318740/1163524636257030254/Awesome_Heaven_Officials_Blessing_Wallpapers_-_WallpaperAccess.png?ex=655b9342&is=65491e42&hm=06cdf1826b3dae56d43b59f478413c03080ce1c8331551e9e087c85f4bd990cb&"
                , "coverSong");
        coverSong.setHeight(100, Unit.PIXELS);
        coverSong.setWidth(100, Unit.PIXELS);

        VerticalLayout verisong = new VerticalLayout();
        H4 songname = new H4("name");
        Text album = new Text("album");
        verisong.add(songname, album);

        Text timesong = new Text("3:40");

        horisong.add(coverSong, verisong, timesong);

        Div mysong = new Div();
        mysong.add(horisong);

        //mock album
        Div myalbum = new Div();
        HorizontalLayout horialbum = new HorizontalLayout();
        horialbum.setSizeFull();
        horialbum.setAlignItems(Alignment.CENTER);
        Image coverAlbum = new Image("https://cdn.discordapp.com/attachments/979676845228318740/1163524636257030254/Awesome_Heaven_Officials_Blessing_Wallpapers_-_WallpaperAccess.png?ex=655b9342&is=65491e42&hm=06cdf1826b3dae56d43b59f478413c03080ce1c8331551e9e087c85f4bd990cb&"
                , "coverSong");
        coverAlbum.setHeight(100, Unit.PIXELS);
        coverAlbum.setWidth(100, Unit.PIXELS);

        VerticalLayout verialbum = new VerticalLayout();
        H4 albumname = new H4("name");
        Text artistalbum = new Text("artist");
        verialbum.add(albumname, artistalbum);

        Text countalbum = new Text("50 Songs");

        horialbum.add(coverAlbum, verialbum, countalbum);
        myalbum.add(horialbum);

        //membership
        Div mymember = new Div();

        //space
        VerticalLayout space  = new VerticalLayout();

        //numbercomment
        VerticalLayout spacecomment = new VerticalLayout();
        H2 numcomment = new H2("ความคิดเห็น 200 รายการ");
        spacecomment.add(numcomment);

        //post
        VerticalLayout mepost = new VerticalLayout();
        mepost.setSizeFull();
        mepost.getStyle().set("background-color", "white");

        HorizontalLayout Mymepost = new HorizontalLayout();
        Text date = new Text("110 ปีที่แล้ว");

        Text mename2 = new Text("namae");
        Avatar avatar3 = new Avatar();
        avatar3.setImage("https://th.bing.com/th/id/OIP.Z4UUr7rXPvOvoALaQfeEnAHaFj?pid=ImgDet&rs=1");

        Mymepost.add(avatar3, mename2, date);
        Mymepost.setAlignItems(Alignment.CENTER);

        H6 textpost = new H6("มีเรื่องอยากจะบอกแต่นึกไม่ออกจะบอกอะไร");

        HorizontalLayout iconlike = new HorizontalLayout();
        Text numberlike = new Text("1.1k");
        iconlike.add(like, numberlike);

        mepost.add(Mymepost, textpost, iconlike);

        //create comment
        HorizontalLayout creatcomment = new HorizontalLayout();
        creatcomment.setSizeFull();
        TextField postcomment = new TextField();
        postcomment.setSizeFull();
        Avatar artistavatar = new Avatar();
        artistavatar.setImage("https://th.bing.com/th/id/OIP.Z4UUr7rXPvOvoALaQfeEnAHaFj?pid=ImgDet&rs=1");

        creatcomment.setAlignItems(Alignment.CENTER);

        postcomment.setPlaceholder("อยากโพสอะไร");
        postcomment.setSizeFull();

        Button Postpost = new Button("POST");
        Postpost.getStyle().set("background-color", "#F2AA49");

        creatcomment.add(artistavatar, postcomment, Postpost);

        //comment
        VerticalLayout viewcomment = new VerticalLayout();
        viewcomment.setSizeFull();
        viewcomment.getStyle().set("background-color", "white");

        HorizontalLayout yoursub = new HorizontalLayout();
        Avatar avatarsub = new Avatar();
        avatarsub.setImage("https://th.bing.com/th/id/OIP.Z4UUr7rXPvOvoALaQfeEnAHaFj?pid=ImgDet&rs=1");
        H5 subname = new H5("name");
        Text subdate = new Text("1 ปี");
        yoursub.add(avatarsub, subname, subdate);
        yoursub.setAlignItems(Alignment.CENTER);

        H6 textcomment = new H6("นึกออกยังหลายปีละเนี่ย");

        viewcomment.add(yoursub, textcomment);

        //add div
        mymember.add(mepost, spacecomment, creatcomment, space, viewcomment);

        TabSheet tabSheet = new TabSheet();
        tabSheet.add("SONG", mysong);
        tabSheet.add("ALBUM", myalbum);
        tabSheet.add("MEMBERSHIP",mymember);
        tabSheet.setWidthFull();

        tab.add(tabSheet);

        Third.add(tab);

        this.add(Sidebar, Main);
    }
}
