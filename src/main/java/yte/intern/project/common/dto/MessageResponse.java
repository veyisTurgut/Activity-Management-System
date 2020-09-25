package yte.intern.project.common.dto;

import lombok.AllArgsConstructor;
import yte.intern.project.common.enums.MessageType;

@AllArgsConstructor
public class MessageResponse {
    public final String message;
    public final MessageType messageType;
}
