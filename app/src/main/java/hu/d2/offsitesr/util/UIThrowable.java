package hu.d2.offsitesr.util;

/**
 * Created by csabinko on 2017.09.15..
 */

public class UIThrowable extends Throwable {

    private int messageId;

    public UIThrowable(int messageId) {
        this.messageId = messageId;
    }

    public int getMessageId() {
        return messageId;
    }
}
