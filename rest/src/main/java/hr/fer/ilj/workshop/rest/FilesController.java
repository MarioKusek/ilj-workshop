package hr.fer.ilj.workshop.rest;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.ilj.workshop.files.FileLoader;
import hr.fer.ilj.workshop.rest.dto.FileInfoConverter;
import hr.fer.ilj.workshop.rest.dto.FileInfoDTO;

@RestController
public class FilesController {

  private FileLoader loader;

  public FilesController(FileLoader loader) {
    this.loader = loader;
  }

  @GetMapping("/**")
  public Collection<FileInfoDTO> files(HttpServletRequest request) throws IOException {
    return loader.loadFiles(request.getPathInfo()).stream()
         .map(FileInfoConverter::toDTO)
         .toList();
  }

}
