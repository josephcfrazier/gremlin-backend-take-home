package com.josephcfrazier.gremelin.backend.take.home.api;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.josephcfrazier.gremelin.backend.take.home.api.impl.ForismaticServiceImpl;
import com.josephcfrazier.gremelin.backend.take.home.dtos.Lang;
import com.josephcfrazier.gremelin.backend.take.home.dtos.QuoteDto;

@WireMockTest(httpPort = 8081)
class ForismaticServiceTest {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private final ForismaticService forismaticService = new ForismaticServiceImpl(
			"http://localhost:8081/forismatic/api");

	@Test
	void fetchQuote_should_throw_ForismaticServiceException_given_a_missing_lang() {
		assertThrows(ForismaticServiceException.class, () -> forismaticService.fetchQuote(null));
	}
	
	@Test
	void fetchQuote_should_throw_ForismaticServiceException_given_the_Forismatic_service_is_unavailable() {
		stubFor(post(urlPathMatching("/forismatic/api"))
				.willReturn(aResponse()
						.withStatus(503)));

		ForismaticServiceException ex = assertThrows(ForismaticServiceException.class, () -> forismaticService.fetchQuote(Lang.ENGISH));
		
		assertThat(ex).as("Error thrown indicates that Forismatic service is unavaible with a 503 http status code").hasMessageContaining("503");
	}

	@Test
	void fetchQuote_should_return_a_QuoteDto_given_a_valid_lang() {
		stubFor(post(urlPathMatching("/forismatic/api"))
				.willReturn(aResponse()
						.withStatus(200)
				        .withHeader("Content-Type", "application/json")
				        .withBody(mockQuoteDtoReturnedFromForismatic())));

		QuoteDto quote = forismaticService.fetchQuote(Lang.ENGISH);

		assertThat(quote.getQuoteAuthor()).as("The returned quote author is present").isEqualTo("Joseph Frazier");
		assertThat(quote.getQuoteText()).as("The returned quote is present")
				.isEqualTo("Just anther day of coding is all I need!");
	}

	static String mockQuoteDtoReturnedFromForismatic() {
		try {
			return OBJECT_MAPPER.writeValueAsString(QuoteDto.builder()
					.quoteText("Just anther day of coding is all I need!").quoteAuthor("Joseph Frazier").build());
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to mock QuoteDto returned from Forismati", e);
		}
	}
}
