package com.denachina.webflux.controller;import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RestController;import reactor.core.publisher.Mono;/** * HealthController * * @author shanmin.zhang * @date 18/12/6 **/@RestController@RequestMapping("health")public class HealthController {    @GetMapping("check")    public Mono<String> check(){        return Mono.just("##->ok<-##");    }}