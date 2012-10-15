/*
 * Copyright 2010, Red Hat, Inc. and individual contributors as indicated by the
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
package org.zanata.service;

import java.util.List;
import java.util.Set;

import org.zanata.common.ContentState;
import org.zanata.common.LocaleId;
import org.zanata.common.MergeType;
import org.zanata.exception.ConcurrentTranslationException;
import org.zanata.model.HTextFlowTarget;
import org.zanata.rest.dto.resource.TranslationsResource;
import org.zanata.webtrans.shared.model.TransUnitUpdateInfo;
import org.zanata.webtrans.shared.model.TransUnitUpdateRequest;

public interface TranslationService
{
   /**
    * Batch size processing for large file upload
    */
   final static int NUM_BATCHES = 100;


   /**
    * Updates a single translation for a single text flow.
    * 
    * @param localeId
    * @param translationRequest
    * @return information about the translation change
    * @throws ConcurrentTranslationException if there has been another
    *            translation based on the same translation version
    */
   TranslationResult translate(LocaleId localeId, TransUnitUpdateRequest translationRequest) throws ConcurrentTranslationException;

   /**
    * Updates multiple text flows within a project-iteration.
    * 
    * @param localeId
    * @param translationRequests
    * @return information about each translation change
    */
   List<TranslationResult> translate(LocaleId localeId, List<TransUnitUpdateRequest> translationRequests);


   /**
    * Attempts to revert a list of updates by adding a new translation that is
    * identical to the previous one.
    * 
    * The versionNum of the translation is advanced and new history is created,
    * so the update and the undo will be visible in history.
    * 
    * If any additional translations have been added after an update described
    * in translationsToRevert, that update will not be reverted and its
    * {@link TranslationResult} will report false for isTranslationSuccessful().
    * This will not prevent other updates in translationsToRevert being
    * reverted.
    * 
    * @param localeId
    * @param translationsToRevert describe each of the updates to be reverted
    * @return a list of results describing the outcome of each revert
    */
   List<TranslationResult> revertTranslations(LocaleId localeId, List<TransUnitUpdateInfo> translationsToRevert);

   /**
    * Translates all text flows in a document.

    * @param projectSlug The project to translate
    * @param iterationSlug The project iteration to translate
    * @param docId The document identifier to translate
    * @param locale The locale that the translations belong to
    * @param translations The translations to save to the document
    * @param extensions The extensions to use while translating
    * @param mergeType Indicates how to handle the translations. AUTO will merge the new translations with the provided
    *                  ones. IMPORT will overwrite all existing translations with the new ones.
    * @param lock If true, no other caller will be allowed to translate All for the same project, iteration, document
    *             and locale.
    * @param userName User name that initiated this translation. If this method is called within a session context, the
    *                 recorded user will be the session's user. Otherwise, it will be the user specified by this parameter.
    * @return A list of warnings about text flow targets that (a) could not be matched to any text flows in the source document
    * or (b) whose states don't match their contents.
    * @see TranslationService#translateAllInDoc(String, String, String, org.zanata.common.LocaleId, org.zanata.rest.dto.resource.TranslationsResource, java.util.Set, org.zanata.common.MergeType)
    */
   public List<String> translateAllInDoc(String projectSlug, String iterationSlug, String docId, LocaleId locale,
                                         TranslationsResource translations, Set<String> extensions, MergeType mergeType,
                                         boolean lock, String userName);

   /**
    * Translates all text flows in a document.
    *
    * @param projectSlug The project to translate
    * @param iterationSlug The project iteration to translate
    * @param docId The document identifier to translate
    * @param locale The locale that the translations belong to
    * @param translations The translations to save to the document
    * @param extensions The extensions to use while translating
    * @param mergeType Indicates how to handle the translations. AUTO will merge the new translations with the provided
    *                  ones. IMPORT will overwrite all existing translations with the new ones.
    * @return A list of warnings about text flow targets that (a) could not be matched to any text flows in the source document 
    * or (b) whose states don't match their contents.
    */
   List<String> translateAllInDoc(String projectSlug, String iterationSlug, String docId, LocaleId locale, TranslationsResource translations, Set<String> extensions,
                                          MergeType mergeType);

   public interface TranslationResult
   {
      boolean isTranslationSuccessful();
      boolean isTargetChanged();
      HTextFlowTarget getTranslatedTextFlowTarget();
      int getBaseVersionNum();
      ContentState getBaseContentState();
   }
}
