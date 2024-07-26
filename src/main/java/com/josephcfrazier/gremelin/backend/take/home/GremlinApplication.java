package com.josephcfrazier.gremelin.backend.take.home;

import java.util.Scanner;

import com.github.jknack.handlebars.internal.lang3.StringUtils;
import com.josephcfrazier.gremelin.backend.take.home.api.ForismaticService;
import com.josephcfrazier.gremelin.backend.take.home.api.impl.ForismaticServiceImpl;
import com.josephcfrazier.gremelin.backend.take.home.dtos.Lang;
import com.josephcfrazier.gremelin.backend.take.home.dtos.QuoteDto;

public class GremlinApplication {
	private static final String FORSIMATIC_SERVICE_BASE_URL = "http://api.forismatic.com/api/1.0/";

	public static void main(String[] args) {
		final ForismaticService forismaticService = new ForismaticServiceImpl(FORSIMATIC_SERVICE_BASE_URL);
		try (Scanner input = new Scanner(System.in)) {
			while (true) {
				promptForUserInput();
				String line = input.nextLine();
				if ("q".equalsIgnoreCase(line)) {
					System.out.println("Goodbye!");
					break;
				} else {
					try {
						if (StringUtils.isAllBlank(line) || "e".equalsIgnoreCase(line)) {
							printCommandResponse(forismaticService.fetchQuote(Lang.ENGISH));
						} else if ("r".equalsIgnoreCase(line)) {
							printCommandResponse(forismaticService.fetchQuote(Lang.RUSSIAN));
						} else {
							System.out.println("Entry not valid, try again!");
						}
					} catch (Exception e) {
						System.out.println("Program terminating, there was an error in processing your request: "
								+ e.getMessage());
						System.exit(1);
					}
				}
			}
		}
	}

	static void printCommandResponse(QuoteDto quote) {
		System.out.println(String.format("%-8s%s", "Author", quote.getQuoteAuthor()));
		System.out.println(String.format("%-8s%s", "Quote", quote.getQuoteText()));
		System.out.println();
	}

	static void promptForUserInput() {
		System.out
				.println("Enter 'q' to quit, 'e' or press ENTER to get quote in English, 'r' to get quote in Russian:");
	}
}
