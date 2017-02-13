## RestFB Messenger

[![Gitter](https://badges.gitter.im/restfbmessenger/restfbmessenger.svg)](https://gitter.im/restfbmessenger/restfbmessenger?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marsbits.restfbmessenger/restfbmessenger-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marsbits.restfbmessenger/restfbmessenger-core)
[![Javadocs](http://www.javadoc.io/badge/com.github.marsbits.restfbmessenger/restfbmessenger-core.svg?color=brightgreen)](http://www.javadoc.io/doc/com.github.marsbits.restfbmessenger/restfbmessenger-core)
[![Apache 2.0 License](https://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

RestFB Messenger is a Java library for implementing chatbots on the
[Facebook Messenger Platform][].

It utilizes the [RestFB][] client for the low level communication with
the Facebook Graph API. It also uses the RestFB Java types for input
arguments and return types.

The RestFB Messenger library does basically all the low level plumbing
and provides the user a high level API to implement the chatbot.

The user has to implement the `com.github.marsbits.restfbmessenger.webhook.CallbackHandler`
interface or extending the convenient `com.github.marsbits.restfbmessenger.webhook.AbstractCallbackHandler`
like below:

```java
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
  <version>1.2.0</version>
</dependency>
```

_Gradle_:

```groovy
dependencies {
    compile "com.github.marsbits.restfbmessenger:restfbmessenger-core:1.2.0"
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

In a Servlet 3 environment the `WebhookServlet` can also be configured
programmatically like:

```java
@WebListener
public class EchoInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        Messenger messenger = new EchoMessengerProvider().getMessenger();
        WebhookServlet webhookServlet = new WebhookServlet(messenger);
        ServletRegistration.Dynamic webhook =
                event.getServletContext().addServlet("WebhookServlet", webhookServlet);
        webhook.setLoadOnStartup(1);
        webhook.addMapping("/webhook");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
```

Another option is to extend the `com.github.marsbits.restfbmessenger.webhook.WebhookServlet`
and annotate it with the `@WebServlet` annotation.

See the [RestFB Messenger Echo Servlet 3][] sample for a full sample
using a `@WebListener` to do the configuration programmatically.

### Spring

When using Spring one option is to simply configure the `WebhookServlet`
like the previous examples.

In a Spring environment the `Messenger` bean will typically be created
manually in the Spring application context and use other injected beans
as well.

In that case the `MessengerProvider` implementation must find a way to
retrieve the `Messenger` instance from the Spring application context.

Another option is to create a custom controller and use that instead of
the `WebhookServlet`. This custom controller should then listen to Facebook
callbacks and act on them appropriately.

This could look like:

```java
@Controller
@RequestMapping("/webhook")
public class EchoWebhookController {

    private static final Logger logger = Logger.getLogger(EchoWebhookController.class.getName());

    @Autowired
    private Messenger messenger;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> get(
            @RequestParam(HUB_MODE_PARAM_NAME) String mode,
            @RequestParam(HUB_VERIFY_TOKEN_PARAM_NAME) String verifyToken,
            @RequestParam(HUB_CHALLENGE_PARAM_NAME) String challenge) {
        logger.info("Validating webhook...");
        if (HUB_MODE_SUBSCRIBE_VALUE.equals(mode) && messenger.verifyToken(verifyToken)) {
            logger.info("Validating webhook succeeded");
            return new ResponseEntity<>(challenge, HttpStatus.OK);
        } else {
            logger.warning("Validating webhook failed");
            return new ResponseEntity<>("Failed validation. Make sure the validation tokens match.",
                    HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public void post(
            @RequestHeader(HUB_SIGNATURE_HEADER_NAME) String signature,
            @RequestBody String payload) {
        logger.info("Webhook received");
        try {
            messenger.handleCallback(payload, signature);
        } catch (Exception e) {
            logger.severe(format("Exception handling webhook: %s", e.getMessage()));
            throw e;
        }
    }
}
```

See the [RestFB Messenger Echo Spring][] sample for a full sample
using a custom Spring controller.

### <a name="springboot"></a>Spring Boot

Add the `restfbmessenger-spring-boot-starter` dependency to your project:

_Maven_:

```xml
<dependency>
  <groupId>com.github.marsbits.restfbmessenger</groupId>
  <artifactId>restfbmessenger-spring-boot-starter</artifactId>
  <version>1.2.0</version>
</dependency>
```

_Gradle_:

```groovy
dependencies {
    compile "com.github.marsbits.restfbmessenger:restfbmessenger-spring-boot-starter:1.2.0"
}
```

The `restfbmessenger-spring-boot-starter` will automatically add the
`restfbmessenger-core` dependency and trigger the auto configuration to
create the `com.github.marsbits.restfbmessenger.Messenger` instance and
register the `com.github.marsbits.restfbmessenger.webhook.WebhookServlet`
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
  api-version: v2.8 # The Facebook api version
  webhook:
    enabled: true # Enable the webhook servlet
    path: /webhook # The path of the webhook servlet
```

See the [RestFB Messenger Echo Spring Boot][] sample for a full sample
using the RestFB Messenger Spring Boot Starter.

## Showcases

Here are some projects that use RestFB Messenger to power its Facebok Messenger chatbot.

* [Chuck Norris IO](https://m.me/chucknorris.io) (source code available [here](https://github.com/chucknorris-io/app-facebook-messenger))
* [Tronald Dump IO](https://m.me/tronalddumpio) (source code available [here](https://github.com/tronalddump-io/app-facebook-messenger))

Create a [New Issue][] or [New Pull Request][] to add your own Facebook Messenger chatbot to the list.

## License

The RestFB Messenger library is released under version 2.0 of the [Apache License][].


[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
[Facebook Messenger Platform]: https://developers.facebook.com/docs/messenger-platform
[New Issue]: https://github.com/marsbits/restfbmessenger/issues/new
[New Pull Request]: https://github.com/marsbits/restfbmessenger/compare
[RestFB]: http://restfb.com
[RestFB Messenger Echo App Engine]: https://github.com/marsbits/restfbmessenger/tree/master/samples/restfbmessenger-echo-appengine
[RestFB Messenger Echo Servlet 3]: https://github.com/marsbits/restfbmessenger/tree/master/samples/restfbmessenger-echo-servlet3
[RestFB Messenger Echo Spring]: https://github.com/marsbits/restfbmessenger/tree/master/samples/restfbmessenger-echo-spring
[RestFB Messenger Echo Spring Boot]: https://github.com/marsbits/restfbmessenger/tree/master/samples/restfbmessenger-echo-spring-boot
