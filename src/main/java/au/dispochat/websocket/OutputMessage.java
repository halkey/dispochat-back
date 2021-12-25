package au.dispochat.websocket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OutputMessage {
    private String from;
    private String text;
    private String time;

    public OutputMessage(String from, String text, String time) {
        this.setFrom(from);
        this.setText(text);
        this.setTime(time);
    }
}
