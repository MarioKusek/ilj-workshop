package hr.fer.ilj.workshop.files;

import java.nio.file.Path;

public record FileInfo(String name, Path path, long size, FileType type) {
}
