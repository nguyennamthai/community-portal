package thai.service.dto.mapper;

import thai.domain.Message;
import thai.service.dto.MessageDto;

public class MessageMapper {
    private MessageMapper() { }

    public static MessageDto convertMessageToMessageDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setContent(message.getContent());
        messageDto.setModified(message.getModified());
        return messageDto;
    }

    public static Message convertMessageDtoToMessage(MessageDto messageDto) {
        Message message = new Message();
        message.setId(messageDto.getId());
        message.setContent(messageDto.getContent());
        message.setModified(messageDto.getModified());
        return message;
    }
}
