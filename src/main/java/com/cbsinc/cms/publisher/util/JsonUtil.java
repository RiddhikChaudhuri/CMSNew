package com.cbsinc.cms.publisher.util;

import com.cbsinc.cms.dto.NameItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonUtil {


  private StringBuffer buf = new StringBuffer();
  public static final String CURL_BEGIN = "{";
  public static final String CURL_END = "}";
  public static final String SQUARE_BEGIN = "[";
  public static final String SQUARE_END = "]";
  public static final String COLON = ":";
  public static final String COMMA = ",";
  public static final String QUOTES = "\"";
  public static final String NEW_LINE = System.getProperty("line.separator");
  private int pairCount = 0;

  /**
   * Begin the JSON Doc
   * 
   * @param value
   */
  public void beginDoc(String value) {
    buf.append(CURL_BEGIN).append("\"").append(value).append(QUOTES).toString();
  }

  /**
   * Add Object Attribute
   * 
   * @param key
   * @param value
   */
  public void addPair(String key, String value) {
    if (pairCount > 0) {
      buf.append(COMMA).append(NEW_LINE);
    } else {
      buf.append(COLON).append(CURL_BEGIN).append(NEW_LINE);
    }
    buf.append(QUOTES).append(key).append(QUOTES).append(COLON).append(QUOTES).append(value)
        .append(QUOTES);
    pairCount++;

  }
  
  /**
   * Add array to the JSON doc
   * 
   * @param name
   * @param values
   * @throws JsonProcessingException 
   */
  public void addArray(String name, NameItem values[]) throws JsonProcessingException {
      StringBuffer ar = new StringBuffer();
      ObjectWriter ow = new ObjectMapper().writer();
      for (int i = 0; i < values.length; i++) {
          // append comma
          if (i > 0) {
              ar.append(COMMA);
          }
          ar.append(QUOTES).append(ow.writeValueAsString(values[i])).append(QUOTES);
      }

      if (pairCount > 0) {
          buf.append(COMMA).append(NEW_LINE);
      } else {
          buf.append(COLON).append(CURL_BEGIN).append(NEW_LINE);
      }

      // add it as pair
      buf.append(QUOTES).append(name).append(QUOTES).append(COLON);
      buf.append(SQUARE_BEGIN);
      buf.append(ar.toString());
      buf.append(SQUARE_END);
  }
  
  /**
   * End JSON doc
   */
  public void endDoc() {
      buf.append(NEW_LINE);
      if (pairCount > 0) {
          buf.append(CURL_END);
      }
      buf.append(CURL_END);
  }

  public String toJSON() {
      return buf.toString();
  }
  
  public void cleanBuffer() {
    buf.setLength(0);
  }
}
