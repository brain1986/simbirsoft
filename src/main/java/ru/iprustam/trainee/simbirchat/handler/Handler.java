package ru.iprustam.trainee.simbirchat.handlers;

import ru.iprustam.trainee.simbirchat.handlers.handleresults.HandleDetails;
import ru.iprustam.trainee.simbirchat.handlers.handleresults.HandleErrorType;
import ru.iprustam.trainee.simbirchat.handlers.handleresults.HandleResult;
import ru.iprustam.trainee.simbirchat.message.ChatMessage;

/**
 * Абстрактный класс для реализации паттерна Chain of responsibility
 * Обработчиками являются действия из матрицы действий из ТЗ
 */
public abstract class Handler {
    protected Handler next;

    public final HandleResult handle(ChatMessage chatMessage) {
        if(canHandle(chatMessage))
            return doHandle(chatMessage);
        else
            if (next != null)
                return next.handle(chatMessage);
            else
                return new HandleResult(HandleErrorType.ERROR,
                        new HandleDetails("No methods in the chain to accomplish operation"));
    }

    public final Handler setNext(Handler next) {
        this.next = next;
        return this;
    }

    protected boolean canHandle(ChatMessage chatMessage) {
        return false;
    }
    protected abstract HandleResult doHandle(ChatMessage chatMessage);
}
