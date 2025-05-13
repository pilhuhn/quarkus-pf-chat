/*
 * Copyright 2025 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.patternfly.chat.bot;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.SessionScoped;

/**
 * @author hrupp
 */
@SessionScoped //  WS wants this session scoped
@RegisterAiService(modelName = "model1")
public interface MyChatServiceWS {
    @SystemMessage("You are a helpful and polite assistant. When replying with code, please format it as Markdown.")
        @UserMessage(
"""
Please provide Info about {topic} (in the context of PatternFly).
""")
    Multi<String> patternFlyInfo(String topic);
}
