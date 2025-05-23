package com.assessmint.be.ai;

import com.assessmint.be.ai.dtos.GenerateQuestionsDTO;
import com.assessmint.be.ai.dtos.GeneratedMCQDTO;
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
    private static final Schema mcqSchema = Schema.builder()
            .type(Type.Known.OBJECT)
            .properties(
                    Map.of(
                            "questionText", Schema.builder()
                                    .type(Type.Known.STRING)
                                    .description("Question text")
                                    .build(),
                            "options", Schema.builder()
                                    .type(Type.Known.ARRAY)
                                    .items(Schema.builder()
                                            .type(Type.Known.STRING)
                                            .description("Options for the question")
                                            .build())
                                    .description("Options for the question")
                                    .build(),
                            "answers", Schema.builder()
                                    .type(Type.Known.ARRAY)
                                    .items(Schema.builder()
                                            .type(Type.Known.STRING)
                                            .description("Answers for the question, from the options")
                                            .build())
                                    .description("Answers for the question")
                                    .build()
                    )
            )
            .propertyOrdering(List.of("questionText", "options", "answers"))
            .build();

    private GenerateContentResponse generateContent(
            Client client, String model, Content content, Schema responseSchema) {
        return client.models.generateContent(
                model, content,
                GenerateContentConfig.builder()
                        .responseMimeType("application/json")
                        .responseSchema(responseSchema)
                        .build()
        );
    }

    public List<GenerateQuestionsDTO> generateTF(GenerateQuestionsDTO reqdto) throws JsonProcessingException {

        final Client client = geminiService.getClient();
        final String model = geminiService.getModel();

        final Content tfContent = Content.builder()
                .parts(List.of(
                        Part.fromText(String.format(
                                "Generate %d true/false questions on '%s'." +
                                        " Each statement should be clear and answerable with True or False.",
                                reqdto.numberOfQuestions(), reqdto.topic()
                        ))
                ))
                .build();

        final Schema tfResponseSchema = Schema.builder()
                .type(Type.Known.ARRAY)
                .items(tfSchema)
                .build();

        logger.info("LLM initialized and requested model");

        final var response = generateContent(client, model, tfContent, tfResponseSchema);

        logger.info("LLM response received");

        final ObjectMapper tfMapper = new ObjectMapper();
        final List<GenerateQuestionsDTO> tfQuestions = tfMapper.readValue(
                response.text(), List.class
        );

        return tfQuestions;
    }

    public List<GeneratedMCQDTO> generateMCQ(GenerateQuestionsDTO reqdto) throws JsonProcessingException {

        final Client client = geminiService.getClient();
        final String model = geminiService.getModel();

        final Content mcqContent = Content.builder()
                .parts(List.of(
                        Part.fromText(String.format(
                                "Generate %d multiple-choice questions on '%s'." +
                                        " Each question should have up to 4 relevant options (as a list)" +
                                        " and be followed by the correct answer(s)" +
                                        " (as a list, allowing multiple correct answers).",
                                reqdto.numberOfQuestions(), reqdto.topic()
                        ))
                ))
                .build();

        final Schema mcqResponseSchema = Schema.builder()
                .type(Type.Known.ARRAY)
                .items(mcqSchema)
                .build();

        logger.info("LLM initialized and requested model");

        final var response = generateContent(client, model, mcqContent, mcqResponseSchema);

        logger.info("LLM response received");

        final ObjectMapper mcqMapper = new ObjectMapper();
        final List<GeneratedMCQDTO> mcqQuesitons = mcqMapper.readValue(
                response.text(), List.class
        );

        return mcqQuesitons;
    }
}
