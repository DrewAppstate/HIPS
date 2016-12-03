package drew.hiddeninplainsight;

/**
 * Created by drew on 11/15/16.
 */
public class MsgBody {
    public static volatile String msgBody = "";

    public void updateMsgBody(String newMsgBody) {
        msgBody = newMsgBody;
    }

    public String getMsgBody() { return msgBody; }
}
