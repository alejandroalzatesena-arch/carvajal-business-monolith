package com.carvajalecomers.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * Integracion con Cloudinary para imagenes de producto.
 *
 * El bean Cloudinary se construye en CloudinaryConfig a partir de variables de
 * entorno; aqui no hay secretos. upload() sube la imagen y devuelve la URL
 * segura (secure_url) que se guarda en product.imageUrl.
 */
@Service
public class CloudinaryService {

    private static final String UPLOAD_MARKER = "/image/upload/";

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Sube un archivo de imagen y devuelve la URL segura (secure_url).
     */
    public String upload(MultipartFile file) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.emptyMap()
            );
            return (String) result.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Error al subir la imagen a Cloudinary", e);
        }
    }

    /**
     * Elimina el asset de Cloudinary asociado a una URL del propio Cloudinary.
     * Devuelve true si se elimino. Si la URL no es de Cloudinary no hace nada.
     */
    public boolean deleteByUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank() || !imageUrl.contains("cloudinary.com/")) {
            return false;
        }
        String publicId = extractPublicId(imageUrl);
        if (publicId == null || publicId.isBlank()) {
            return false;
        }
        try {
            Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equalsIgnoreCase(String.valueOf(result.get("result")));
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la imagen de Cloudinary", e);
        }
    }

    private String extractPublicId(String url) {
        String normalized = url;

        int queryIndex = normalized.indexOf('?');
        if (queryIndex >= 0) {
            normalized = normalized.substring(0, queryIndex);
        }

        int uploadIndex = normalized.indexOf(UPLOAD_MARKER);
        if (uploadIndex < 0) {
            return null;
        }
        String after = normalized.substring(uploadIndex + UPLOAD_MARKER.length());

        String[] parts = after.split("/");
        int start = 0;
        if (parts.length > 0 && parts[0].matches("v\\d+")) {
            start = 1;
        }

        String publicId = String.join("/", Arrays.copyOfRange(parts, start, parts.length));
        int dotIndex = publicId.lastIndexOf('.');
        return dotIndex >= 0 ? publicId.substring(0, dotIndex) : publicId;
    }
}