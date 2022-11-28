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
import org.openapitools.client.model.CompositionInfo;
import org.openapitools.client.model.CompositionInfoResponseSingleAllOf;

/**
 * CompositionInfoResponseSingle
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-11-25T23:38:52.614487+05:00[Asia/Tashkent]")
public class CompositionInfoResponseSingle {
  public static final String SERIALIZED_NAME_COMPOSITION_INFO = "composition-info";
  @SerializedName(SERIALIZED_NAME_COMPOSITION_INFO)
  private CompositionInfo compositionInfo;


  public CompositionInfoResponseSingle compositionInfo(CompositionInfo compositionInfo) {
    
    this.compositionInfo = compositionInfo;
    return this;
  }

   /**
   * Get compositionInfo
   * @return compositionInfo
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public CompositionInfo getCompositionInfo() {
    return compositionInfo;
  }


  public void setCompositionInfo(CompositionInfo compositionInfo) {
    this.compositionInfo = compositionInfo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompositionInfoResponseSingle compositionInfoResponseSingle = (CompositionInfoResponseSingle) o;
    return Objects.equals(this.compositionInfo, compositionInfoResponseSingle.compositionInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(compositionInfo);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompositionInfoResponseSingle {\n");
    sb.append("    compositionInfo: ").append(toIndentedString(compositionInfo)).append("\n");
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
