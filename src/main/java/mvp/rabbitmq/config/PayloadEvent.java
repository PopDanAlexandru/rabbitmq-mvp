package mvp.rabbitmq.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayloadEvent implements Serializable {

    private PayloadMessage payloadMessage;
    private long timestamp;

    @JsonCreator
    public PayloadEvent(@JsonProperty("payloadMessage") PayloadMessage payloadMessage) {
        this.payloadMessage = payloadMessage;
        this.timestamp = System.currentTimeMillis();    // add creation time
    }

}
