package hr.fer.ilj.workshop.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileLoader {
  private Logger LOG = LoggerFactory.getLogger(this.getClass());

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

  public FileInfo toFileInfo(Path path) {
    try {
      return new FileInfo(path.getFileName().toString(),
          root.relativize(path),
          Files.size(path),
          Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE
          );
    } catch (IOException e) {
      LOG.error("Can not create FileInfo from Path, returning null", e);
      return null;
    }
  }

}
