package partygames.shirley.com.playandguesslib.model;

/**
 * Created by Administrator on 2016/7/4.
 */
public class WordState {
    private String word;   //词语
    private boolean isright; //是否正确

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isright() {
        return isright;
    }

    public void setIsright(boolean isright) {
        this.isright = isright;
    }
}
