package org.zanata.rest.service;

import static org.zanata.model.tm.TransMemoryUnit.tu;
import static org.zanata.model.tm.TransMemoryUnitVariant.tuv;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.ws.rs.core.StreamingOutput;

import org.testng.annotations.Test;
import org.zanata.common.LocaleId;
import org.zanata.model.tm.TransMemoryUnit;
import org.zanata.model.tm.TransMemory;

import com.google.common.collect.Lists;

public class ExportTransUnitTest extends TMXStreamingOutputTest
{

   @Test
   public void exportTransMemory() throws Exception
   {
      StreamingOutput output = streamSourceContents();
      checkAllLocales(output);
   }

   private TMXStreamingOutput<TransMemoryUnit> streamSourceContents()
   {
      return new TMXStreamingOutput<TransMemoryUnit>(createTestData(), new ExportTMXTransUnitStrategy());
   }

   private @Nonnull Iterator<TransMemoryUnit> createTestData()
   {
      TransMemory tm = null;
      String fr = LocaleId.FR.getId();
      String de = LocaleId.DE.getId();
      String sourceLoc = sourceLocale.getId();
      return Lists.<TransMemoryUnit>newArrayList(
            tu(
                  tm,
                  "doc0:resId0",
                  "doc0:resId0",
                  sourceLoc,
                  "source0",
                  tuv(fr, "targetFR0"),
                  tuv(de, "targetDE0")),
            tu(
                  tm,
                  "doc0:resId1",
                  "doc0:resId1",
                  sourceLoc,
                  "SOURCE0",
                  tuv(fr, "TARGETfr0")),
            tu(
                  tm,
                  "doc1:resId0",
                  "doc1:resId0",
                  sourceLoc,
                  "source0",
                  tuv(fr, "targetFR0")),
            tu(
                  tm,
                  "doc1:resId1",
                  "doc1:resId1",
                  sourceLoc,
                  "SOURCE0",
                  tuv(de, "TARGETde0"))).iterator();
   }

}
