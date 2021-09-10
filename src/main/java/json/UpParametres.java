package json;


import java.util.List;

public class UpParametres {
    public String date;
    public List<Parametres> weather;

    public UpParametres(String date, List<Parametres> weather){
        this.date = date;
        this.weather = weather;
    }
}
