package fyber.jehandadk.com.data.errors;

/**
 * Created by jehandad.kamal on 1/24/2016.
 */
public class Err {
    private String message;
    private String requestId;
    private String hostId;
    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String Message) {
        this.message = Message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String RequestId) {
        this.requestId = RequestId;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String HostId) {
        this.hostId = HostId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String Code) {
        this.code = Code;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", requestId = " + requestId + ", hostId = " + hostId + ", code = " + code + "]";
    }
}
