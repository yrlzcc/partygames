package partygames.shirley.com.playandguesslib.model;

import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 */
public class GuessResult {
    private int result = 0;                            //返回结果
    private List<GroupData> grouplist = null;          //分类列表

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<GroupData> getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(List<GroupData> groupList) {
        this.grouplist = groupList;
    }
}
