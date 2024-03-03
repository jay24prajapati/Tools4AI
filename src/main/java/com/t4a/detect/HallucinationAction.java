package com.t4a.detect;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.t4a.bridge.AIAction;
import com.t4a.bridge.ActionType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Log
public class HallucinationAction implements AIAction {
    private String projectId;
    private String location ;
    private String modelName ;
    private String context;


    public List<HallucinationQA> askQuestions(String[] questions) {
        List<HallucinationQA> haList = new ArrayList<HallucinationQA>();

        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
            GenerativeModel model =
                    GenerativeModel.newBuilder()
                            .setModelName(modelName)
                            .setVertexAi(vertexAI)
                            .build();
            ChatSession chatSession = new ChatSession(model);


            for (String question : questions
            ) {
                log.info(question);
              //  GenerateContentResponse response = chatSession.sendMessage(question);
               // log.info(ResponseHandler.getText(response));

            }
        }
        return haList;
    }

    @Override
    public String getActionName() {
        return "askQuestions";
    }

    @Override
    public String getDescription() {
        return "uses self check mechansims to detect hallucinations";
    }

    @Override
    public ActionType getActionType() {
        return ActionType.HALLUCINATION;
    }
}
