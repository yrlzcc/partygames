package partygames.shirley.com.playandguesslib;

import java.util.ArrayList;
import java.util.List;

import partygames.shirley.com.playandguesslib.model.GuessResult;
import partygames.shirley.com.playandguesslib.model.WordState;

/**
 * Created by Administrator on 2016/7/1.
 */
public class GBaseData {
    private static GBaseData instance = null;
    public GuessResult guessResult = null;
    private List<WordState> wordStateList = null;
    public static GBaseData getInstance(){
        if(instance == null){
            instance = new GBaseData();
        }
        return instance;
    }

    private GBaseData(){
        wordStateList = new ArrayList<WordState>();
    }

    /**
     * 清空列表
     */
    public void clear(){
        wordStateList.clear();
    }

    /**
     * 增加词语
     * @param wordState
     */
    public void addWord(WordState wordState){
        wordStateList.add(wordState);
    }

    public List<WordState> getWordStateList() {
        return wordStateList;
    }
}
