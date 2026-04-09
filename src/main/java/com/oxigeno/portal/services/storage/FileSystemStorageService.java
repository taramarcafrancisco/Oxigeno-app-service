package com.oxigeno.portal.services.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

	@Value ("${setup.uploadfolder}")
	private String uploadfolder;
	private Path rootLocation;

	FileSystemStorageService  () {
	}
	
	@Override
	public Path store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("No se recibio ningun archivo.");
			}
			Path destinationFile = this.getRootLocation().resolve(
					Paths.get(file.getOriginalFilename()))
					.normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(this.getRootLocation().toAbsolutePath())) {
				// This is a security check
				throw new StorageException(
						"Cannot store file outside current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}
			return destinationFile;
		}
		catch (IOException e) {
			throw new StorageException("Error guardando archivo.", e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.getRootLocation(), 1)
				.filter(path -> !path.equals(this.getRootLocation()))
				.map(this.getRootLocation()::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Error leyendo archivo", e);
		}

	}

	@Override
	public Path load(String filename) {
		return getRootLocation().resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);
			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(getRootLocation().toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(getRootLocation());
		}
		catch (IOException e) {
			throw new StorageException("No es posible inicializar el almacen de archivos", e);
		}
	}

	private Path getRootLocation() {
		rootLocation = Paths.get(uploadfolder);
		return rootLocation;
	}

	public void setRootLocation(Path rootLocation) {
		this.rootLocation = rootLocation;
	}
}