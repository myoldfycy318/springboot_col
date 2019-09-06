package com.compent;import lombok.extern.slf4j.Slf4j;import org.springframework.beans.factory.annotation.Value;import org.springframework.kafka.annotation.KafkaListener;import org.springframework.stereotype.Component;/** * KafkaListenerTest * * @author shanmin.zhang * @date 19/1/21 **/@Component@Slf4jpublic class KafkaListenerTest {    @KafkaListener(topics = "mask-word-topic", groupId = "${appconfig.customerTopic}")    public void listen(String value) {        System.out.println("-----========》广播组0 " + value);        log.info("-->" + value);    }}