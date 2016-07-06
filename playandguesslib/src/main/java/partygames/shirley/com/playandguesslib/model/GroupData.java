package partygames.shirley.com.playandguesslib.model;

/**
 * Created by Administrator on 2016/6/29.
 */
public class GroupData{
    private int type = 1;   //类别
    private String[] words; //词语

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }
}
