package com.josephcfrazier.gremelin.backend.take.home.api;

import com.josephcfrazier.gremelin.backend.take.home.dtos.Lang;
import com.josephcfrazier.gremelin.backend.take.home.dtos.QuoteDto;

public interface ForismaticService {
	/**
	 * Fetch quote from the Forismatic service
	 * 
	 * @param lang The language the quote should be returned in
	 * @return QuoteDto The localized quote
	 * @throws ForismaticServiceException
	 */
	QuoteDto fetchQuote(Lang lang) throws ForismaticServiceException;
}
