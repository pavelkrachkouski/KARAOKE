package karaoke.points;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PointsController
{

    @FXML
    private Label lb1;

    @FXML
    private Label lbPoints;

    @FXML
    private Label lbPointsText;

    @FXML
    void initialize()
    {

    }



    public void checkPoints(int i)
    {
        if (i == 1)
        {
            lbPointsText.setText(lbPointsText.getText());
        }
        else if (i > 1 && i < 5)
        {
            lbPointsText.setText(lbPointsText.getText() + "а");
        }
        else if (i > 4 && i < 21)
        {
            lbPointsText.setText(lbPointsText.getText() + "ов");
        }

        else if (i%10 > 1 && i%10 < 5 && i > 20)
        {
            lbPointsText.setText(lbPointsText.getText() + "а");
        }
        else if (i%10 > 4 && i%10< 11 && i >20 || i%10 == 0)
        {
            lbPointsText.setText(lbPointsText.getText() + "ов");
        }
    }



    public void setPoints(String a)
    {
        lbPoints.setText(a);
        checkPoints(Integer.parseInt(a));
    }



    public void setSettings(int size1, int size2, int size3, String color)
    {
        lb1.setStyle("-fx-text-fill: " + color + "; -fx-font-size: " + size1 + "; -fx-font-family: \"Arial Black\";");
        lbPoints.setStyle("-fx-text-fill: " + color + "; -fx-font-size: " + size2 + "; -fx-font-family: \"Arial Black\";");
        lbPointsText.setStyle("-fx-text-fill: " + color + "; -fx-font-size: " + size3 + "; -fx-font-family: \"Arial Black\";");
    }
}
