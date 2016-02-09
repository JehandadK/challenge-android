
package fyber.jehandadk.com.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class TimeToPayout {

    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("readable")
    @Expose
    private String readable;

    /**
     * @return The amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @param amount The amount
     */
    public void setAmount(int amount) {
        this.amount = amount;
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
