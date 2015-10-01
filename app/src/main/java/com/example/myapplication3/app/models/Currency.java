package com.example.myapplication3.app.models;

/**
 * Created by omar on 9/22/15.
 */
public class Currency {

    private String ask;
    private String bid;
    private String nameCurrency;
    private String id;
    private String previousAck = "0";
    private String previousBid = "0";


    public String getPreviousAck() {
        return previousAck;
    }

    public void setPreviousAck(String previousAck) {
        this.previousAck = previousAck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPreviousBid() {
        return previousBid;
    }

    public void setPreviousBid(String previousBid) {
        this.previousBid = previousBid;
    }

    public String getNameCurrency() {
        return nameCurrency;
    }

    public void setNameCurrency(String name) {
        this.nameCurrency = name;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }


    public class CurrencySmall {

        private String ask;
        private String bid;

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getAsk() {
            return ask;
        }

        public void setAsk(String ask) {
            this.ask = ask;
        }

    }
}
