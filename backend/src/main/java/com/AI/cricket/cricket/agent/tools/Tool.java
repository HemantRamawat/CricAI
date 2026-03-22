package com.AI.cricket.cricket.agent.tools;

public interface Tool {
    String getName();
    String getDescription();
    String execute(String input);
}