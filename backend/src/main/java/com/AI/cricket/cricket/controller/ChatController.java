package com.AI.cricket.cricket.controller;

import com.AI.cricket.cricket.service.AgentService;
import com.AI.cricket.cricket.service.RealAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ChatController {

    private final AgentService agentService;
    @Autowired
    private RealAgentService realAgentService;
    public ChatController(AgentService agentService){
        this.agentService = agentService;
    }

    @PostMapping("/chat")
    public String chat1(@RequestBody Map<String, Object> body){

        String message = (String) body.get("message");
        String matchName = (String) body.get("matchName");

        List<Map<String,String>> history =
                (List<Map<String,String>>) body.get("history");

        return agentService.process(message, matchName, history);
    }

    @PostMapping("/agent")
    public String chat(@RequestBody Map<String, Object> body){

        String message = (String) body.get("message");
        String matchName = (String) body.get("matchName");

        return realAgentService.process(message, matchName);
    }
}
