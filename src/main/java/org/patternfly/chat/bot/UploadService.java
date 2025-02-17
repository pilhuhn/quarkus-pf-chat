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
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @author hrupp
 */
@Path("/upload")
public class UploadService {


    Set<String> allowedUrls = Set.of(
            // "https://github.com/patternfly",
            "https://patternfly.org",
            "https://www.patternfly.org"
            // "https://medium.com/patternfly"  TODO we need different document parsers here. GH as well.
    );

    @Inject
    MilvusIngester ingester;


    @GET
    @Path("/")
    public String upload(@QueryParam("url") String startUrl, @QueryParam("limit") int limit) throws Exception {

        Set<String> urlSet = new HashSet<>();
        int depth = 0;
        getUrls(urlSet, startUrl, depth, 2, limit);
        int count = 0;
        for (String url: urlSet) {
            System.out.println(url);

            if (count+1>limit) {
                break;
            }
            count++;

            try {


                Document doc;
                if (url.toLowerCase(Locale.ROOT).contains("patternfly.org")) { // TODO RE-match?
                    // We use a  different loader / parser that only returns the <div class=pf-v6-c-page__main-container>
                    // content (if exists at all) for PF
                    doc = MyDocumentLoader.load(url, new ApacheTikaDocumentParser());
                } else {
                    doc = UrlDocumentLoader.load(url, new ApacheTikaDocumentParser());
                }
                ingester.ingest(doc);
            }
            catch (Exception e) {
                System.err.println("Error ingesting " + url + " :-> " + e.getMessage());
            }
        }
        return "ok " + count + " documents ingested";
    }

    private void getUrls(Set<String> urlSet, String startUrl, int depth, int maxDepth, int collectionLimit) throws Exception {
        if (depth > maxDepth) {
            return;
        }

        if (urlSet.size() > collectionLimit) {
            return;
        }

        depth ++;

        if (urlSet.contains(startUrl)) {
            return;
        }

        urlSet.add(startUrl);
        org.jsoup.nodes.Document jdoc;
        try {
            jdoc = Jsoup.connect(startUrl).get();
        } catch (IOException ioe) {
            System.err.println("Error fetching " + startUrl + " -> " + ioe.getMessage());
            return;
        }

        Elements linksOnPage = jdoc.select("a[href]");
        for (Element page : linksOnPage) {
            // limit to pages of the same domain
            String href = page.attr("abs:href");
            if (!urlSet.contains(href)) {
                if (isInAllowList(href) && !urlSet.contains(href)) {
                    // recurse down TODO  Build a queue instead of recursion?
                    getUrls(urlSet, href, depth, maxDepth, collectionLimit);
                }
            }
        }
    }

    private boolean isInAllowList(String href) {
        return allowedUrls.stream().anyMatch(href::startsWith);
    }

}
