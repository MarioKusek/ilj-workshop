package hr.fer.ilj.workshop.rest;

import java.util.Collection;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.ilj.workshop.files.FileLoader;
import hr.fer.ilj.workshop.rest.dto.FileInfoDTO;

@RestController
public class FilesController {

  private FileLoader loader;

  public FilesController(FileLoader loader) {
    this.loader = loader;
  }

  @GetMapping("/**")
  public Collection<FileInfoDTO> files() {
    return List.of();
  }



}
