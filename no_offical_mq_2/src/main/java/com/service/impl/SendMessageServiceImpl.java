package com.service.impl;

import com.service.MQSenderSupplier;
import com.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service("sendMessageService")
public class SendMessageServiceImpl implements SendMessageService {
    @Autowired
    private ThreadPoolTaskExecutor executor;

    public String sendMessage(String message) {
        CompletableFuture<String> resultCompletableFuture =
                CompletableFuture.supplyAsync(new MQSenderSupplier(message), executor);
        try {
            String result = resultCompletableFuture.get();
            return result;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}