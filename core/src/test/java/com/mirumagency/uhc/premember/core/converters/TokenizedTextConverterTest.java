package com.mirumagency.uhc.premember.core.converters;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TokenizedTextConverterTest {

  private String firstToken = "firstToken";
  private String firstTokenValue = "1";
  private String secondToken = "secondToken";
  private String secondTokenValue = "2";
  private String thirdToken = "thirdToken";
  private String thirdTokenValue = "3";
  private String rawTextFormat = "First token: {%s}, second token: {%s}, third token: {%s}";
  private String rawText = String.format(rawTextFormat, firstToken, secondToken, thirdToken);

  @Test
  void shouldReplaceAllTokensInText_whenMapContainsEveryToken() {
    // given
    ImmutableMap<String, String> tokensMap = ImmutableMap
        .of(
            firstToken, firstTokenValue,
            secondToken, secondTokenValue,
            thirdToken, thirdTokenValue
        );
    // when
    String convertedText = TokenizedTextConverter.convert(rawText, tokensMap);
    // then
    assertEquals(
        "First token: " + firstTokenValue + ", second token: " + secondTokenValue
            + ", third token: " + thirdTokenValue,
        convertedText);
  }

  @Test
  void shouldNotReplaceTokensInText_whenMapIsMissingSomeTokens() {
    // given
    ImmutableMap<String, String> tokensMap = ImmutableMap
        .of(firstToken, firstTokenValue, secondToken, secondTokenValue);
    // when
    String convertedText = TokenizedTextConverter.convert(rawText, tokensMap);
    // then
    assertEquals(
        "First token: " + firstTokenValue + ", second token: " + secondTokenValue
            + ", third token: {" + thirdToken
            + "}",
        convertedText);
  }

  @Test
  void shouldNotReplaceAnyTokenInText_whenMapIsNullOrEmpty() {
    // given
    ImmutableMap<String, String> emptyTokensMap = ImmutableMap.of();
    // when
    String convertedTextWithEmptyMap = TokenizedTextConverter.convert(rawText, emptyTokensMap);
    String convertedTextWithNullMap = TokenizedTextConverter.convert(rawText, null);
    // then
    assertAll(
        () -> assertEquals(rawText, convertedTextWithEmptyMap),
        () -> assertEquals(rawText, convertedTextWithNullMap));
  }

  @Test
  void shouldReturnNull_whenNullTextIsProvided() {
    // given
    ImmutableMap<String, String> tokensMap = ImmutableMap
        .of(firstToken, firstTokenValue, secondToken, secondTokenValue, thirdToken,
            thirdTokenValue);
    // when
    String convertedText = TokenizedTextConverter.convert(null, tokensMap);
    // then
    assertNull(convertedText);
  }

  @Test
  void shouldReturnRawText_whenNoTokensAreIncluded() {
    // given
    String textWithNoTokens = "Text with no tokens";
    ImmutableMap<String, String> tokensMap = ImmutableMap
        .of(firstToken, firstTokenValue, secondToken, secondTokenValue, thirdToken,
            thirdTokenValue);
    // when
    String convertedText = TokenizedTextConverter.convert(textWithNoTokens, tokensMap);
    // then
    assertEquals(textWithNoTokens, convertedText);
  }

  @Test
  void shouldReturnRawTextWithEmptyTokens_whenMapDoesNotContainEmptyTokens() {
    // given
    String textWithEmptyTokens = "Text with empty tokens: {}, {}, {}";
    ImmutableMap<String, String> tokensMap = ImmutableMap
        .of(firstToken, firstTokenValue, secondToken, secondTokenValue, thirdToken,
            thirdTokenValue);
    // when
    String convertedText = TokenizedTextConverter.convert(textWithEmptyTokens, tokensMap);
    // then
    assertEquals(textWithEmptyTokens, convertedText);
  }

  @Test
  void shouldReplaceAllTokensInText_whenThereAreEncodedTokens() {
    // given
    ImmutableMap<String, String> tokensMap = ImmutableMap
        .of(
            "firstToken", "firstTokenValue",
            "secondToken", "secondTokenValue"
        );
    // when
    String convertedText = TokenizedTextConverter
        .convert("Some silly text {firstToken} with tokens %7BsecondToken%7D", tokensMap);
    // then
    assertEquals(
        "Some silly text firstTokenValue with tokens secondTokenValue",
        convertedText);
  }

  @Test
  void shouldNotReplaceAllTokensInText_whenThereAreEncodedTokens_andSomeTokenMissing() {
    // given
    ImmutableMap<String, String> tokensMap = ImmutableMap
        .of(
            "firstToken", "firstTokenValue"
        );
    // when
    String convertedText = TokenizedTextConverter
        .convert("Some silly text {firstToken} with tokens %7BsecondToken%7D", tokensMap);
    // then
    assertEquals(
        "Some silly text firstTokenValue with tokens %7BsecondToken%7D",
        convertedText);
  }

  @Test
  void shouldReplaceAllTokensInText_whenThereAreSquareBrackets() {
    // given
    ImmutableMap<String, String> tokensMap = ImmutableMap
        .of(
            "firstToken", "firstTokenValue",
            "secondToken", "secondTokenValue"
        );
    // when
    String convertedText = TokenizedTextConverter
        .convert("Some silly text {firstToken} with tokens #[secondToken]", tokensMap);
    // then
    assertEquals(
        "Some silly text firstTokenValue with tokens secondTokenValue",
        convertedText);
  }

  @Test
  void shouldNotReplaceAllTokensInText_whenThereAreSquareBrackets_andSomeTokenMissing() {
    // given
    ImmutableMap<String, String> tokensMap = ImmutableMap
        .of(
            "firstToken", "firstTokenValue"
        );
    // when
    String convertedText = TokenizedTextConverter
        .convert("Some silly text {firstToken} with tokens #[secondToken]", tokensMap);
    // then
    assertEquals(
        "Some silly text firstTokenValue with tokens #[secondToken]",
        convertedText);
  }

  @Test
  void shouldReplaceAllTokensInText_whenThereAreAllTokenTypes() {
    // given
    ImmutableMap<String, String> tokensMap = ImmutableMap
        .of(
            "firstToken", "firstTokenValue",
            "secondToken", "secondTokenValue"
        );
    // when
    String convertedText = TokenizedTextConverter
        .convert("Some silly text {firstToken} with tokens %7BsecondToken%7D #[secondToken]",
            tokensMap);
    // then
    assertEquals(
        "Some silly text firstTokenValue with tokens secondTokenValue secondTokenValue",
        convertedText);
  }

  @Test
  void shouldReplaceAllTokensInText_whenThereAreTokensInResolvedText() {
    // given
    ImmutableMap<String, String> tokensMap = ImmutableMap
        .of(
            "firstToken", "token with a {secondToken}",
            "secondToken", "secondTokenValue"
        );
    // when
    String convertedText = TokenizedTextConverter
        .convert("Some silly text with {firstToken}", tokensMap);
    // then
    assertEquals(
        "Some silly text with token with a secondTokenValue",
        convertedText);
  }

  @Test
  void shouldReplaceAllTokensInText_whenThereAreTokensInResolvedText_andHandleCircularDependency() {
    // given
    ImmutableMap<String, String> tokensMap = ImmutableMap
        .of(
            "firstToken", "token with a {secondToken}",
            "secondToken", "secondTokenValue {firstToken}"
        );
    // when
    String convertedText = TokenizedTextConverter
        .convert("Some silly text with {firstToken}", tokensMap);
    // then
    assertEquals(
        "Some silly text with token with a secondTokenValue {firstToken}",
        convertedText);
  }

  @Test
  void shouldEncodeUrlsInTokens() {
    //given
    Map<String, String> tokens = ImmutableMap.<String, String>builder()
        .put("tokenWithLink",
            "https://stackoverflow.com/questions/4737841/urlencoder-not-able-to-translate-space-character")
        .put("tokenWithSpacesLink",
            "https://dev-aem-author.eipm.uhc.mirum.agency/content/dam/premember/documents/video-transcripts/Health Care 101 Video Script.pdf")
        .put("tokenWithSpacesPdf",
            "/content/dam/premember/documents/video-transcripts/Health Care 101 Video Script.pdf")
        .put("regularTokenWithSpaces", "This is some text")
        .put("emptyToken", "")
        .put("checkboxTokenPdf", "true")
        .put("checkboxTokenLink", "false")
        .build();

    //when
    Map<String, String> encodedTokens = TokenizedTextConverter.encodeUrlsIn(tokens);

    //then
    assertNotNull(encodedTokens);
    assertEquals(
        "https://stackoverflow.com/questions/4737841/urlencoder-not-able-to-translate-space-character",
        encodedTokens.get("tokenWithLink"));
    assertEquals(
        "https://dev-aem-author.eipm.uhc.mirum.agency/content/dam/premember/documents/video-transcripts/Health%20Care%20101%20Video%20Script.pdf",
        encodedTokens.get("tokenWithSpacesLink"));
    assertEquals(
        "/content/dam/premember/documents/video-transcripts/Health%20Care%20101%20Video%20Script.pdf",
        encodedTokens.get("tokenWithSpacesPdf"));
    assertEquals("This is some text", encodedTokens.get("regularTokenWithSpaces"));
    assertEquals("", encodedTokens.get("emptyToken"));
    assertEquals("true", encodedTokens.get("checkboxTokenPdf"));
    assertEquals("false", encodedTokens.get("checkboxTokenLink"));
  }
}