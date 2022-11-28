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
import org.openapitools.client.model.DiscussionStatus;

/**
 * Set of search filters
 */
@ApiModel(description = "Set of search filters")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-11-25T23:38:52.614487+05:00[Asia/Tashkent]")
public class CompositionSearchFilter {
  public static final String SERIALIZED_NAME_DISCUSSION_STATUS = "discussionStatus";
  @SerializedName(SERIALIZED_NAME_DISCUSSION_STATUS)
  private DiscussionStatus discussionStatus;

  public static final String SERIALIZED_NAME_OWNER = "owner";
  @SerializedName(SERIALIZED_NAME_OWNER)
  private String owner;


  public CompositionSearchFilter discussionStatus(DiscussionStatus discussionStatus) {
    
    this.discussionStatus = discussionStatus;
    return this;
  }

   /**
   * Get discussionStatus
   * @return discussionStatus
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public DiscussionStatus getDiscussionStatus() {
    return discussionStatus;
  }


  public void setDiscussionStatus(DiscussionStatus discussionStatus) {
    this.discussionStatus = discussionStatus;
  }


  public CompositionSearchFilter owner(String owner) {
    
    this.owner = owner;
    return this;
  }

   /**
   * User id
   * @return owner
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "User id")

  public String getOwner() {
    return owner;
  }


  public void setOwner(String owner) {
    this.owner = owner;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompositionSearchFilter compositionSearchFilter = (CompositionSearchFilter) o;
    return Objects.equals(this.discussionStatus, compositionSearchFilter.discussionStatus) &&
        Objects.equals(this.owner, compositionSearchFilter.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(discussionStatus, owner);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompositionSearchFilter {\n");
    sb.append("    discussionStatus: ").append(toIndentedString(discussionStatus)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
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
