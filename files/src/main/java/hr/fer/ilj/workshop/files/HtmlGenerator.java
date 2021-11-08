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

    result.append(fileInfo.name());
    result.append(" - ");
    result.append(fileInfo.size());
    result.append("B");

    return result.toString();
  }

}
