package com.josephcfrazier.gremelin.backend.take.home.api.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.josephcfrazier.gremelin.backend.take.home.api.ForismaticService;
import com.josephcfrazier.gremelin.backend.take.home.api.ForismaticServiceException;
import com.josephcfrazier.gremelin.backend.take.home.dtos.Lang;
import com.josephcfrazier.gremelin.backend.take.home.dtos.QuoteDto;

public class ForismaticServiceImpl implements ForismaticService {
	private static final Charset UTF8 = StandardCharsets.UTF_8;

	private final HttpClient httpClient;
	private final String forismaticBaseUrl;
	private final ObjectMapper objectMapper;

	public ForismaticServiceImpl(String forismaticBaseUrl) {
		this.forismaticBaseUrl = forismaticBaseUrl;
		this.httpClient = HttpClient.newHttpClient();
		this.objectMapper = new ObjectMapper();

	}

	@Override
	public QuoteDto fetchQuote(Lang lang) throws ForismaticServiceException {
		try {
			Objects.requireNonNull(lang, "Missing lang value, lang can not be null");
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(forismaticBaseUrl))
					.headers("Content-Type", "application/x-www-form-urlencoded")
					.POST(HttpRequest.BodyPublishers
							.ofString(encode(Map.of("method", "getQuote", "format", "json", "lang", lang.id()))))
					.build();

			return createFromHttpResponse(httpClient.send(request, BodyHandlers.ofString()));
		} catch (NullPointerException e) {
			throw new ForismaticServiceException(e.getMessage());
		} catch (URISyntaxException e) {
			throw new ForismaticServiceException(
					"Failed to create a valid connection to the Forismatic service, " + e.getMessage());
		} catch (IOException | InterruptedException e) {
			throw new ForismaticServiceException("Failed to contact the Forismatic service at " + forismaticBaseUrl
					+ ". Please check teh connection and try again.");
		}
	}

	protected QuoteDto createFromHttpResponse(final HttpResponse<String> resp) {
		if (200 == resp.statusCode()) {
			String body = resp.body();
			try {
				return objectMapper.readValue(body, QuoteDto.class);
			} catch (Exception e) {
				throw new ForismaticServiceException(
						"Failed to process quote received from the Forismatic service at " + forismaticBaseUrl);
			}
		} else {
			throw new ForismaticServiceException("Failed to get quote from the Forismatic service at "
					+ forismaticBaseUrl + ". Http status code = " + resp.statusCode());
		}
	}

	protected String encode(final Map<String, String> form) {
		return form.entrySet().stream().map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), UTF8))
				.collect(Collectors.joining("&"));
	}
}
