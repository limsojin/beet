package kr.ac.hs.beet;

import android.media.Image;
import android.provider.MediaStore;

public class DiaryList {
    String sentence;
    String date;

    public DiaryList(String sentence, String date){
        this.sentence= sentence;
        this.date = date;

    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
