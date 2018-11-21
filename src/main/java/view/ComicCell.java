package view;

import controller.ComicDownloader;
import entity.ComicInfo;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ComicCell extends ListCell<ComicInfo> {
    Button downloadButton = new Button("Download");
    Label comicName = null;
    Image comicCoverImage = null;
    ImageView comicCover = null;

    @Override
    protected void updateItem(ComicInfo item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            comicName = new Label(item.getComicName());
            comicName.setWrapText(true);
            comicName.setMaxWidth(300);

            comicCoverImage = new Image(item.getComicCover());
            comicCover = new ImageView(comicCoverImage);
            comicCover.setFitWidth(250);
            comicCover.setFitHeight(350);
            comicCover.setCache(true);
            comicCover.setCacheHint(CacheHint.SPEED);

            downloadButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (!ComicDownloader.downloadQueue.contains(item)) {
                        ComicDownloader.downloadQueue.add(item);
                        downloadButton.setText("Downloading");
                    }
                }
            });


            VBox leftView = new VBox();

            StackPane buttonCenter = new StackPane();
            buttonCenter.setMinWidth(300);
            downloadButton.setAlignment(Pos.CENTER);
            buttonCenter.getChildren().add(downloadButton);

            leftView.setMaxSize(300, 350);
            leftView.getChildren().addAll(comicName, buttonCenter);

            HBox root = new HBox();
            root.setMaxSize(600, 350);
            root.getChildren().addAll(comicCover, leftView);

            setGraphic(root);

            System.gc();
        }
    }
}