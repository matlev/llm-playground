package com.llmplayground.dm.tools;

import java.util.List;

public interface ToolRegistry {
    List<ToolDescriptor> listTools();

    record ToolDescriptor(String name, String description, String inputSchemaJson, String outputSchemaJson) {}
}
