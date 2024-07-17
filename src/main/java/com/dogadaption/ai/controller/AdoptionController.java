package com.dogadaption.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class AdoptionController {

    private final ChatClient client;


    public AdoptionController(ChatClient client) {
        this.client = client;
    }

    @PostMapping("/suggestions")
    public DogAdoptionSuggestion dogAdoptionSuggestion(@RequestBody DogSuggestionQuery dogSuggestionQuery) {
        return client.prompt().user(dogSuggestionQuery.getQuery())
                .call()
                .entity(DogAdoptionSuggestion.class);
    }


}
