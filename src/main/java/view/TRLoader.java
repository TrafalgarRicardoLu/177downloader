package view;
import controller.ComicDownloader;
import entity.ComicInfo;
import entity.ComicList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import utils.ConfigHelper;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class TRLoader extends Application {

    private ComicList comicList = ComicList.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = new URL(ConfigHelper.getIndexUrl());
        ArrayList<ComicInfo> comicInfoList = comicList.initComicInfoList(url);

        ObservableList comicList = FXCollections.observableArrayList(comicInfoList);

        ListView comicListView = new ListView();
        comicListView.setItems(comicList);
        comicListView.setCellFactory(new Callback<ListView<ComicInfo>, ListCell<ComicInfo>>() {
            @Override
            public ListCell<ComicInfo> call(ListView<ComicInfo> param) {
                return new ComicCell();
            }
        });

        HBox root = new HBox();
        root.getChildren().add(comicListView);
        comicListView.setPrefSize(600,1000);

        Scene scene = new Scene(root, 600, 1000);

        primaryStage.setTitle("TRLoader");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                ComicDownloader.closeFlag=false;
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ComicDownloader comicDownloader = new ComicDownloader();
        Thread download = new Thread(comicDownloader);
        download.start();
        launch(args);

    }
}
