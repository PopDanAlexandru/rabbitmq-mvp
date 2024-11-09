package mvp.rabbitmq.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayloadMessage implements Serializable {

    private String content;

    @JsonCreator
    public PayloadMessage(@JsonProperty("content") String content) {
        this.content = content;
    }

}
