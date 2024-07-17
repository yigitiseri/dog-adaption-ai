package com.dogadaption.ai.configuration;

import com.dogadaption.ai.repository.DogRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class Assistant {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder, VectorStore simpleVectorStore) {
        var systemPrompt = """
                You are an AI-powered assistant for Dog-Center, an adoption agency dedicated to helping people find their perfect dog companion.
                Your primary role is to understand and analyze individuals' needs and preferences to suggest the most suitable dog available for adoption with with fancy sentences.
                In cases where no suitable match is found, respond empathetically, expressing regret that there are currently no dogs available that meet their specific requirements.
                """;
        return builder
                .defaultAdvisors(new QuestionAnswerAdvisor(simpleVectorStore, SearchRequest.defaults()))
                .defaultSystem(systemPrompt).build();
    }

    @Bean
    VectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
    }


    @Bean
    ApplicationRunner load(DogRepository dogRepository, VectorStore simpleVectorStore) {
        return args ->
                dogRepository.findAll().forEach(dog -> {
                    var content = "name %s, description: %s"
                            .formatted(dog.getName(), dog.getDescription());
                    System.out.println(content);
                    var document = new Document(content, Map.of("dogId", dog.getId()));
                    simpleVectorStore.add(List.of(document));
                });
    }
}
