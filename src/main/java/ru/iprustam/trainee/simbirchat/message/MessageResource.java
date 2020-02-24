package ru.iprustam.trainee.simbirchat.message;

/**
 * Этот интерфейс часть паттерна Bridge, для реализации дополнительных
 * элементов в текстовом сообщении
 */
public interface MessageResource {
    String getContent();
}
