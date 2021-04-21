package cz.uhk.controllers;

import cz.uhk.HSLColor;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    private ImageView imageView;

    @FXML
    private Slider sliderHue;

    @FXML
    private Slider sliderSaturation;

    @FXML
    private Slider sliderLightness;

    @FXML
    private Button btnOpenImage;

    HSLColor hslColor = new HSLColor(0, 0, 0);

    public void openImage() {
        List<String> imageTypes = new ArrayList<>();
        imageTypes.add("*.jpg");
        imageTypes.add("*.jpeg");
        imageTypes.add("*.png");

        FileChooser fc = new FileChooser();
        fc.setTitle("Open Image");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image files", imageTypes));
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            Image selectedImage = new Image(selectedFile.toURI().toString());
            imageView.setImage(selectedImage);
        }
    }

    public void applyAdjustments() {
        Image image = imageView.getImage();

        double height = image.getHeight();
        double width = image.getWidth();

        BufferedImage newImg = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < height; y++) {
            for (int x = 1; x < width; x++) {

                Color color = image.getPixelReader().getColor(x, y);

                //podminka 360

                double newHue = color.getHue() + hslColor.getHue();
                double newSaturation = color.getSaturation() + hslColor.getSaturation();
                double newBrightness = color.getBrightness() + hslColor.getLightness();

                java.awt.Color myRGBColor = java.awt.Color.getHSBColor((float) newHue, (float) newSaturation, (float) newBrightness);


                newImg.setRGB(x, y, myRGBColor.getRGB());
            }
        }
        Image newImage = SwingFXUtils.toFXImage(newImg, null);
        imageView.setImage(newImage);
    }


    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    private void handleDrop(DragEvent event) throws FileNotFoundException {
        List<File> files = event.getDragboard().getFiles();     //????
        Image image = new Image(new FileInputStream(files.get(0)));
        imageView.setImage(image);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sliderHue.valueProperty().addListener((observableValue, number, t1) -> {
            double hueValue = sliderHue.getValue();
            hslColor.setHue(hueValue);
            applyAdjustments();
        });

        sliderSaturation.valueProperty().addListener((observableValue, number, t1) -> {
            double saturationValue = sliderSaturation.getValue();
            hslColor.setSaturation(saturationValue);
            applyAdjustments();
        });

        sliderLightness.valueProperty().addListener((observableValue, number, t1) -> {
            double lightnessValue = sliderLightness.getValue();
            hslColor.setLightness(lightnessValue);
            applyAdjustments();
        });

    }
}
