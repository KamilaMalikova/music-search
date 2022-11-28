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
import java.util.ArrayList;
import java.util.List;
import org.openapitools.client.model.CompositionInfo;
import org.openapitools.client.model.CompositionInfoResponseMultiAllOf;

/**
 * CompositionInfoResponseMulti
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-11-25T23:38:52.614487+05:00[Asia/Tashkent]")
public class CompositionInfoResponseMulti {
  public static final String SERIALIZED_NAME_COMPOSITIONS = "compositions";
  @SerializedName(SERIALIZED_NAME_COMPOSITIONS)
  private List<CompositionInfo> compositions = null;


  public CompositionInfoResponseMulti compositions(List<CompositionInfo> compositions) {
    
    this.compositions = compositions;
    return this;
  }

  public CompositionInfoResponseMulti addCompositionsItem(CompositionInfo compositionsItem) {
    if (this.compositions == null) {
      this.compositions = new ArrayList<CompositionInfo>();
    }
    this.compositions.add(compositionsItem);
    return this;
  }

   /**
   * Get compositions
   * @return compositions
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<CompositionInfo> getCompositions() {
    return compositions;
  }


  public void setCompositions(List<CompositionInfo> compositions) {
    this.compositions = compositions;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompositionInfoResponseMulti compositionInfoResponseMulti = (CompositionInfoResponseMulti) o;
    return Objects.equals(this.compositions, compositionInfoResponseMulti.compositions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(compositions);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompositionInfoResponseMulti {\n");
    sb.append("    compositions: ").append(toIndentedString(compositions)).append("\n");
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
