## RestFB Messenger

RestFB Messenger is a Java library for implementing chatbots on the 
[Facebook Messenger Platform][].

It utilizes the [RestFB][] client for the low level communication with 
the Facebook Graph API. It also uses the RestFB Java types for input 
arguments and return types.

The RestFB Messenger library does basically all the low level plumbing 
and provides the user a high level API to implement the chatbot.

The user only has to implement the `com.github.marsbits.restfbmessenger.webhook.CallbackHandler` 
interface or extending the convenient `com.github.marsbits.restfbmessenger.webhook.AbstractCallbackHandler` 
like below:

```java
@Component
public class MyCallbackHandler extends AbstractCallbackHandler {

    @Override
    public void onMessage(Messenger messenger, MessagingItem messaging) {
        String senderId = messaging.getSender().getId();
        IdMessageRecipient recipient = new IdMessageRecipient(senderId);
        MessageItem message = messaging.getMessage();
        messenger.send().markSeen(recipient);
        messenger.send().typingOn(recipient);
        messenger.send().textMessage(recipient, 
            format("Hi human, I received your message: %s", message.getText()));
        messenger.send().typingOff(recipient);
    }
    
    @Override
    public void onPostback(Messenger messenger, MessagingItem messaging) {
        // TODO implement..
    }
    
    // TODO override other methods the chatbot need to repsond to..
}
```

## Installation

Add the `restfbmessenger-core` dependency to your project:

_Maven_:

```xml
<dependency>
  <groupId>com.github.marsbits.restfbmessenger</groupId>
  <artifactId>restfbmessenger-core</artifactId>
  <version>1.1.0-SNAPSHOT</version>
</dependency>
```

_Gradle_:

```groovy
dependencies {
    compile "com.github.marsbits.restfbmessenger:restfbmessenger-core:1.0.0-SNAPSHOT"
}
```

If you want to use RestFB Messenger in a Spring Boot application see the 
[Spring Boot](#springboot) configuration instead. 

## Configuration

### Servlet 2.5/3 web.xml

In a standard webapp the `WebhookServlet` can be configured in the `web.xml` like:

```xml
<servlet>
    <servlet-name>webhook</servlet-name>
    <servlet-class>com.github.marsbits.restfbmessenger.webhook.WebhookServlet</servlet-class>
    <init-param>
        <param-name>messengerProviderClass</param-name>
        <param-value>[fully-qualified class name of the MessengerProvider]</param-value>
    </init-param>
</servlet>

<servlet-mapping>
    <servlet-name>webhook</servlet-name>
    <url-pattern>/webhook</url-pattern>
</servlet-mapping>
```

The `messengerProviderClass` init param must point to a custom 
class implementing the `com.github.marsbits.restfbmessenger.MessengerProvider`
to provide the `Messenger` instance to the `WebhookServlet`.

See the [RestFB Messenger Echo App Engine][] sample for a full sample 
using the `web.xml` configuration. 

### Servlet 3 @WebListener

..

### Spring

..

### <a name="springboot"></a>Spring Boot

Add the `restfbmessenger-spring-boot-starter` dependency to your project:

_Maven_:

```xml
<dependency>
  <groupId>com.github.marsbits.restfbmessenger</groupId>
  <artifactId>restfbmessenger-spring-boot-starter</artifactId>
  <version>1.1.0-SNAPSHOT</version>
</dependency>
```

_Gradle_:

```groovy
dependencies {
    compile "com.github.marsbits.restfbmessenger:restfbmessenger-spring-boot-starter:1.0.0-SNAPSHOT"
}
```

The `restfbmessenger-spring-boot-starter` will automatically trigger 
auto configuration to create the `com.github.marsbits.restfbmessenger.Messenger` 
instance and register the `com.github.marsbits.restfbmessenger.webhook.WebhookServlet` 
servlet.

Only a class implementing the `CallbackHandler` need to be added to the 
Spring context like:

```java
@Component
public class MyCallbackHandler extends AbstractCallbackHandler {

    @Override
    public void onMessage(Messenger messenger, MessagingItem messaging) {
        // TODO implement..
    }
```

The auto configuration will automatically hook the `CallbackHandler` in 
the `Messenger` instance.

The following properties can be specified inside your 
`application.properties`/`application.yml` file:

```txt
restfbmessenger:
  enabled: true # Enable RestFB Messenger
  verify-token: # The Facebook verify token (required the verify the webhook)
  access-token: # The Facebook access token (required to send messages)
  app-secret: # The Facebook app secret (if not provided the payload signature will not be validated; useful in e.g. dev environment)
  api-version: v2.7 # The Facebook api version
  webhook:
    enabled: true # Enable the webhook servlet
    path: /webhook # The path of the webhook servlet 
```

## License

The RestFB Messenger library is released under version 2.0 of the [Apache License][].


[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
[Facebook Messenger Platform]: https://developers.facebook.com/docs/messenger-platform
[RestFB]: http://restfb.com
[RestFB Messenger Echo App Engine]: https://github.com/marsbits/restfbmessenger/tree/master/samples/restfbmessenger-echo-appengine
