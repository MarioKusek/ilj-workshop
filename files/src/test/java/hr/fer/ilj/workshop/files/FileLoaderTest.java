package hr.fer.ilj.workshop.files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
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
    FileHelper.structureBuilder(root)
      .directory("dir1")
      .file("file1", 0);

    List<FileInfo> files = loader.loadFiles("/");

    assertThat(extractDirectoryRepresentation(files, fi -> fi.name())).isEqualTo("dir1 file1");
    assertThat(extractDirectoryRepresentation(files, fi -> fi.path().toString())).isEqualTo("dir1 file1");
  }

  @Test
  void loadFilesWithSpaces() throws Exception {
    FileHelper.structureBuilder(root)
    .directory("dir 1")
    .file("file 1", 0);

    List<FileInfo> files = loader.loadFiles("/");

    assertThat(extractDirectoryRepresentation(files, fi -> fi.name())).isEqualTo("dir 1 file 1");
    assertThat(extractDirectoryRepresentation(files, fi -> fi.path().toString())).isEqualTo("dir 1 file 1");
  }

  @Test
  void loadRootFileList_shouldNotContainParentDir() throws Exception {
    FileHelper.structureBuilder(root)
      .directory("dir1")
      .file("file1", 0);

    List<FileInfo> files = loader.loadFiles("/");

    assertThat(extractDirectoryRepresentation(files, fi -> fi.name())).doesNotContain("..");
  }

  @Test
  void loadSubdirectoryFileList_shouldContainParentDir() throws Exception {
    FileHelper.structureBuilder(root)
      .file("dir1/file1", 0)
      .directory("dir1/dir2");

    List<FileInfo> files = loader.loadFiles("/dir1");

    assertThat(extractDirectoryRepresentation(files, fi -> fi.name())).contains("..");
    assertThat(extractDirectoryRepresentation(files, fi -> fi.path().toString())).isEqualTo(" dir1/dir2 dir1/file1");
  }

  @Test
  void loadEmptyDirectory() throws Exception {
    FileHelper.structureBuilder(root)
      .directory("dir1/dir2");

    List<FileInfo> files = loader.loadFiles("/dir1/dir2");

    assertThat(files).hasSize(1);
    assertThat(extractDirectoryRepresentation(files, fi -> fi.name())).contains("..");
    assertThat(extractDirectoryRepresentation(files, fi -> fi.path().toString())).isEqualTo("dir1");
  }

  @Test
  void loadRootParent() throws Exception {
    assertThrows(AccessDeniedException.class, () -> {
      loader.loadFiles("/..");
    });

  }

  private String extractDirectoryRepresentation(List<FileInfo> files,
      Function<? super FileInfo, ? extends String> mapper) {
    String directoryRepresentation = files.stream()
        .map(mapper)
        .sorted()
        .collect(Collectors.joining(" "));
    return directoryRepresentation;
  }

  @Test
  void convertingDirectoryToFileInfo() throws Exception {
    FileHelper.structureBuilder(root)
      .directory("dir1");
    Path dir1 = root.resolve("dir1");

    FileInfo fileInfo = loader.toFileInfo(dir1);

    assertThat(fileInfo.name()).isEqualTo("dir1");
    assertThat(fileInfo.path()).hasToString("dir1");
    assertThat(fileInfo.size()).isZero();
    assertThat(fileInfo.type()).isEqualTo(FileType.DIRECTORY);
  }

  @Test
  void convertingFileToFileInfo() throws Exception {
    FileHelper.structureBuilder(root)
      .file("dir1/file1", 5);

    FileInfo fileInfo = loader.toFileInfo(root.resolve("dir1").resolve("file1"));

    assertThat(fileInfo.name()).isEqualTo("file1");
    assertThat(fileInfo.path()).hasToString("dir1/file1");
    assertThat(fileInfo.size()).isEqualTo(5);
    assertThat(fileInfo.type()).isEqualTo(FileType.FILE);
  }

}
