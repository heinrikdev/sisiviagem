package ufg.br.fabricasw.jatai.swsisviagem.storage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ufg.br.fabricasw.jatai.swsisviagem.domain.arquivo.Anexo;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    @SuppressWarnings("null")
    public void store(Anexo anexo) {

        if (anexo.getId() == null) {

            try {

                MultipartFile file = anexo.getFile();

                String filename = UUID.randomUUID().toString();
                filename = filename + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

                Path filePath = FileSystemStorageService.this.rootLocation.resolve(filename);

                Files.write(filePath, anexo.getFileBytes());
                anexo.setCaminho(filePath.toString());

            } catch (IOException ex) {

                Logger.getLogger(FileSystemStorageService.class.getName()).log(Level.SEVERE, null, ex);
                Logger.getLogger(FileSystemStorageService.class.getName()).log(Level.SEVERE, null, "ERROR ao tentar salvar arquivo");
            }
        }
    }

    @Override
    @SuppressWarnings("null")
    public void store(List<Anexo> anexos) {

        anexos.stream().forEach((Anexo anexo) -> {

            if (anexo.getId() == null) {

                try {

                    MultipartFile file = anexo.getFile();
                    
                    String filename = UUID.randomUUID().toString();
                    filename = filename + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

                    Path filePath = FileSystemStorageService.this.rootLocation.resolve(filename);

                    Files.write(filePath, anexo.getFileBytes());
                    anexo.setCaminho(filePath.toString());

                } catch (IOException ex) {

                    Logger.getLogger(FileSystemStorageService.class.getName()).log(Level.SEVERE, null, ex);
                    Logger.getLogger(FileSystemStorageService.class.getName()).log(Level.SEVERE, null, "ERROR ao tentar salvar arquivo");
                }
            }

        });
    }

    /**
     * Delta os anexos gravados no disco.
     *
     * @param anexos
     */
    @Override
    public void deletar(List<Anexo> anexos) {

        anexos.stream().forEach((Anexo anexo) -> {
            try {

                if (anexo.getCaminho() != null && anexo.getCaminho().trim().length() > 0) {

                    Path file = Paths.get(anexo.getCaminho());

                    if (Files.exists(file)) {

                        Files.delete(file);
                    }
                }

            } catch (IOException ex) {

                Logger.getLogger(FileSystemStorageService.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    /**
     * Delta o anexo gravados no disco.
     *
     * @param anexo
     */
    @Override
    public void deletar(Anexo anexo) {

        try {

            if (anexo.getCaminho() != null && anexo.getCaminho().trim().length() > 0) {

                Path file = Paths.get(anexo.getCaminho());
                Files.delete(file);
            }

        } catch (IOException ex) {

            Logger.getLogger(FileSystemStorageService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;

            } else {

                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void store(MultipartFile file) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        try {

            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }

            if (filename.contains("..")) {

                throw new StorageException("Cannot store file with relative path outside current directory " + filename);
            }

            filename = UUID.randomUUID().toString();
            filename = filename + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

            Path filePath = FileSystemStorageService.this.rootLocation.resolve(filename);

            Files.write(filePath, file.getBytes());
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {

            throw new StorageException("Failed to store file " + filename, e);
        }
    }
}
