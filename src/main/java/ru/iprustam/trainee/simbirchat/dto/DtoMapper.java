package ru.iprustam.trainee.simbirchat.dto;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.iprustam.trainee.simbirchat.entity.ChatMessage;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    ChatMessageDto msgToDto(ChatMessage source);

    Set<ChatMessageDto> msgToDto(Set<ChatMessage> source);

    List<ChatMessageDto> msgToDto(List<ChatMessage> source);

    @AfterMapping
    default void setRoomId(ChatMessage chatMessage, @MappingTarget ChatMessageDto chatMessageDto) {
        chatMessageDto.setRoomId(chatMessage.getChatRoom().getRoomId());
    }

    ChatRoomDto roomToDto(ChatRoom source);

    Set<ChatRoomDto> roomToDto(Set<ChatRoom> source);

    List<ChatRoomDto> roomToDto(List<ChatRoom> source);

    ChatUserDto userToDto(ChatUser source);

    Set<ChatUserDto> userToDto(Set<ChatUser> source);

    List<ChatUserDto> userToDto(List<ChatUser> source);
}
