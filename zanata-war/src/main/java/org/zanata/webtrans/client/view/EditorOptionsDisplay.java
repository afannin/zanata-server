package org.zanata.webtrans.client.view;

import org.zanata.webtrans.client.presenter.UserConfigHolder;
import org.zanata.webtrans.client.ui.EnumRadioButtonGroup;
import org.zanata.webtrans.shared.rpc.NavOption;
import com.google.gwt.user.client.ui.HasValue;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

/**
 * @author Patrick Huang <a href="mailto:pahuang@redhat.com">pahuang@redhat.com</a>
 */
public interface EditorOptionsDisplay extends WidgetDisplay
{
   HasValue<Boolean> getTranslatedChk();

   HasValue<Boolean> getNeedReviewChk();

   HasValue<Boolean> getUntranslatedChk();

   void setListener(Listener listener);

   void setOptionsState(UserConfigHolder.ConfigurationState state);

   interface Listener extends EnumRadioButtonGroup.SelectionChangeListener<NavOption>
   {
      void onPageSizeClick(int pageSize);

      void onEnterSaveOptionChanged(Boolean enterSaveApproved);

      void onEscCancelEditOptionChanged(Boolean escCancelEdit);

      void onEditorButtonsOptionChanged(Boolean editorButtons);

      void onShowErrorsOptionChanged(Boolean showErrorChkValue);

      void onUseCodeMirrorOptionChanged(Boolean useCodeMirrorChkValue);

      void refreshCurrentPage();
   }
}