/*
 * Music search
 * This service helps in finding music
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import org.openapitools.client.model.CommentAcceptRequest;
import org.openapitools.client.model.CommentAddRequest;
import org.openapitools.client.model.CommentDeclineRequest;
import org.openapitools.client.model.CompositionCreateRequest;
import org.openapitools.client.model.CompositionDebug;
import org.openapitools.client.model.CompositionReadRequest;
import org.openapitools.client.model.CompositionRequestDebug;
import org.openapitools.client.model.CompositionSearchFilter;
import org.openapitools.client.model.CompositionSearchRequest;
import org.openapitools.client.model.CompositionSearchRequestAllOf;
import org.openapitools.client.model.IRequest;

/**
 * CompositionSearchRequest
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-11-25T23:38:52.614487+05:00[Asia/Tashkent]")
public class CompositionSearchRequest extends IRequest {
  public static final String SERIALIZED_NAME_DEBUG = "debug";
  @SerializedName(SERIALIZED_NAME_DEBUG)
  private CompositionDebug debug;

  public static final String SERIALIZED_NAME_FILTER = "filter";
  @SerializedName(SERIALIZED_NAME_FILTER)
  private CompositionSearchFilter filter;

  public CompositionSearchRequest() {
    this.requestType = this.getClass().getSimpleName();
  }

  public CompositionSearchRequest debug(CompositionDebug debug) {
    
    this.debug = debug;
    return this;
  }

   /**
   * Get debug
   * @return debug
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public CompositionDebug getDebug() {
    return debug;
  }


  public void setDebug(CompositionDebug debug) {
    this.debug = debug;
  }


  public CompositionSearchRequest filter(CompositionSearchFilter filter) {
    
    this.filter = filter;
    return this;
  }

   /**
   * Get filter
   * @return filter
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public CompositionSearchFilter getFilter() {
    return filter;
  }


  public void setFilter(CompositionSearchFilter filter) {
    this.filter = filter;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompositionSearchRequest compositionSearchRequest = (CompositionSearchRequest) o;
    return Objects.equals(this.debug, compositionSearchRequest.debug) &&
        Objects.equals(this.filter, compositionSearchRequest.filter) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(debug, filter, super.hashCode());
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompositionSearchRequest {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    debug: ").append(toIndentedString(debug)).append("\n");
    sb.append("    filter: ").append(toIndentedString(filter)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
