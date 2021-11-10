package hr.fer.ilj.workshop.rest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class FioeInfoJsonConversionTests {
  @Autowired
  private JacksonTester<FileInfoDTO> json;

  @Test
  void convertingDtoToJson() throws Exception {
    FileInfoDTO dto = new FileInfoDTO("f1", "d1/d2/f1", 135, "FILE");
    assertThat(json.write(dto)).isEqualTo("""
            {
              "name": "f1",
              "path": "d1/d2/f1",
              "size": 135,
              "type": "FILE"
            }
            """);
  }

}
