package hr.fer.ilj.workshop.rest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

}
