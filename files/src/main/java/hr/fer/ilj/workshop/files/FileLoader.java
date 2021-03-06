package hr.fer.ilj.workshop.files;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileLoader {
  private Logger LOG = LoggerFactory.getLogger(this.getClass());

  private Path root;

  public FileLoader(Path root) {
    this.root = root.toAbsolutePath();
  }

  /**
   * Load file infos of path.
   *
   * @param path absolute path which is translated to be relative to root
   * @return list of file infos
   * @throws IOException
   */
  public List<FileInfo> loadFiles(String path) throws IOException {
    if(path.contains(".."))
      throw new AccessDeniedException("path");
    Path dir = root.resolve(path.substring(1));
    Stream<FileInfo> fileStream = Files.list(dir)
        .map(this::toFileInfo);

    if(path.equals("/"))
      return fileStream.toList();
    else
      return Stream.concat(Stream.of(new FileInfo("..", root.relativize(dir.getParent()), 0, FileType.DIRECTORY)), fileStream).toList();
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
