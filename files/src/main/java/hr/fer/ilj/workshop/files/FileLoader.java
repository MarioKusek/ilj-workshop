package hr.fer.ilj.workshop.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileLoader {

  private Path root;

  public FileLoader(Path root) {
    this.root = root.toAbsolutePath();
  }

  public List<FileInfo> loadFiles(String path) throws IOException {
    Path dir = root.resolve(path.substring(1));
    return Files.list(dir)
        .map(this::toFileInfo)
        .toList();
  }

  // TODO test
  public FileInfo toFileInfo(Path path) {
    return new FileInfo(path.getFileName().toString(),
        null, //path.toAbsolutePath().toString().substring(root.toString().length()+1),
        0, //0,
        null //? path.toFile().isDirectory() : FileType.DIRECTORY : FileType.FILE
        );
  }

}
