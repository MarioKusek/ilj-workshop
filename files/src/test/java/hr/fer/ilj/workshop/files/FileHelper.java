package hr.fer.ilj.workshop.files;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * It is helping creating files in directory structure.
 */
public class FileHelper {

  /**
   * Creates structure builder.
   *
   * @param dir base directory where file structure will be created
   * @return builder
   */
  public static FileStructureBuilder structureBuilder(Path dir) {
    return new FileStructureBuilder(dir);
  }

  /**
   * File structure builder.
   */
  public static class FileStructureBuilder {
    private static byte[] buffer = new byte[1000];
    private Path rootDir;

    /**
     * Creates builder.
     *
     * @param baseDir directory that will contain files
     */
    public FileStructureBuilder(Path baseDir) {
      this.rootDir = baseDir;
    }

    /**
     * Creates file with specified size.
     *
     * @param fileName file name relative to the base dir
     * @param size file size
     * @return builder
     * @throws IOException if the files can not be created
     */
    public FileStructureBuilder file(String fileName, int size) throws IOException {
      Path filePath = rootDir.resolve(fileName);
      Files.createDirectories(filePath.getParent());

      createFile(filePath, size);
      return this;
    }

    private void createFile(Path file, int size) throws FileNotFoundException, IOException {
      try (OutputStream fos = Files.newOutputStream(file)) {
        int loopCount = size / buffer.length;
        int restToWrite = size % buffer.length;

        for (int i = 0; i < loopCount; i++) {
          fos.write(buffer);
        }
        fos.write(buffer, 0, restToWrite);
      }
    }

    /**
     * Creates directory relative to base dir. If parent directories does not exist then they will be created.
     * @param dirName directory name relative to base dir
     * @return builder
     * @throws IOException if it can not create directory
     */
    public FileStructureBuilder directory(String dirName) throws IOException {
      Path dirPath = rootDir.resolve(dirName);
      Files.createDirectories(dirPath);

      return this;
    }
  }
}
