package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import controller.ComicDownloader;
import entity.ComicInfo;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ComicCell extends ListCell<ComicInfo> {
    ComicDownloader comicDownloader = new ComicDownloader();
    Button downloadButton = new Button("Download");
    Label comicName = null;
    Image comicCoverImage = null;
    ImageView comicCover = null;

    @Override
    protected void updateItem(ComicInfo item, boolean empty) {
        if (item != null) {
            comicName = new Label(item.getComicName());
            comicCoverImage = new Image(item.getComicCover());
            comicCover = new ImageView(comicCoverImage);
            comicCover.setFitWidth(250);
            comicCover.setFitHeight(350);
            downloadButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        comicDownloader.downloadByPage(item);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            VBox leftView = new VBox();

            StackPane buttonCenter = new StackPane();
            buttonCenter.getChildren().add(downloadButton);

            leftView.getChildren().addAll(comicName, buttonCenter);

            HBox root = new HBox();
            root.getChildren().addAll(comicCover, leftView);

            setGraphic(root);
        }
    }
}