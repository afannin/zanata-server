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
package org.zanata.feature.administration;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.zanata.feature.DetailedTest;
import org.zanata.page.administration.ManageSearchPage;
import org.zanata.page.utility.DashboardPage;
import org.zanata.util.SampleProjectRule;
import org.zanata.workflow.LoginWorkFlow;

import static org.hamcrest.MatcherAssert.assertThat;

@Category(DetailedTest.class)
public class ManageSearchTest {

    @Rule
    public SampleProjectRule sampleProjectRule = new SampleProjectRule();

    private DashboardPage dashboardPage;

    @Before
    public void before() {
        dashboardPage = new LoginWorkFlow().signIn("admin", "admin");
    }

    @Test
    public void regenerateSearchIndexes() {
        ManageSearchPage manageSearchPage = dashboardPage
                .goToAdministration()
                .goToManageSeachPage()
                .clickSelectAll();

        assertThat("All actions are selected",
                manageSearchPage.allActionsSelected());
        assertThat("No operations are running",
                manageSearchPage.noOperationsRunningIsDisplayed());

        manageSearchPage = manageSearchPage
                .performSelectedActions()
                .waitForActionsToFinish();

        assertThat("Completed is displayed",
                manageSearchPage.completedIsDisplayed());

        assertThat("No operations are running",
                manageSearchPage.noOperationsRunningIsDisplayed());
    }

    @Test
    @Ignore("Data set not large enough to achieve stable test")
    public void abortReindexes() {
        ManageSearchPage manageSearchPage = dashboardPage
                .goToAdministration()
                .goToManageSeachPage()
                .clickSelectAll();

        assertThat("All actions are selected",
                manageSearchPage.allActionsSelected());
        assertThat("No operations are running",
                manageSearchPage.noOperationsRunningIsDisplayed());

        manageSearchPage = manageSearchPage
                .performSelectedActions()
                .abort();

        assertThat("Aborted is displayed",
                manageSearchPage.abortedIsDisplayed());
    }
}
