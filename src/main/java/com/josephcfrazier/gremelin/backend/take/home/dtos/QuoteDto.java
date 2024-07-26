package com.josephcfrazier.gremelin.backend.take.home.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteDto implements Serializable {
	private static final long serialVersionUID = -1323086377795917549L;

	private String quoteLink;
	private String quoteText;
	private String senderName;
	private String senderLink;
	private String quoteAuthor;
}
