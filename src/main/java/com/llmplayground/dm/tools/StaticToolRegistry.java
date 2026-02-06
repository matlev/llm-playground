package com.llmplayground.dm.tools;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class StaticToolRegistry implements ToolRegistry {

    @Override
    public List<ToolDescriptor> listTools() {
        return List.of(
                new ToolDescriptor(
                        "roll_dice",
                        "Rolls deterministic dice by supplied seed and notation.",
                        "{\"type\":\"object\",\"required\":[\"seed\",\"notation\"],\"properties\":{\"seed\":{\"type\":\"string\"},\"notation\":{\"type\":\"string\"}}}",
                        "{\"type\":\"object\",\"required\":[\"total\",\"breakdown\"],\"properties\":{\"total\":{\"type\":\"integer\"},\"breakdown\":{\"type\":\"array\",\"items\":{\"type\":\"integer\"}}}}"));
    }
}
