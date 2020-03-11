package ru.iprustam.trainee.simbirchat.dto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.iprustam.trainee.simbirchat.dto.model.ChatMessageDto;
import ru.iprustam.trainee.simbirchat.dto.model.ChatRoomDto;
import ru.iprustam.trainee.simbirchat.entity.ChatMessage;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoTransport {
    private ModelMapper modelMapper;

    public DtoTransport(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public DtoPacket chatRoomToDto(String eventType, ChatRoom entity) {
        ChatRoomDto dto = getRoomDto(entity);
        return new DtoPacket(eventType, dto);
    }

    public DtoPacket chatRoomsToDto(String eventType, List<ChatRoom> entity) {
        List<ChatRoomDto> dtoList = entity.stream()
                .map(e -> getRoomDto(e))
                .collect(Collectors.toList());
        return new DtoPacket(eventType, dtoList);
    }

    private ChatRoomDto getRoomDto(ChatRoom chatRoom) {
        ChatRoomDto dto = modelMapper.map(chatRoom, ChatRoomDto.class);
        return dto;
    }

    public DtoPacket chatMessageToDto(String eventType, ChatMessage entity) {
        ChatMessageDto dto = getMessageDto(entity);
        return new DtoPacket(eventType, dto);
    }

    public DtoPacket chatMessagesToDto(String eventType, List<ChatMessage> entity) {
        List<ChatMessageDto> dtoList = entity.stream()
                .map(e -> getMessageDto(e))
                .collect(Collectors.toList());
        return new DtoPacket(eventType, dtoList);
    }

    private ChatMessageDto getMessageDto(ChatMessage chatMessage) {
        ChatMessageDto dto = modelMapper.map(chatMessage, ChatMessageDto.class);
        return dto;
    }
}
