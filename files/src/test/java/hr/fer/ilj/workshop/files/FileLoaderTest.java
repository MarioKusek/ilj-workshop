package hr.fer.ilj.workshop.files;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

class FileLoaderTest {

  @Test
  void loadFileList() throws Exception {
    FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
    Path root = fs.getPath("/root");
    Files.createDirectories(root.resolve("dir1"));
    Files.createFile(root.resolve("file1"));

    FileLoader loader = new FileLoader(root);
    List<FileInfo> files = loader.loadFiles("/");

    assertThat(files.stream().map(fi -> fi.name()).sorted().collect(Collectors.joining(" "))).isEqualTo("dir1 file1");
  }

}
