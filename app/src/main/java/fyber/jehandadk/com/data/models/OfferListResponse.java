package fyber.jehandadk.com.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class OfferListResponse {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("pages")
    @Expose
    private int pages;
    @SerializedName("information")
    @Expose
    private Information information;
    @SerializedName("offers")
    @Expose
    private List<Offer> offers = new ArrayList<Offer>();

    /**
     * @return The code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return The pages
     */
    public int getPages() {
        return pages;
    }

    /**
     * @param pages The pages
     */
    public void setPages(int pages) {
        this.pages = pages;
    }

    /**
     * @return The information
     */
    public Information getInformation() {
        return information;
    }

    /**
     * @param information The information
     */
    public void setInformation(Information information) {
        this.information = information;
    }

    /**
     * @return The offers
     */
    public List<Offer> getOffers() {
        return offers;
    }

    /**
     * @param offers The offers
     */
    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

}