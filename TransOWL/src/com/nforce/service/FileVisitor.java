package com.nforce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import com.nforce.model.FileVisitorCallback;

public class FileVisitor {

	private final File directory;
	private final String wildcard;
	private final Integer recursionLevel;
	
	public FileVisitor(File directory, String wildcard, Integer recursionLevel) {
		this.directory = directory;
		this.wildcard = wildcard;
		this.recursionLevel = recursionLevel;
	}
	
	public void runVisitor(FileVisitorCallback callback) {
		Path startDir = directory.toPath();
		EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
		try {
			Files.walkFileTree(startDir, opts, recursionLevel, new SimpleFileVisitor<Path>() {
				private final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + wildcard);
				
				@Override
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {
					Path name = file.getFileName();
					if(name != null && matcher.matches(name)) {
						callback.visit(file.toFile());
					}

					return super.visitFile(file, attrs);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
