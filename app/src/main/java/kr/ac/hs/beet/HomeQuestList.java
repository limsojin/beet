package kr.ac.hs.beet;

public class HomeQuestList {

    String questtext;
    int date;

    public String getQuesttext() {
        return questtext;
    }

    public void setQuesttext(String questtext) {
        this.questtext = questtext;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public HomeQuestList(int date, String questtext){
        this.date = date;
        this.questtext = questtext;
    }

}
