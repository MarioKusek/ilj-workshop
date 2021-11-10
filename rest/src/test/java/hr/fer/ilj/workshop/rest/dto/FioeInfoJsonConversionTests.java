package hr.fer.ilj.workshop.rest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import hr.fer.ilj.workshop.files.FileInfo;
import hr.fer.ilj.workshop.files.FileType;

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

  @Test
  void convertingFileInfoToDTO() throws Exception {
    FileInfo fileInfo = new FileInfo("f1", Path.of("d1/d2/f1"), 146, FileType.FILE);

    FileInfoDTO dto = FileInfoConverter.toDTO(fileInfo);

    assertThat(dto.name()).isEqualTo("f1");
    assertThat(dto.path()).isEqualTo("d1/d2/f1");
    assertThat(dto.size()).isEqualTo(146);
    assertThat(dto.type()).isEqualTo("FILE");

  }

}
