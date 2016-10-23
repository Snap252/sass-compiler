/*
 * Copyright 2000-2014 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.sass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.w3c.css.sac.CSSException;

import com.vaadin.sass.internal.ScssStylesheet;

import org.junit.Assert;

public abstract class AbstractTestBase {

    public static final String CR = "\r";

    protected String originalScss;
    protected String parsedScss;
    protected String comparisonCss;

    public ScssStylesheet getStyleSheet(String filename)
            throws URISyntaxException, CSSException, IOException {
        final File file = new File(getClass().getResource(filename).toURI());
        return ScssStylesheet.get(file.getAbsolutePath());
    }

    public final String getFileContent(String filename) throws IOException,
            CSSException, URISyntaxException {
        return IOUtils.toString(getClass().getResourceAsStream(filename), "UTF-8");
    }

    public ScssStylesheet testParser(String fileName) throws CSSException,
            IOException, URISyntaxException {
        originalScss = getFileContent(fileName);
        originalScss = originalScss.replaceAll(CR, "");
        ScssStylesheet sheet = getStyleSheet(fileName);
        parsedScss = sheet.printState();
        parsedScss = parsedScss.replace(CR, "");
        Assert.assertEquals("Original CSS and parsed CSS do not match",
                originalScss, parsedScss);
        return sheet;
    }

    public ScssStylesheet testCompiler(String scss, String cssFileName)
            throws Exception {
        comparisonCss = getFileContent(cssFileName);
        comparisonCss = comparisonCss.replaceAll(CR, "");
        ScssStylesheet sheet = getStyleSheet(scss);
        sheet.compile();
        parsedScss = sheet.printState();
        parsedScss = parsedScss.replaceAll(CR, "");
        Assert.assertEquals("Original CSS and parsed CSS do not match",
                comparisonCss, parsedScss);
        return sheet;
    }
}
