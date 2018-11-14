package com.lucasaquila.cursomc.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Service
public class S3Service {

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	// MultipartFile é o tipo correspondente ao arquivo que é enviado na requisição
	public URI uploadFile(MultipartFile multipartFile) {
		try {
			String filename = multipartFile.getOriginalFilename();

			// Encapsula o processo de leitura do arquivo
			InputStream inputStream;
			inputStream = multipartFile.getInputStream();
			// Armazena o tipo do arquivo
			String contentType = multipartFile.getContentType();

			return uploadFile(inputStream, filename, contentType);
		} catch (IOException e) {
			throw new RuntimeException("Erro de IO: " + e.getMessage());
		}

	}

	public URI uploadFile(InputStream inputStream, String filename, String contentType) {
		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			LOG.info("Iniciando upload");
			s3client.putObject(bucketName, filename, inputStream, meta);
			LOG.info("Upload finalizado");
			return s3client.getUrl(bucketName, filename).toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException("Erro ao converter URL para URI");
		}

	}
}
