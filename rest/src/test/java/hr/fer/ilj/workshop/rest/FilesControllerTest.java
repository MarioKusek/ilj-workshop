package hr.fer.ilj.workshop.rest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import hr.fer.ilj.workshop.files.FileLoader;

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

}
