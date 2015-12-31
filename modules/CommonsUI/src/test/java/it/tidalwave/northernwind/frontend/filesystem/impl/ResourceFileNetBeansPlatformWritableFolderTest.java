/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
 * *********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * *********************************************************************************************************************
 *
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.northernwind.frontend.filesystem.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import it.tidalwave.role.Marshallable;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import lombok.extern.slf4j.Slf4j;
import static org.mockito.Mockito.*;
import static it.tidalwave.util.test.FileComparisonUtils.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class ResourceFileNetBeansPlatformWritableFolderTest
  {
    private ResourceFileNetBeansPlatformWritableFolder underTest;

    private ResourceFileNetBeansPlatform fileNetBeansPlatform;

    private FileObject fileObject;

    private File folder;

    private Marshallable marshallable;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        folder = new File("target/test-results/mockFileObject");
        log.info("Creating {} ...", folder.getAbsolutePath());
        folder.mkdirs();
//        assertThat(folder.mkdirs(), is(true));

        fileObject = mock(FileObject.class);
        when(fileObject.toString()).thenReturn("mockFileObject");

        when(fileObject.getFileObject(anyString())).thenAnswer(invocation ->
          {
            final String fileName = (String)invocation.getArguments()[0];
            final FileObject childFile = mock(FileObject.class);
            when(childFile.toString()).thenReturn("mockFileObject/" + fileName);
            final FileLock lock = mock(FileLock.class);
            when(childFile.lock()).thenReturn(lock); // prevent later NPE

            when(childFile.getOutputStream(any(FileLock.class))).thenAnswer(
                    invocation1 -> new FileOutputStream(new File(folder, fileName)));

            return childFile;
          });

        fileNetBeansPlatform = mock(ResourceFileNetBeansPlatform.class);
        when(fileNetBeansPlatform.getDelegate()).thenReturn(fileObject);
        underTest = new ResourceFileNetBeansPlatformWritableFolder(fileNetBeansPlatform);

        marshallable = out -> out.write("marshallable\n".getBytes("UTF-8"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_write_a_text()
      throws IOException
      {
        // when
        underTest.write("fileName", "proven\u00E7al\n");
        // then
        final File expectedFile = new File("src/test/resources/expected-results/fileName");
        final File actualFile = new File(folder, "fileName");
        assertSameContents(expectedFile, actualFile);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_marshall()
      throws IOException
      {
        // when
        underTest.write("marshallable", marshallable);
        // then
        final File expectedFile = new File("src/test/resources/expected-results/marshallable");
        final File actualFile = new File(folder, "marshallable");
        assertSameContents(expectedFile, actualFile);
      }
  }
