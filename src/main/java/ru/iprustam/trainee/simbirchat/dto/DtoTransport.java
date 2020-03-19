package ru.iprustam.trainee.simbirchat.dto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.iprustam.trainee.simbirchat.dto.model.Dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DtoTransport {
    private ModelMapper modelMapper;

    public DtoTransport(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Упаковка entity объектов в dto пакет
     *
     * @param eventType
     * @param entity
     * @param classDto
     * @return
     */
    public DtoPacket entityToDto(String eventType, Object entity, Class<? extends Dto> classDto) {
        Dto dto = getDto(entity, classDto);
        return new DtoPacket(eventType, dto);
    }

    /**
     * Упаковка списка однотипных объектов в dto packet
     *
     * @param eventType
     * @param entities
     * @param classDto
     * @return
     */
    public DtoPacket entitiesToDtoList(String eventType, List<? extends Object> entities, Class<? extends Dto> classDto) {
        List<Dto> dtoList = entities.stream()
                .map(e -> getDto(e, classDto))
                .collect(Collectors.toList());
        return new DtoPacket(eventType, dtoList);
    }

    /**
     * Упаковка разнотипных объектов в dto packet в виде map
     *
     * @param eventType
     * @param entitiesKeys    названия для соответствующих объектов
     * @param entitiesValues  объекты, которые нужно упаковать
     * @param entitiesClasses типы dto, которыми нужно упаковать
     *                        Если value=null, {@link ModelMapper} не будет использован и объект будет
     *                        упакован в том виде, в котором он передан
     * @return
     */
    public DtoPacket entitiesToDtoMap(String eventType,
                                      List<String> entitiesKeys,
                                      List<? extends Object> entitiesValues,
                                      List<? extends Class<? extends Dto>> entitiesClasses) {
        Map<String, Object> dtoMap = new HashMap<>();
        for(int i=0; i<entitiesKeys.size(); i++) {
            Class dtoClass = entitiesClasses.get(i);
            Object dtoObject = entitiesValues.get(i);
            if (dtoClass != null) // использовать ли ModelMapper
                dtoMap.put(entitiesKeys.get(i), getDto(dtoObject, dtoClass));
            else
                dtoMap.put(entitiesKeys.get(i), dtoObject);
        }

        return new DtoPacket(eventType, dtoMap);
    }

    /**
     * Упаковка объекта в dto пакет без использования ModelMapper
     *
     * @param eventType
     * @param obj
     * @return
     */
    public DtoPacket objectToDto(String eventType, Object obj) {
        return new DtoPacket(eventType, obj);
    }

    private Dto getDto(Object entity, Class<? extends Dto> classDto) {
        Dto dto = modelMapper.map(entity, classDto);
        return dto;
    }

}
