package hr.fer.ilj.workshop.rest;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.NotDirectoryException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hr.fer.ilj.workshop.files.FileLoader;
import hr.fer.ilj.workshop.rest.dto.FileInfoConverter;
import hr.fer.ilj.workshop.rest.dto.FileInfoDTO;

@RestController
public class FilesController {
  private final Logger LOG = LoggerFactory.getLogger(this.getClass());

  private FileLoader loader;

  public FilesController(FileLoader loader) {
    this.loader = loader;
  }

  @GetMapping("/**")
  public Collection<FileInfoDTO> files(HttpServletRequest request) {
    try {
      return loader.loadFiles(request.getPathInfo()).stream()
           .map(FileInfoConverter::toDTO)
           .toList();
    } catch (NotDirectoryException e) {
      LOG.info("Requested {} is not directory", e.getFile());
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getFile() + " is not directory");
    } catch (AccessDeniedException e) {
      LOG.info("Requested {} is not existing directory", e.getFile());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not load path " + e.getFile());
    } catch (IOException e) {
      LOG.error("Can not read directory " + request.getPathInfo(), e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
