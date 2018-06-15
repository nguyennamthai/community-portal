package thai.service.dto.mapper;

import org.springframework.stereotype.Component;
import thai.domain.Message;
import thai.service.dto.MessageDto;

@Component
public class MessageMapper {
    public MessageDto convertMessageToMessageDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setContent(message.getContent());
        messageDto.setModified(message.getModified());
        return messageDto;
    }

    public Message convertMessageDtoToMessage(MessageDto messageDto) {
        Message message = new Message();
        message.setId(messageDto.getId());
        message.setContent(messageDto.getContent());
        message.setModified(messageDto.getModified());
        return message;
    }
}
