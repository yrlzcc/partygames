package partygames.shirley.com.playandguesslib.model;

import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 */
public class GroupData{
    private int type = 1;   //类别
    private List<String> words; //词语

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}
