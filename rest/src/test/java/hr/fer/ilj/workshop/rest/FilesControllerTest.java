package hr.fer.ilj.workshop.rest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import hr.fer.ilj.workshop.files.FileInfo;
import hr.fer.ilj.workshop.files.FileLoader;
import hr.fer.ilj.workshop.files.FileType;

@WebMvcTest(FilesController.class)
class FilesControllerTest {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private FileLoader loader;

  @Test
  void emptyDirectory() throws Exception {
    given(loader.loadFiles("/d1/d2"))
      .willReturn(List.of());

    mvc.perform(get("/d1/d2").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().string("[]"));
  }

  @Test
  void directoryWithOneFile() throws Exception {
    given(loader.loadFiles("/d1/d2"))
      .willReturn(List.of(new FileInfo("f1", Path.of("/d1/d2/f1"), 134, FileType.FILE)));

    mvc.perform(get("/d1/d2").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json("""
              [
                {
                  "name": "f1",
                  "path": "/d1/d2/f1",
                  "size": 134,
                  "type": "FILE"
                }
              ]
              """));
  }

  @Test
  void directoryWithFileAndDirectory() throws Exception {
    given(loader.loadFiles("/d1/d2"))
      .willReturn(List.of(
        new FileInfo("f1", Path.of("/d1/d2/f1"), 134, FileType.FILE),
        new FileInfo("d3", Path.of("/d1/d2/d3"), 0, FileType.DIRECTORY)
      ));

    mvc.perform(get("/d1/d2").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json("""
              [
                {
                  "name": "f1",
                  "path": "/d1/d2/f1",
                  "size": 134,
                  "type": "FILE"
                },
                {
                  "name": "d3",
                  "path": "/d1/d2/d3",
                  "size": 0,
                  "type": "DIRECTORY"
                }
              ]
              """));
  }

  @Test
  void requestedFile_should_return404() throws Exception {
    given(loader.loadFiles("/someFile"))
      .willThrow(new NotDirectoryException("/someFile"));

    mvc.perform(get("/someFile").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }

  @Test
  void notExistingDirectory_should_return400() throws Exception {
    given(loader.loadFiles("/someNotExistingDirectory"))
      .willThrow(new AccessDeniedException("/someNotExistingDirectory"));

    mvc.perform(get("/someNotExistingDirectory").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  void otherIOException_should_return500() throws Exception {
    given(loader.loadFiles("/somethingNotLoadable"))
      .willThrow(new IOException());

    mvc.perform(get("/somethingNotLoadable").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isInternalServerError());
  }
}
