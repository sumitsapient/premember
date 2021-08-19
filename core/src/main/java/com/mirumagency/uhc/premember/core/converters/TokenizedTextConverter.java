package com.mirumagency.uhc.premember.core.converters;

import static com.google.common.net.UrlEscapers.urlFragmentEscaper;
import static com.mirumagency.uhc.premember.core.util.MapUtil.mapValue;
import static com.mirumagency.uhc.premember.core.util.MapUtil.toMap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used for converting raw text into tokenized text, in the mean of replacing tokens from raw
 * text into adequate token values.
 */
public final class TokenizedTextConverter {

  private static final Logger logger = LoggerFactory.getLogger(TokenizedTextConverter.class);

  private static final List<String> TOKEN_URL_SUFFIXES = ImmutableList.of("Link", "Pdf");

  private TokenizedTextConverter() {
    // intentionally empty
  }

  /**
   * Replaces all tokens in the given text with adequate token values present in the map
   *
   * @param rawText   text with tokens as placeholders
   * @param tokensMap map containing tokens where each entry key should be a token name and the
   *                  corresponding entry value should be a String representing given token value
   * @return converted text with replaced token values for placeholders
   */
  public static String convert(String rawText, Map<String, String> tokensMap) {
    if (StringUtils.isBlank(rawText) || tokensMap == null || tokensMap.isEmpty()) {
      logger.debug("Can't convert text, because no tokens are provided or the text is blank");
      return rawText;
    }
    Map<String, String> tokensMapWithEncodedUrls = encodeUrlsIn(tokensMap);
    String textWithTokensReplaced = replaceTokensWithValuesFromMap(rawText,
        tokensMapWithEncodedUrls);
    if (hasTokens(textWithTokensReplaced)) {
      return replaceTokensWithValuesFromMap(textWithTokensReplaced, tokensMapWithEncodedUrls);
    }
    return textWithTokensReplaced;
  }

  static Map<String, String> encodeUrlsIn(Map<String, String> tokensMap) {
    return tokensMap.entrySet().stream()
        .map(entry ->
            isAnUrlToken(entry)
                ? mapValue(entry, TokenizedTextConverter::escapeUrl)
                : entry)
        .collect(toMap());
  }

  private static String escapeUrl(String url) {
    return urlFragmentEscaper().escape(url);
  }

  private static boolean isAnUrlToken(Entry<String, String> entry) {
    return TOKEN_URL_SUFFIXES.stream()
        .anyMatch(suffix -> entry.getKey().endsWith(suffix));
  }


  public static boolean hasTokens(String rawText) {
    return !extractTokensNames(rawText).isEmpty();
  }

  public static boolean withoutTokens(String rawText) {
    return !hasTokens(rawText);
  }

  public static String firstTokenName(String rawText) {
    return extractTokensNames(rawText)
        .stream()
        .findFirst()
        .map(Token::getName)
        .orElse(null);
  }

  private static String replaceTokensWithValuesFromMap(String rawText,
      Map<String, String> tokensMap) {
    List<Token> tokens = extractTokensNames(rawText);
    return StringUtils
        .replaceEachRepeatedly(rawText,
            tokens.stream().map(Token::getNameWrappedWithDelimiters).toArray(String[]::new),
            tokens.stream().map(Token::getName).map(tokensMap::get).toArray(String[]::new));
  }

  private static List<Token> extractTokensNames(String rawText) {
    Stream<Token> tokens = extractTokensNames(rawText, Token.DELIMITERS_PLAINTEXT);
    Stream<Token> tokensEncoded = extractTokensNames(rawText, Token.DELIMITERS_ENCODED);
    Stream<Token> tokensSpecial = extractTokensNames(rawText, Token.DELIMITERS_SPECIAL);
    return concat(concat(tokens, tokensEncoded), tokensSpecial).collect(toList());
  }

  private static Stream<Token> extractTokensNames(String rawText, Token.Delimiters delimiters) {
    String[] tokenNames = StringUtils
        .substringsBetween(rawText, delimiters.getStartSymbol(), delimiters.getEndSymbol());
    return Optional.ofNullable(tokenNames)
        .map(Arrays::stream)
        .orElseGet(Stream::empty)
        .map(name -> new Token(name, delimiters));
  }
}
