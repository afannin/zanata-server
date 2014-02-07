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

import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.zanata.feature.BasicAcceptanceTest;
import org.zanata.page.projects.ProjectSourceDocumentsPage;
import org.zanata.page.webtrans.EditorPage;
import org.zanata.util.CleanDocumentStorageRule;
import org.zanata.util.SampleProjectRule;
import org.zanata.util.TestFileGenerator;
import org.zanata.workflow.LoginWorkFlow;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Damian Jansen
 *         <a href="mailto:djansen@redhat.com">djansen@redhat.com</a>
 */
@RunWith(Theories.class)
public class DocTypeUploadTest {

    @Rule
    public SampleProjectRule sampleProjectRule = new SampleProjectRule();

    @Rule
    public CleanDocumentStorageRule documentStorageRule =
            new CleanDocumentStorageRule();

    private static String testString = "Test text 1";

    @DataPoint
    public static File SRT_FILE = new TestFileGenerator()
            .generateTestFileWithContent(
                    "testsrtfile", ".srt",
                    "1" + sep() + "00:00:01,000 --> 00:00:02,000" +
                    sep() + testString);

    @DataPoint
    public static File WEBVTT_FILE = new TestFileGenerator()
            .generateTestFileWithContent(
                    "testvttfile", ".vtt",
                    "00:01.000 --> 00:01.000" +
                    sep() + testString);

    @DataPoint
    public static File SBT_FILE = new TestFileGenerator()
            .generateTestFileWithContent(
                    "testsbtfile", ".sbt",
                    "00:04:35.03,00:04:38.82" +
                    sep() + testString);

    @DataPoint
    public static File SUB_FILE = new TestFileGenerator()
            .generateTestFileWithContent(
                    "testsubfile", ".sub",
                    "00:04:35.03,00:04:38.82" +
                    sep() + testString);

    @Theory
    @Category(BasicAcceptanceTest.class)
    public void uploadFile(File testFile) {
        String testFileName = testFile.getName();
        String successfullyUploaded =
                "Document file " + testFileName + " uploaded.";

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
                projectSourceDocumentsPage.sourceDocumentsContains(testFileName));

        EditorPage editorPage = projectSourceDocumentsPage.goToProjects()
                .goToProject("about fedora").goToVersion("master")
                .translate("pl").clickDocumentLink("", testFileName);

        assertThat("The translation source is correct",
                editorPage.getMessageSourceAtRowIndex(0),
                equalTo(testString));
    }

    private static String sep() {
        return System.getProperty("line.separator");
    }

}
