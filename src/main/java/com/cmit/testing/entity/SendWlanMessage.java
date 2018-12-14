package com.cmit.testing.entity;

public class SendWlanMessage {
   private String receiverPhone;
   private String senderPhone;
   private String sendOrderText;
   private String confirmOrderString;
   private String sendConfirmOrderText;
   private String confirmOrderResulString;
   private String unsubscribeText;
   private String unsubscribeConfirmResulString;
   private int type;

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSendOrderText() {
        return sendOrderText;
    }

    public void setSendOrderText(String sendOrderText) {
        this.sendOrderText = sendOrderText;
    }

    public String getConfirmOrderString() {
        return confirmOrderString;
    }

    public void setConfirmOrderString(String confirmOrderString) {
        this.confirmOrderString = confirmOrderString;
    }

    public String getSendConfirmOrderText() {
        return sendConfirmOrderText;
    }

    public void setSendConfirmOrderText(String sendConfirmOrderText) {
        this.sendConfirmOrderText = sendConfirmOrderText;
    }

    public String getConfirmOrderResulString() {
        return confirmOrderResulString;
    }

    public void setConfirmOrderResulString(String confirmOrderResulString) {
        this.confirmOrderResulString = confirmOrderResulString;
    }

    public String getUnsubscribeText() {
        return unsubscribeText;
    }

    public void setUnsubscribeText(String unsubscribeText) {
        this.unsubscribeText = unsubscribeText;
    }

    public String getUnsubscribeConfirmResulString() {
        return unsubscribeConfirmResulString;
    }

    public void setUnsubscribeConfirmResulString(String unsubscribeConfirmResulString) {
        this.unsubscribeConfirmResulString = unsubscribeConfirmResulString;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
