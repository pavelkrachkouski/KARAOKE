package karaoke;

import java.io.*;
import java.util.Objects;
import java.util.Optional;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import karaoke.database.H2jdbcCreateDemo;
import karaoke.database.Tables;
import karaoke.points.PointsController;


public class Controller
{
    private Integer id;
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


    @FXML
    private ComboBox<String> cbPoints;

    @FXML
    private ImageView imgStart;

    @FXML
    private ImageView imgSettings;

    @FXML
    private ImageView imgClose;

    @FXML
    private ImageView imgCloseTable;

    @FXML
    private ComboBox<Integer> cbTimer;

    @FXML
    private ComboBox<String> cbSounds;

    @FXML
    private TableView<Tables> tvStat;

    @FXML
    private TableColumn<Tables, Integer> colTable;

    @FXML
    private TableColumn<Tables, Integer> colPoints;

    @FXML
    private CheckBox cbStat;

    @FXML
    private Button btnTrash;

    @FXML
    private Button btnTable;

    @FXML
    private TextField tfTable;

    @FXML
    private TextField tfPoints;

    @FXML
    private Button btnEdit;



    @FXML
    void initialize() throws IOException
    {
        H2jdbcCreateDemo.dataBase();
        getSetting();

        imgSettings.setOnMouseClicked(mouseEvent -> openNewScene("settings.fxml", "Настройки"));
        imgStart.setOnMouseClicked(mouseEvent -> showPoints());
        imgClose.setOnMouseClicked(mouseEvent -> clear());

        btnEdit.setOnAction(event -> updateRecord());
        tvStat.setOnMouseClicked(event -> TableClick());
        btnTrash.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Эй!");
            alert.setHeaderText("Ты точно хочешь стереть все данные?");
            alert.setContentText("Подумай еще раз хорошенько :(");
            Optional<ButtonType> option = alert.showAndWait();
            if(option.orElse(null) == ButtonType.OK)
            {
                H2jdbcCreateDemo.truncateTable();
                showTables();
            }
        });
        btnTable.setOnAction(actionEvent -> {
            imgCloseTable.setVisible(true);
            btnTable.setDisable(true);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("finaltable/final.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //PointsController pointsController = loader.getController();
            //pointsController.setPoints(String.valueOf(cbPoints.getValue()));
            //pointsController.setSettings(Integer.parseInt(size1), Integer.parseInt(size2), Integer.parseInt(size3), textcolor);
            //root.setStyle("-fx-background-radius: " + radius + ";" + "-fx-background-color: " + "transparent" + ";" + "-fx-background-insets: 0, 0 1 1 0;");
            Stage stage = new Stage();
            assert root != null;
            Scene scene = new Scene(root, 800, 800, Color.TRANSPARENT);
            stage.setScene(scene);
            //stage.initStyle(StageStyle.TRANSPARENT);
            stage.setX(posxt);
            stage.setY(posyt);
            //stage.setResizable(false);
            stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/microphone.png"))));
            //stage.setAlwaysOnTop(true);
            stage.show();
            //System.out.println(com.sun.glass.ui.Window.getWindows().isEmpty());
            //com.sun.glass.ui.Window.getWindows().get(0).setUndecoratedMoveRectangle(22);
            imgCloseTable.setOnMouseClicked(event -> {
                stage.close();
                imgCloseTable.setVisible(false);
                btnTable.setDisable(false);
            });
        });


        fillComboPoints();
        fillComboTimer();
        fillComboMusic(path);
        showTables();
    }




    private void fillComboPoints()
    {
        ObservableList<String> points = FXCollections.observableArrayList();
        for (int i = 100; i >= 1; i--)
        {
            points.add(String.valueOf(i));
        }
        cbPoints.setItems(points);
    }



    private void fillComboTimer()
    {
        ObservableList<Integer> times = FXCollections.observableArrayList();
        for (int i = 20; i >= 1; i--)
        {
            times.add(i);
        }
        cbTimer.setItems(times);
    }



    private boolean showPoints()
    {
        if (cbTimer.getValue() != null)
        {
            if (cbPoints.getValue() == null)
            {
                showWarningAlert("Не указал количество баллов, будь внимательнее!");
            }
            else if (cbSounds.getValue() == null)
            {
                showWarningAlert("Без фоновой музыки не запущу!");
            }
            else
            {
                if (cbStat.isSelected())
                {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Ну что?");
                    dialog.setHeaderText("Какой столик исполнял?:");
                    dialog.setContentText("Столик:");
                    Optional<String> result = dialog.showAndWait();
                    if (!result.isPresent()) return false;
                    result.ifPresent(task -> {
                        H2jdbcCreateDemo.insertRecord(task, Integer.parseInt(cbPoints.getValue()));
                        showTables();
                        System.out.println(task);
                    });
                }

                File file = new File(path + "/" + cbSounds.getValue());
                Sound sound = new Sound(file);
                //Image image = new Image(new File("/home/pavel/Рабочий стол/KARAOKE/src/karaoke/images/stop.png").toURI().toString(), 200, 200, true, true);
                imgStart.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/stop.png"))));
                imgStart.setDisable(true);
                sound.play();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("points/points.fxml"));
                    Parent root = loader.load();
                    PointsController pointsController = loader.getController();
                    pointsController.setPoints(String.valueOf(cbPoints.getValue()));
                    pointsController.setSettings(Integer.parseInt(size1), Integer.parseInt(size2), Integer.parseInt(size3), textcolor);
                    root.setStyle("-fx-background-radius: " + radius + ";" + "-fx-background-color: " + color + ";" + "-fx-background-insets: 0, 0 1 1 0;");
                    Stage stage = new Stage();
                    Scene scene = new Scene(root, windowSize, windowSize, Color.TRANSPARENT);
                    stage.setScene(scene);
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setX(positionX);
                    stage.setY(positionY);
                    stage.setResizable(false);
                    stage.setAlwaysOnTop(true);
                    stage.show();
                    //заставка запустится на 3 секунды
                    PauseTransition delay = new PauseTransition(Duration.seconds(cbTimer.getValue()));
                    delay.setOnFinished(event -> {
                        stage.close();
                        sound.stop();
                        sound.close();
                        //Image image2 = new Image(new File("/home/pavel/Рабочий стол/KARAOKE/src/karaoke/images/play.png").toURI().toString(), 200, 200, true, true);
                        imgStart.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/play.png"))));
                        imgStart.setDisable(false);
                    });
                    delay.play();
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
            }
        }
        else
        {
            if (cbPoints.getValue() == null) {
                showWarningAlert("Не указал количество баллов, будь внимательнее!");
            } else if (cbSounds.getValue() == null) {
                showWarningAlert("Без фоновой музыки не запущу!");
            }
            else
            {
                if (cbStat.isSelected()) {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Ну что?");
                    dialog.setHeaderText("Какой столик исполнял?:");
                    dialog.setContentText("Столик:");
                    Optional<String> result = dialog.showAndWait();
                    if (!result.isPresent()) return false;
                    result.ifPresent(task -> {
                        H2jdbcCreateDemo.insertRecord(task, Integer.parseInt(cbPoints.getValue()));
                        showTables();
                    });
                }

                File file = new File(path + "/" + cbSounds.getValue());
                Sound sound = new Sound(file);
                //Image image = new Image(new File("/home/pavel/Рабочий стол/KARAOKE/src/karaoke/images/stop.png").toURI().toString(), 200, 200, true, true);
                imgStart.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/stop.png"))));

                sound.play();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("points/points.fxml"));
                    Parent root = loader.load();
                    PointsController pointsController = loader.getController();
                    pointsController.setPoints(String.valueOf(cbPoints.getValue()));
                    pointsController.setSettings(Integer.parseInt(size1), Integer.parseInt(size2), Integer.parseInt(size3), textcolor);
                    root.setStyle("-fx-background-radius: " + radius + ";" + "-fx-background-color: " + color + ";" + "-fx-background-insets: 0, 0 1 1 0;");
                    Stage stage = new Stage();
                    Scene scene = new Scene(root, windowSize, windowSize, Color.TRANSPARENT);
                    stage.setScene(scene);
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setX(positionX);
                    stage.setY(positionY);
                    stage.setResizable(false);
                    stage.setAlwaysOnTop(true);
                    stage.show();

                    imgStart.setOnMouseClicked(mouseEvent -> {
                        stage.close();
                        sound.stop();
                        sound.close();
                        //Image image2 = new Image(new File("/home/pavel/Рабочий стол/KARAOKE/src/karaoke/images/play.png").toURI().toString(), 200, 200, true, true);
                        imgStart.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/play.png"))));
                        imgStart.setOnMouseClicked(mouseEvent1 -> showPoints());
                    });
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
            }
        }
        return true;
    }



    public void getSetting() throws IOException
    {
        File file = new File("settings.txt");
        if (file.exists())
        {
            System.out.println(file.getCanonicalPath() + " существует файл? " + file.exists());
            BufferedReader reader = new BufferedReader(new FileReader("settings.txt"));
            String line;
            while ((line = reader.readLine()) != null)
            {
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
                posyt = Integer.valueOf(settings[11]);
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
        }
    }



    public void updateSetting(int posx, int posy, String music, int winSize, String colorWindow, String radiusWindow, String textColor, String sizeLabel1, String sizeLabel2, String sizeLabel3, int posxT, int posyT)
    {
        positionX = posx;
        positionY = posy;
        path = music;
        windowSize = winSize;
        color = colorWindow;
        radius = radiusWindow;
        textcolor = textColor;
        size1 = sizeLabel1;
        size2 = sizeLabel2;
        size3 = sizeLabel3;
        posxt = posxT;
        posyt = posyT;

        fillComboMusic(path);
    }



    private void showWarningAlert(String str)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Алё, гараж!");
        alert.setHeaderText(null);
        alert.setContentText(str);
        alert.showAndWait();
    }



    public void fillComboMusic(String path)
    {
        ObservableList<String> music = FXCollections.observableArrayList();
        File root = new File(path);
        File[] list = root.listFiles();
        if (list == null)
            return;
        for (File f : list)
        {
            String m = f.getName();
            if (m.endsWith(".wav"))
            {
                System.out.println("Файлы с расширением   : " + m);
                music.add(m);
            }
        }
        cbSounds.setItems(music);
    }



    public void showTables()
    {
        ObservableList<Tables> list = H2jdbcCreateDemo.readRecord();
        colTable.setCellValueFactory(new PropertyValueFactory<>("number"));
        colPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
        tvStat.setItems(list);
    }


    public void TableClick()
    {
        Tables client = tvStat.getSelectionModel().getSelectedItem();
        id = client.getId();
        tfTable.setText(String.valueOf(client.getNumber()));
        tfPoints.setText(String.valueOf(client.getPoints()));
    }


    public void updateRecord()
    {
        H2jdbcCreateDemo.updateRecord(id, tfTable.getText(), Integer.parseInt(tfPoints.getText()));
        tfTable.clear();
        tfPoints.clear();
        showTables();
    }



    public void openNewScene(String window, String title)
    {
        //btnStart.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/wrw.png"))));
        stage.setResizable(false);
        //stage.setAlwaysOnTop(true);
        stage.show();
        //ссылки на контроллеры
        SettingsController children = loader.getController(); //получаем контроллер окна
        children.setParent(this);
    }



    private void clear()
    {
        cbTimer.getSelectionModel().clearSelection();
    }
}


