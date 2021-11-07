package hr.fer.ilj.workshop.files;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

class FileLoaderTest {

  private Path root;
  private FileLoader loader;

  @BeforeEach
  void setup() {
    FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
    root = fs.getPath("/root");
    loader = new FileLoader(root);
  }

  @Test
  void loadFileList() throws Exception {
    Files.createDirectories(root.resolve("dir1"));
    Files.createFile(root.resolve("file1"));

    List<FileInfo> files = loader.loadFiles("/");

    String directoryRepresentation = files.stream()
        .map(fi -> fi.name())
        .sorted()
        .collect(Collectors.joining(" "));
    assertThat(directoryRepresentation).isEqualTo("dir1 file1");
  }

  @Test
  void convertingDirectoryToFileInfo() throws Exception {
    Path dir1 = Files.createDirectories(root.resolve("dir1"));

    FileInfo fileInfo = loader.toFileInfo(dir1);

    assertThat(fileInfo.name()).isEqualTo("dir1");
    assertThat(fileInfo.path()).hasToString("dir1");
    assertThat(fileInfo.size()).isZero();
    assertThat(fileInfo.type()).isEqualTo(FileType.DIRECTORY);
  }

  @Test
  void convertingFileToFileInfo() throws Exception {

    Path dir1 = Files.createDirectories(root.resolve("dir1"));
    Path file1 = Files.createFile(dir1.resolve("file1"));
    Files.copy(new ByteArrayInputStream(new byte[] {0, 1, 3, 4, 5}), file1, StandardCopyOption.REPLACE_EXISTING);

    FileInfo fileInfo = loader.toFileInfo(file1);

    assertThat(fileInfo.name()).isEqualTo("file1");
    assertThat(fileInfo.path()).hasToString("dir1/file1");
    assertThat(fileInfo.size()).isEqualTo(5);
    assertThat(fileInfo.type()).isEqualTo(FileType.FILE);
  }

}
