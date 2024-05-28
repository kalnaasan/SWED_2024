package edu.fra.uas.websitemonitor.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class MessageResponse {
    private String message;
    private Object data;
    private LocalDateTime timestamp = LocalDateTime.now();

    public MessageResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
