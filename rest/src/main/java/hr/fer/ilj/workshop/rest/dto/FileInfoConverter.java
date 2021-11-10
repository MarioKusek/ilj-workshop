package hr.fer.ilj.workshop.rest.dto;

import hr.fer.ilj.workshop.files.FileInfo;

public class FileInfoConverter {

  public static FileInfoDTO toDTO(FileInfo fileInfo) {
    return new FileInfoDTO(fileInfo.name(), fileInfo.path().toString(), fileInfo.size(), fileInfo.type().toString());

  }

}
