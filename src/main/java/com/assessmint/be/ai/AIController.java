package com.assessmint.be.ai;

import com.assessmint.be.ai.dtos.GenerateQuestionsDTO;
import com.assessmint.be.ai.dtos.GeneratedMCQDTO;
import com.assessmint.be.global.controllers.dtos.APIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.genai.Client;
import com.google.genai.types.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {
    private final AIService aiService;

    @Value("${app.gemini.api-key}")
    private String apiKey;

    @PostMapping("/chat")
    public String chat(@RequestBody String message) {

        final Client client = Client.builder().apiKey(apiKey).build();
        String model = "gemini-2.0-flash";

        final var response = client.models.generateContent(
                model,
                Content.builder().build(),
                GenerateContentConfig.builder()
                        .responseMimeType("application/json")
                        .responseSchema(
                                Schema.builder()
                                        .type(Type.Known.OBJECT)
                                        .build()
                        )
                        .build()
        );

//        List<Content> contents = ImmutableList.of(
//                Content.builder()
//                        .role("user")
//                        .parts(ImmutableList.of(
//                                Part.fromText("i want you to tell me a joke\n")
//                        ))
//                        .build(),
//                Content.builder()
//                        .role("model")
//                        .parts(ImmutableList.of(
//                                Part.fromText("**Devising a Cheese Joke**\n\nI'm starting with a straightforward request: \"a joke.\" It's a blank slate. Now, I'm considering types: pun-based jokes for their speed and wordplay. Next, I'll explore observational humor, maybe something about cheese in particular. I'm searching for the most efficient path.\n\n\n**Exploring Joke Formats**\n\nI've evaluated different joke types, favoring puns for quick development. The idea of question/answer formats is intriguing too. I'm brainstorming keywords around animals, food, objects, people, and situations, aiming for a relatable cheese joke. I'm testing pun ideas internally, searching for the right balance of cleverness and humor.\n\n\n**Crafting the Cheese Joke**\n\nI'm now zeroing in on a question-and-answer format for the cheese joke. I'm building on the premise of \"what do you call\" to create a punchline that's memorable. It's becoming more focused on a clever wordplay. I am trying to build upon and avoid already well-known jokes. I am working towards a final draft.\n\n\n**Developing a Final Punchline**\n\nI've been examining the initial joke I came up with, the \"Nacho cheese\" pun, in light of the request for a \"joke.\" I'm now exploring if the punchline is too common.  I'm now in a trial-and-error phase, attempting to create a unique and more clever version. I'm exploring the nuances of cheese types and puns to avoid the obvious and aiming for a fresh approach.\n\n\n"),
//                                Part.fromText("Okay, here's one for you:\n\nWhy don't skeletons fight each other?\n\n... They don't have the guts!")
//                        ))
//                        .build(),
//                Content.builder()
//                        .role("user")
//                        .parts(ImmutableList.of(
//                                Part.fromText(message)
//                        ))
//                        .build()
//        );
//        GenerateContentConfig config =
//                GenerateContentConfig
//                        .builder()
//                        .responseMimeType("text/plain")
//                        .build();

//        ResponseStream<GenerateContentResponse> responseStream = client.models.generateContentStream(model, contents, config);
//
//        for (GenerateContentResponse res : responseStream) {
//            if (res.candidates().isEmpty() || res.candidates().get().get(0).content().isEmpty() || res.candidates().get().get(0).content().get().parts().isEmpty()) {
//                continue;
//            }
//
//            List<Part> parts = res.candidates().get().get(0).content().get().parts().get();
//            for (Part part : parts) {
//                System.out.println(part.text());
//            }
//        }
//
//        responseStream.close();

        return "Chat with AI";
    }

    @PostMapping("/generate_tf")
    public ResponseEntity<APIResponse<List<GenerateQuestionsDTO>>> generateTF(
            @Valid @RequestBody GenerateQuestionsDTO reqdto
    ) throws JsonProcessingException {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "TF_QUESTIONS_GENERATION_SUCCESS",
                aiService.generateTF(reqdto)
        );
    }

    @PostMapping("/generate_mcq")
    public ResponseEntity<APIResponse<List<GeneratedMCQDTO>>> generateMCQ(
            @Valid @RequestBody GenerateQuestionsDTO reqdto
    ) throws JsonProcessingException {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "MCQ_QUESTIONS_GENERATION_SUCCESS",
                aiService.generateMCQ(reqdto)
        );
    }
}
