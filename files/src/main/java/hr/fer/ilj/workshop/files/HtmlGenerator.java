package hr.fer.ilj.workshop.files;

import java.util.List;

public class HtmlGenerator {

  public String generate(List<FileInfo> list) {
    StringBuilder body = new StringBuilder();

    body.append("<p>File list:</p>\n");

    if(list.isEmpty()) {
      body.append("<p>empty</p>\n");
    } else {
      body.append("<ul>\n");
      list.forEach(fi -> body.append("  <li>").append(toHtml(fi)).append("</li>\n"));
      body.append("</ul>\n");
    }

    return body.toString();
  }

  public String toHtml(FileInfo fileInfo) {
    StringBuilder result = new StringBuilder();

    if(fileInfo.type() == FileType.FILE)
      result.append(fileToHtml(fileInfo));
    else
      result.append(dirToHtml(fileInfo));

    return result.toString();
  }

  private String dirToHtml(FileInfo fileInfo) {
    return String.format("""
            <a href="/%s">%s</a>\
            """, fileInfo.path().toString(), fileInfo.name());
  }

  private String fileToHtml(FileInfo fileInfo) {
    return String.format("%s - %s", fileInfo.name(), calculateSizeInUnits(fileInfo));
  }

  private String calculateSizeInUnits(FileInfo fileInfo) {
    if(sizeInMBs(fileInfo) != 0)
      return sizeInMBs(fileInfo) + " MB";
    else if(sizeInKBs(fileInfo) != 0)
      return sizeInKBs(fileInfo) + " KB";
    else
      return fileInfo.size() + " B";
  }

  private long sizeInKBs(FileInfo fileInfo) {
    return fileInfo.size() / 1024;
  }

  private long sizeInMBs(FileInfo fileInfo) {
    return fileInfo.size() / 1024 / 1024;
  }

}
