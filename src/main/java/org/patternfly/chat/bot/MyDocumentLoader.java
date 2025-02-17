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

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.source.UrlSource;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Use "real" XPath, as Apache Tika XPath is pretty
 * limited, so we need to pre-parse the document
 * already
 * @author hrupp
 */
public class MyDocumentLoader {

    public static Document load(String url, DocumentParser parser) {
        UrlSource iurl = UrlSource.from(url);

        try (InputStream is = iurl.inputStream()) {
            org.jsoup.nodes.Document doc = Jsoup.parse(is,"UTF-8", url);
            String exp = "/html/body//div[@id='ws-page-main']";
            Elements  elems = doc.selectXpath(exp);
            String text = elems.toString();
            InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));

            return parser.parse(stream);

        } catch (Exception e) {
            System.err.println(e.getMessage() + " -> " + e.getCause().getMessage());
            throw new RuntimeException(e);
        }
    }
}
