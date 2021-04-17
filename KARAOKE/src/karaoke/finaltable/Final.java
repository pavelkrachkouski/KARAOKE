package karaoke.finaltable;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import karaoke.database.H2jdbcCreateDemo;
import karaoke.database.Tables;

public class Final
{

    @FXML
    private TableView<Tables> tvFinal;

    @FXML
    private TableColumn<Tables, String> colTable;

    @FXML
    private TableColumn<Tables, Integer> colPoints;

    @FXML
    void initialize()
    {

        //System.out.println(com.sun.glass.ui.Window.getWindows().isEmpty());
        showTables();
    }

    public void showTables()
    {
        ObservableList<Tables> list = H2jdbcCreateDemo.readRecordGroup();
        colTable.setCellValueFactory(new PropertyValueFactory<>("number"));
        colPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
        tvFinal.setItems(list);
    }
}





