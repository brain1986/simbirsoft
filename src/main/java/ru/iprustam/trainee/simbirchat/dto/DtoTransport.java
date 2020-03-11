package ru.iprustam.trainee.simbirchat.controller.dto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.iprustam.trainee.simbirchat.controller.dto.model.ChatRoomDto;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoTransport {
    private ModelMapper modelMapper;

    public DtoTransport(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public DtoPacket entityToDto(String eventType, ChatRoom entity) {
        ChatRoomDto dto = modelMapper.map(entity, ChatRoomDto.class);
        //dto.setRoomId(entity.getRoomId());
        //dto.setRoomName(entity.getRoomName());
        return new DtoPacket(eventType, dto);
    }

    public DtoPacket entityToDto(String eventType, List<ChatRoom> entity) {
        List<ChatRoomDto> dtoList = entity.stream().map(e -> {
            ChatRoomDto dto = modelMapper.map(e, ChatRoomDto.class);
            return dto;
        }).collect(Collectors.toList());
        return new DtoPacket(eventType, dtoList);
    }


}
