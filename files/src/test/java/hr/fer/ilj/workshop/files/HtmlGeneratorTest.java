package hr.fer.ilj.workshop.files;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class HtmlGeneratorTest {

  @Test
  void generateHtmlForEmptyList() {
    HtmlGenerator generator = new HtmlGenerator();
    assertThat(generator.generate(List.of())).isEqualTo("""
        <p>File list:</p>
        <p>empty</p>
        """);
  }

  @Test
  void generateHtmlForOneElement() {
    HtmlGenerator generator = new HtmlGenerator() {
      @Override
      public String toHtml(FileInfo fileInfo) {
        return fileInfo.name();
      }
    };
    List<FileInfo> list = List.of(new FileInfo("ime", Path.of("dir/ime"), 0, FileType.FILE));

    assertThat(generator.generate(list)).isEqualTo("""
        <p>File list:</p>
        <ul>
          <li>ime</li>
        </ul>
        """);
  }

  @Test
  void generateHtmlForManyElement() {
    HtmlGenerator generator = new HtmlGenerator() {
      @Override
      public String toHtml(FileInfo fileInfo) {
        return fileInfo.name();
      }
    };
    List<FileInfo> list = List.of(
        new FileInfo("ime1", Path.of("dir/ime1"), 0, FileType.FILE),
        new FileInfo("ime2", Path.of("dir/ime2"), 0, FileType.FILE),
        new FileInfo("ime3", Path.of("dir/ime3"), 0, FileType.FILE));

    assertThat(generator.generate(list)).isEqualTo("""
        <p>File list:</p>
        <ul>
          <li>ime1</li>
          <li>ime2</li>
          <li>ime3</li>
        </ul>
        """);
  }

  @Test
  void generateHtmlForItem_file() throws Exception {
    HtmlGenerator generator = new HtmlGenerator();

    FileInfo fileInfo = new FileInfo("ime1", Path.of("dir/ime1"), 345, FileType.FILE);

    assertThat(generator.toHtml(fileInfo)).isEqualTo("""
            ime1 - 345 B\
            """);
  }

  @Test
  void generateHtmlForItem_dir() throws Exception {
    HtmlGenerator generator = new HtmlGenerator();

    FileInfo fileInfo = new FileInfo("subdir1", Path.of("dir/subdir1"), 0, FileType.DIRECTORY);

    assertThat(generator.toHtml(fileInfo)).isEqualTo("""
        <a href="dir/subdir1">subdir1</a>\
        """);
  }

  private record ItemSize(long size, String representation) {}

  static Stream<ItemSize> sizeProvider() {
    return Stream.of(
        new ItemSize(0, "0 B"),
        new ItemSize(1023, "1023 B"),
        new ItemSize(1024, "1 KB"),
        new ItemSize(2047, "1 KB"),
        new ItemSize(2048, "2 KB"),
        new ItemSize(1024 * 1024 - 1, "1023 KB")
        );
  }

  @ParameterizedTest
  @MethodSource("sizeProvider")
  void generateSizeOfItem(ItemSize itemSize) {
    HtmlGenerator generator = new HtmlGenerator();

    FileInfo fileInfo = new FileInfo("not important name", null, itemSize.size(), FileType.FILE);

    assertThat(generator.toHtml(fileInfo)).endsWith(itemSize.representation());

  }
}

