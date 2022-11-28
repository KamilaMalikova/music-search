# CompositionApi

All URIs are relative to *https://localhost/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**commentAccept**](CompositionApi.md#commentAccept) | **POST** /composition/comment/accept | Accepts comment
[**commentAdd**](CompositionApi.md#commentAdd) | **POST** /composition/comment | Adds comment to composition discussion
[**commentDecline**](CompositionApi.md#commentDecline) | **POST** /composition/comment/decline | Declines comment
[**compositionCreate**](CompositionApi.md#compositionCreate) | **POST** /composition/create | Creates a music composition and creates a new discussion
[**compositionDiscussionRead**](CompositionApi.md#compositionDiscussionRead) | **POST** /composition/discussion | Reads the music composition discussion
[**musicView**](CompositionApi.md#musicView) | **POST** /composition/search | Searches a music composition


<a name="commentAccept"></a>
# **commentAccept**
> CommentAcceptResponse commentAccept(commentAcceptRequest)

Accepts comment

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CompositionApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://localhost/v1");

    CompositionApi apiInstance = new CompositionApi(defaultClient);
    CommentAcceptRequest commentAcceptRequest = new CommentAcceptRequest(); // CommentAcceptRequest | Request body
    try {
      CommentAcceptResponse result = apiInstance.commentAccept(commentAcceptRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CompositionApi#commentAccept");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **commentAcceptRequest** | [**CommentAcceptRequest**](CommentAcceptRequest.md)| Request body |

### Return type

[**CommentAcceptResponse**](CommentAcceptResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Response body |  -  |

<a name="commentAdd"></a>
# **commentAdd**
> CommentAddResponse commentAdd(commentAddRequest)

Adds comment to composition discussion

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CompositionApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://localhost/v1");

    CompositionApi apiInstance = new CompositionApi(defaultClient);
    CommentAddRequest commentAddRequest = new CommentAddRequest(); // CommentAddRequest | Request body
    try {
      CommentAddResponse result = apiInstance.commentAdd(commentAddRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CompositionApi#commentAdd");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **commentAddRequest** | [**CommentAddRequest**](CommentAddRequest.md)| Request body |

### Return type

[**CommentAddResponse**](CommentAddResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Response body |  -  |

<a name="commentDecline"></a>
# **commentDecline**
> CommentDeclineResponse commentDecline(commentDeclineRequest)

Declines comment

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CompositionApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://localhost/v1");

    CompositionApi apiInstance = new CompositionApi(defaultClient);
    CommentDeclineRequest commentDeclineRequest = new CommentDeclineRequest(); // CommentDeclineRequest | Request body
    try {
      CommentDeclineResponse result = apiInstance.commentDecline(commentDeclineRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CompositionApi#commentDecline");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **commentDeclineRequest** | [**CommentDeclineRequest**](CommentDeclineRequest.md)| Request body |

### Return type

[**CommentDeclineResponse**](CommentDeclineResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Response body |  -  |

<a name="compositionCreate"></a>
# **compositionCreate**
> CompositionCreateResponse compositionCreate(compositionCreateRequest)

Creates a music composition and creates a new discussion

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CompositionApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://localhost/v1");

    CompositionApi apiInstance = new CompositionApi(defaultClient);
    CompositionCreateRequest compositionCreateRequest = new CompositionCreateRequest(); // CompositionCreateRequest | Request body
    try {
      CompositionCreateResponse result = apiInstance.compositionCreate(compositionCreateRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CompositionApi#compositionCreate");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **compositionCreateRequest** | [**CompositionCreateRequest**](CompositionCreateRequest.md)| Request body |

### Return type

[**CompositionCreateResponse**](CompositionCreateResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Response body |  -  |

<a name="compositionDiscussionRead"></a>
# **compositionDiscussionRead**
> CompositionReadResponse compositionDiscussionRead(compositionReadRequest)

Reads the music composition discussion

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CompositionApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://localhost/v1");

    CompositionApi apiInstance = new CompositionApi(defaultClient);
    CompositionReadRequest compositionReadRequest = new CompositionReadRequest(); // CompositionReadRequest | Request body
    try {
      CompositionReadResponse result = apiInstance.compositionDiscussionRead(compositionReadRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CompositionApi#compositionDiscussionRead");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **compositionReadRequest** | [**CompositionReadRequest**](CompositionReadRequest.md)| Request body |

### Return type

[**CompositionReadResponse**](CompositionReadResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Response body |  -  |

<a name="musicView"></a>
# **musicView**
> CompositionSearchResponse musicView(compositionSearchRequest)

Searches a music composition

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CompositionApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://localhost/v1");

    CompositionApi apiInstance = new CompositionApi(defaultClient);
    CompositionSearchRequest compositionSearchRequest = new CompositionSearchRequest(); // CompositionSearchRequest | Request body
    try {
      CompositionSearchResponse result = apiInstance.musicView(compositionSearchRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CompositionApi#musicView");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **compositionSearchRequest** | [**CompositionSearchRequest**](CompositionSearchRequest.md)| Request body |

### Return type

[**CompositionSearchResponse**](CompositionSearchResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Response body |  -  |

