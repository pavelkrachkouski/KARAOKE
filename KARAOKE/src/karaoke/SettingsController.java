package karaoke;

import java.io.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;

public class SettingsController
{

    private Integer positionX;
    private Integer positionY;
    private Integer posxt;
    private Integer posyt;
    private String path;
    private Integer windowSize;
    private String color;
    private String radius;
    private String size1;
    private String size2;
    private String size3;
    private String textcolor;

    private Controller controller;

    @FXML
    private TextField tfPosx;

    @FXML
    private TextField tfPosy;

    @FXML
    private TextField tfSize;

    @FXML
    private TextField tfRadius;

    @FXML
    private ColorPicker cpWindowColor;

    @FXML
    private TextField tfSize1;

    @FXML
    private TextField tfSize2;

    @FXML
    private TextField tfSize3;

    @FXML
    private ColorPicker cpTextColor;

    @FXML
    private Button btnConfirmSetting;

    @FXML
    private TextField tfPath;

    @FXML
    private ImageView imgChange;

    @FXML
    private TextField tfPosxT;

    @FXML
    private TextField tfPosyT;

    @FXML
    void initialize() throws IOException
    {
        getSetting();
        cpWindowColor.setValue(Color.web(color));
        cpTextColor.setValue(Color.web(textcolor));
        imgChange.setOnMouseClicked(event -> changeDirectory());

        btnConfirmSetting.setOnAction(event -> {
            try {
                updateSetting();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
                loader.load();
                //Parent root = loader.load();
                //     Controller controller = loader.getController();
                controller.updateSetting(positionX, positionY, path, windowSize, color, radius, textcolor, size1, size2, size3, posxt, posyt);
                //controller.fillComboMusic(path);
                /*Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();*/
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Отлично!");
                alert.setHeaderText(null);
                alert.setContentText("Новые настройки сохранены!");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }



    public void getSetting() throws IOException
    {
        File file = new File("settings.txt");
        if (file.exists()) {
            System.out.println(file.getCanonicalPath() + " существует файл? " + file.exists());
            BufferedReader reader = new BufferedReader(new FileReader("settings.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                String[] settings = line.split("-");
                positionX = Integer.valueOf(settings[0]);
                positionY = Integer.valueOf(settings[1]);
                path = settings[2];
                windowSize = Integer.valueOf(settings[3]);
                color = settings[4];
                radius = settings[5];
                size1 = settings[6];
                size2 = settings[7];
                size3 = settings[8];
                textcolor = settings[9];
                posxt = Integer.valueOf(settings[10]);
                posyt = Integer.valueOf(settings[10]);



                tfPosx.setText(String.valueOf(positionX));
                tfPosy.setText(String.valueOf(positionY));
                tfPath.setText(path);
                tfSize.setText(String.valueOf(windowSize));
                tfRadius.setText(radius);
                tfSize1.setText(size1);
                tfSize2.setText(size2);
                tfSize3.setText(size3);
                cpWindowColor.setValue(Color.web(color));
                cpTextColor.setValue(Color.web(textcolor));
                tfPosxT.setText(String.valueOf(posxt));
                tfPosyT.setText(String.valueOf(posyt));
            }
        }
        else
        {
            System.out.println("Такого файла нет, нужно создать файл с настройками по умолчанию.");
            FileWriter writer = new FileWriter("settings.txt", false);
            String text = "500-300-/home/pavel/Рабочий стол/KARAOKE/src/karaoke/music-400-#13ee13ff-50-40-90-40-#f20a0aff-100-100";
            writer.write(text);
            writer.flush();
            String[] settings = text.split("-");
            positionX = Integer.valueOf(settings[0]);
            positionY = Integer.valueOf(settings[1]);
            path = settings[2];
            windowSize = Integer.valueOf(settings[3]);
            color = settings[4];
            radius = settings[5];
            size1 = settings[6];
            size2 = settings[7];
            size3 = settings[8];
            textcolor = settings[9];
            posxt = Integer.valueOf(settings[10]);
            posyt = Integer.valueOf(settings[11]);

            tfPosx.setText(String.valueOf(positionX));
            tfPosy.setText(String.valueOf(positionY));
            tfPath.setText(path);
            tfSize.setText(String.valueOf(windowSize));
            tfRadius.setText(radius);
            tfSize1.setText(size1);
            tfSize2.setText(size2);
            tfSize3.setText(size3);
            cpWindowColor.setValue(Color.web(color));
            cpTextColor.setValue(Color.web(textcolor));
            tfPosxT.setText(String.valueOf(posxt));
            tfPosyT.setText(String.valueOf(posyt));
        }
    }



    public void updateSetting() throws IOException
    {
        positionX = Integer.valueOf(tfPosx.getText());
        positionY = Integer.valueOf(tfPosy.getText());
        path = tfPath.getText();
        windowSize = Integer.valueOf(tfSize.getText());
        color = "#" + Integer.toHexString(cpWindowColor.getValue().hashCode());
        radius = tfRadius.getText();
        textcolor = "#" + Integer.toHexString(cpTextColor.getValue().hashCode());
        size1 = tfSize1.getText();
        size2 = tfSize2.getText();
        size3 = tfSize3.getText();
        posxt = Integer.valueOf(tfPosxT.getText());
        posyt = Integer.valueOf(tfPosyT.getText());
        FileWriter writer = new FileWriter("settings.txt", false);
        String text = positionX + "-" + positionY + "-" + path + "-" + windowSize + "-" + color + "-" + radius + "-" + size1 + "-" + size2 + "-"  + size3 + "-" + textcolor + "-" + posxt + "-" + posyt;
        writer.write(text);
        writer.flush();
    }



    public void changeDirectory()
    {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(btnConfirmSetting.getContextMenu());
        System.out.println(selectedDirectory);
        if (selectedDirectory == null)
        {
            tfPath.setText(path);
        }
        else
        {
            tfPath.setText(String.valueOf(selectedDirectory).replace("\\", "/"));
            path = String.valueOf(selectedDirectory);
        }
    }



    public void setParent(Controller controller)
    {
        this.controller = controller;
    }
}

