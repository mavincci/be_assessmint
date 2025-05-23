package com.assessmint.be.ai;

import com.assessmint.be.ai.dtos.GenerateTFQeustionsDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {
    private final Logger logger = LoggerFactory.getLogger(AIService.class);

    private final GeminiService geminiService;

    private static final Schema tfSchema = Schema.builder()
            .type(Type.Known.OBJECT)
            .properties(
                    Map.of(
                            "questionText", Schema.builder()
                                    .type(Type.Known.STRING)
                                    .description("Question text")
                                    .build(),
                            "answer", Schema.builder()
                                    .type(Type.Known.BOOLEAN)
                                    .description("Answer value for the question")
                                    .build()
                    )
            )
            .propertyOrdering(List.of("questionText", "answer"))
            .build();

    public List<GenerateTFQeustionsDTO> prepare(GenerateTFQeustionsDTO reqdto) throws JsonProcessingException {

        final Client client = geminiService.getClient();
        final String model = geminiService.getModel();

        logger.info("LLM initialized and requested model");
        final var response = client.models.generateContent(
                model,
                Content.builder()
                        .parts(List.of(
                                Part.fromText("Please provide a list of questions and answers on the topic of " + reqdto.topic()),
                                Part.fromText("\n The questions should be " + reqdto.numberOfQuestions() + "in number"),
                                Part.fromText("\n Make sure the questions are relevant to the topic and the answers are either true or false")
                        ))
                        .build(),
                GenerateContentConfig.builder()
                        .responseMimeType("application/json")
                        .responseSchema(
                                Schema.builder()
                                        .type(Type.Known.ARRAY)
                                        .items(tfSchema)
                                        .build()
                        )
                        .build()
        );
        logger.info("LLM response received");

        final ObjectMapper tfMapper = new ObjectMapper();
        final List<GenerateTFQeustionsDTO> tfQuestions = tfMapper.readValue(
                response.text(), List.class
        );

        return tfQuestions;
    }
}
