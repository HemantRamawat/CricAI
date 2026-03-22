package com.AI.cricket.cricket.agent.tools;

import com.AI.cricket.cricket.agent.DataAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GetMatchesTool implements Tool {

    @Autowired
    private DataAgent dataAgent;

    @Override
    public String getName() {
        return "get_matches";
    }

    @Override
    public String getDescription() {
        return "Get today's cricket matches";
    }

    @Override
    public String execute(String input) {

        var matches = dataAgent.getMatches();

        StringBuilder sb = new StringBuilder();

        for(Map<String,Object> match : matches){
            sb.append(match.get("name")).append("\n");
        }

        return sb.toString();
    }
}
