package karaoke.database;

public class Tables
{
    private final int id;
    private final String number;
    private final int points;

    public Tables(int id, String number, int points)
    {
        this.id = id;
        this.number = number;
        this.points = points;
    }

    public String getNumber() {
        return number;
    }

    public int getPoints() {
        return points;
    }

    public int getId() {
        return id;
    }
}
