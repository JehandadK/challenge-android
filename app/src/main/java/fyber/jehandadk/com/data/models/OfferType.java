
package fyber.jehandadk.com.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class OfferType {

    @SerializedName("offer_type_id")
    @Expose
    private int offerTypeId;
    @SerializedName("readable")
    @Expose
    private String readable;

    /**
     * @return The offerTypeId
     */
    public int getOfferTypeId() {
        return offerTypeId;
    }

    /**
     * @param offerTypeId The offer_type_id
     */
    public void setOfferTypeId(int offerTypeId) {
        this.offerTypeId = offerTypeId;
    }

    /**
     * @return The readable
     */
    public String getReadable() {
        return readable;
    }

    /**
     * @param readable The readable
     */
    public void setReadable(String readable) {
        this.readable = readable;
    }

}
