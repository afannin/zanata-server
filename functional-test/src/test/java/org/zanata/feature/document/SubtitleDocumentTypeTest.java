/*
 * Copyright 2014, Red Hat, Inc. and individual contributors as indicated by the
 * @author tags. See the copyright.txt file in the distribution for a full
 * listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.zanata.feature.document;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.zanata.feature.DetailedTest;
import org.zanata.page.projects.ProjectSourceDocumentsPage;
import org.zanata.page.webtrans.EditorPage;
import org.zanata.util.CleanDocumentStorageRule;
import org.zanata.util.SampleProjectRule;
import org.zanata.util.TestFileGenerator;
import org.zanata.workflow.BasicWorkFlow;
import org.zanata.workflow.LoginWorkFlow;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.zanata.util.FunctionalTestHelper.assumeFalse;

/**
 * Covers more detailed testing of the subtitle formats
 * @see DocTypeUploadTest
 * @author Damian Jansen <a
 *         href="mailto:djansen@redhat.com">djansen@redhat.com</a>
 */
@Category(DetailedTest.class)
public class SubtitleDocumentTypeTest {
    @Rule
    public SampleProjectRule sampleProjectRule = new SampleProjectRule();

    @Rule
    public CleanDocumentStorageRule documentStorageRule =
            new CleanDocumentStorageRule();

    private TestFileGenerator testFileGenerator = new TestFileGenerator();
    private String sep = System.getProperty("line.separator");

    @Before
    public void before() {
        new BasicWorkFlow().goToHome().deleteCookiesAndRefresh();
        String documentStorageDirectory =
                CleanDocumentStorageRule.getDocumentStoragePath()
                        .concat(File.separator).concat("documents")
                        .concat(File.separator);
        assumeFalse("", new File(documentStorageDirectory).exists());
    }

    @Test
    public void similarSrtEntriesAreIndividual() {
        EditorPage editorPage = uploadAndGoToDocument(testFileGenerator
                .generateTestFileWithContent("duplicationinsrtfile", ".srt",
                        "1" + sep +
                        "00:00:01,000 --> 00:00:02,000" + sep +
                        "Exactly the same text" + sep + sep +
                        "2" + sep +
                        "00:00:02,000 --> 00:00:03,000" + sep +
                        "Exactly the same text"));

        assertThat("The first translation source is correct",
                editorPage.getMessageSourceAtRowIndex(0),
                equalTo("Exactly the same text"));
        assertThat("The second translation source is correct",
                editorPage.getMessageSourceAtRowIndex(1),
                equalTo("Exactly the same text"));
    }

    @Test
    public void webVttLabelsAreNotParsed() {
        EditorPage editorPage = uploadAndGoToDocument(testFileGenerator
                .generateTestFileWithContent("labelledVttfile", ".vtt",
                        "Introduction" + sep +
                        "00:00:01.000 --> 00:00:02.000" + sep +
                        "Test subtitle 1"));

        assertThat("The translation source is correct",
                editorPage.getMessageSourceAtRowIndex(0),
                equalTo("Test subtitle 1"));
    }

    @Test
    public void multilineSRTAreParsedCorrectly() {
        EditorPage editorPage = uploadAndGoToDocument(testFileGenerator
                .generateTestFileWithContent("multilinesrtfile", ".srt",
                        "1" + sep +
                        "00:00:01,000 --> 00:00:02,000" + sep +
                        "Test subtitle 1" + sep +
                        "Test subtitle 1 line 2" + sep + sep +
                        "2" + sep +
                        "00:00:03,000 --> 00:00:04,000" + sep +
                        "Test subtitle 2"));

        assertThat("The first translation source is correct",
                editorPage.getMessageSourceAtRowIndex(0),
                equalTo("Test subtitle 1" + sep + "Test subtitle 1 line 2"));
        assertThat("The second translation source is correct",
                editorPage.getMessageSourceAtRowIndex(1),
                equalTo("Test subtitle 2"));
    }

    @Test
    public void multilineVTTAreParsedCorrectly() {
        EditorPage editorPage = uploadAndGoToDocument(testFileGenerator
                .generateTestFileWithContent("multilinevttfile", ".vtt",
                        "00:00:01.000 --> 00:00:02.000" + sep +
                        "Test subtitle 1" + sep +
                        "Test subtitle 1 line 2" + sep + sep +
                        "00:00:03.000 --> 00:00:04.000" + sep +
                        "Test subtitle 2"));

        assertThat("The first translation source is correct",
                editorPage.getMessageSourceAtRowIndex(0),
                equalTo("Test subtitle 1" + sep + "Test subtitle 1 line 2"));
        assertThat("The second translation source is correct",
                editorPage.getMessageSourceAtRowIndex(1),
                equalTo("Test subtitle 2"));
    }

    @Test
    public void multilineSBTAreParsedCorrectly() {
        EditorPage editorPage = uploadAndGoToDocument(testFileGenerator
                .generateTestFileWithContent("multilinesbtfile", ".sbt",
                        "00:04:35.03,00:04:38.82" + sep +
                        "Test subtitle 1" + sep +
                        "Test subtitle 1 line 2" + sep + sep +
                        "2" + sep +
                        "00:04:39.03,00:04:44.82" + sep +
                        "Test subtitle 2"));

        assertThat("The first translation source is correct",
                editorPage.getMessageSourceAtRowIndex(0),
                equalTo("Test subtitle 1" + sep + "Test subtitle 1 line 2"));
        assertThat("The second translation source is correct",
                editorPage.getMessageSourceAtRowIndex(1),
                equalTo("Test subtitle 2"));
    }

    @Test
    public void multilineSubAreParsedCorrectly() {
        EditorPage editorPage = uploadAndGoToDocument(testFileGenerator
                .generateTestFileWithContent("multilinesubfile", ".sub",
                        "00:04:35.03,00:04:38.82" + sep +
                        "Test subtitle 1" + sep +
                        "Test subtitle 1 line 2" + sep + sep +
                        "00:04:39.03,00:04:44.82" + sep +
                        "Test subtitle 2"));

        assertThat("The first translation source is correct",
                editorPage.getMessageSourceAtRowIndex(0),
                equalTo("Test subtitle 1" + sep + "Test subtitle 1 line 2"));
        assertThat("The second translation source is correct",
                editorPage.getMessageSourceAtRowIndex(1),
                equalTo("Test subtitle 2"));
    }

    @Test
    public void formattingInSrtEntries() {
        EditorPage editorPage = uploadAndGoToDocument(
                testFileGenerator.generateTestFileWithContent(
                    "formattedsrtfile",
                    ".srt",
                    "1" + sep + "00:00:01,000 --> 00:00:02,000" + sep +
                    "<b>Exactly the same text</b> {u}and more{/u}"));

        assertThat("The translation source is correct",
                editorPage.getMessageSourceAtRowIndex(0),
                equalTo("<x1/>Exactly the same text<x2/> {u}and more{/u}"));
    }

    @Test
    public void formattingInVttEntries() {
        EditorPage editorPage = uploadAndGoToDocument(
                testFileGenerator.generateTestFileWithContent(
                        "formattedvttfile",
                        ".vtt",
                        "00:00:01.000 --> 00:00:02.000" + sep +
                        "<b>Exactly the same text</b> {u}and more{/u}"));

        assertThat("The translation source is correct",
                editorPage.getMessageSourceAtRowIndex(0),
                equalTo("<x1/>Exactly the same text<x2/> {u}and more{/u}"));
    }

    @Test
    public void formattingInSbtEntries() {
        EditorPage editorPage = uploadAndGoToDocument(
                testFileGenerator.generateTestFileWithContent(
                        "formattedsbtfile",
                        ".sbt",
                        "00:04:35.03,00:04:38.82" + sep +
                        "<b>Exactly the same text</b> {u}and more{/u}"));

        assertThat("The translation source is correct",
                editorPage.getMessageSourceAtRowIndex(0),
                equalTo("<x1/>Exactly the same text<x2/> {u}and more{/u}"));
    }

    @Test
    public void formattingInSubEntries() {
        EditorPage editorPage = uploadAndGoToDocument(
                testFileGenerator.generateTestFileWithContent(
                        "formattedsubfile",
                        ".sub",
                        "00:04:35.03,00:04:38.82" + sep +
                        "<b>Exactly the same text</b> {u}and more{/u}"));

        assertThat("The translation source is correct",
                editorPage.getMessageSourceAtRowIndex(0),
                equalTo("<x1/>Exactly the same text<x2/> {u}and more{/u}"));
    }

    /*
     * Upload and open the test file in the editor for verification
     */
    private EditorPage uploadAndGoToDocument(File testFile) {
        String successfullyUploaded =
                "Document file " + testFile.getName() + " uploaded.";
        ProjectSourceDocumentsPage projectSourceDocumentsPage =
                new LoginWorkFlow().signIn("admin", "admin").goToProjects()
                        .goToProject("about fedora").goToVersion("master")
                        .goToSourceDocuments().pressUploadFileButton()
                        .enterFilePath(testFile.getAbsolutePath())
                        .submitUpload();

        assertThat("Document uploaded notification shows",
                projectSourceDocumentsPage.getNotificationMessage(),
                equalTo(successfullyUploaded));
        assertThat("Document shows in table",
                projectSourceDocumentsPage.sourceDocumentsContains(testFile
                        .getName()));

        return projectSourceDocumentsPage.goToProjects()
                .goToProject("about fedora").goToVersion("master")
                .translate("pl").clickDocumentLink("", testFile.getName());
    }
}
