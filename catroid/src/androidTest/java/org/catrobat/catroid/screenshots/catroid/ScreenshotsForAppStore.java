/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2021 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.screenshots.catroid;

import android.content.Context;
import android.preference.PreferenceManager;

import com.badlogic.gdx.utils.Array;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.UiTestCatroidApplication;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.common.defaultprojectcreators.ChromeCastProjectCreator;
import org.catrobat.catroid.common.defaultprojectcreators.DefaultExampleProject;
import org.catrobat.catroid.common.defaultprojectcreators.DefaultProjectCreator;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.SetXBrick;
import org.catrobat.catroid.exceptions.LoadingProjectException;
import org.catrobat.catroid.io.XstreamSerializer;
import org.catrobat.catroid.test.utils.TestUtils;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.uiespresso.util.actions.CustomActions;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.web.webdriver.Locator;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy;
import tools.fastlane.screengrab.locale.LocaleTestRule;

import static org.catrobat.catroid.uiespresso.content.brick.utils.BrickDataInteractionWrapper.onBrickAtPosition;
import static org.catrobat.catroid.uiespresso.formulaeditor.utils.FormulaEditorWrapper.FORMULA_EDITOR_TEXT_FIELD_MATCHER;
import static org.catrobat.catroid.uiespresso.formulaeditor.utils.FormulaEditorWrapper.onFormulaEditor;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.web.sugar.Web.onWebView;
import static androidx.test.espresso.web.webdriver.DriverAtoms.findElement;
import static androidx.test.espresso.web.webdriver.DriverAtoms.webClick;

@RunWith(AndroidJUnit4.class)
public class ScreenshotsForAppStore {

	private static final String AGREED_TO_PRIVACY_POLICY_SETTINGS_KEY = "AgreedToPrivacyPolicy";

	@ClassRule
	public static final LocaleTestRule LOCALE_TEST_RULE = new LocaleTestRule();

	@Rule
	public ActivityTestRule<MainMenuActivity> mainActivityTestRule =
			new ActivityTestRule<>(MainMenuActivity.class, false, false);

	private String projectName;
	private ProjectManager projectManager;

	@Before
	public void setUp() throws IOException {

		PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext())
				.edit()
				.putBoolean(AGREED_TO_PRIVACY_POLICY_SETTINGS_KEY, true)
				.commit();
		Screengrab.setDefaultScreenshotStrategy(new UiAutomatorScreenshotStrategy());
		projectName =
				ApplicationProvider.getApplicationContext().getString(R.string.default_project_name) + "_test";
		Project project = new DefaultExampleProject()
				.createDefaultProject(projectName, ApplicationProvider.getApplicationContext(), true);
		projectManager = UiTestCatroidApplication.projectManager;
	}

	@After
	public void tearDown() throws Exception {
		projectManager.setCurrentProject(null);
		TestUtils.deleteProjects(projectName);
	}

	@Test
	public void createScreenshotsApp() {

		mainActivityTestRule.launchActivity(null);
		onView(isRoot()).perform(CustomActions.wait(1000));

		onView(withText(R.string.main_menu_programs)).check(matches(isDisplayed()));
		Screengrab.screenshot("screenshot1");

		onView(withText(R.string.main_menu_programs)).perform(click());
		onView(withText(projectName)).perform(click());
		onView(withText(R.string.default_project_sprites_animal_name)).perform(click());
		Screengrab.screenshot("screenshot2");

		onView(withId(R.id.button_add)).perform(click());
		onView(withText(R.string.category_event)).check(matches(isDisplayed()));
		Screengrab.screenshot("screenshot3");

		onView(withText(R.string.category_event)).perform(click());
		onBrickAtPosition(0)
				.checkShowsText(R.string.brick_when_started);
		Screengrab.screenshot("screenshot4");

	}

	@Test
	public void createScreenshotsWeb() {
		mainActivityTestRule.launchActivity(null);
		onView(withText(R.string.main_menu_web)).check(matches(isDisplayed()));
		onView(withText(R.string.main_menu_web)).perform(click());
		onView(isRoot()).perform(CustomActions.wait(10000));
		Screengrab.screenshot("screenshot5");
	}
}