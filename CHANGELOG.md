##  RestFB Messenger Changelog

### 2.2.0 (TBA)

* Added required `messaging_type` property (https://developers.facebook.com/docs/messenger-platform/send-messages#messaging_types) to all send operations.
* Added support for the open graph template
* Added support for additional webhook callbacks:
  * Added support for policy enforcement
  * Added support for app roles
  * Added support for take thread control
  * Added support for pass thread control  
* Updated the available message tags (https://developers.facebook.com/docs/messenger-platform/send-messages/message-tags)
* Upgraded RestFB dependency to latest 1.48.0
* Upgraded Spring Boot Starter to latest Spring Boot 1.5.8
* Updated default Facebook API version to 2.11

### 1.3.0 (2017-04-27)

* Added support for Message Tags (https://developers.facebook.com/docs/messenger-platform/send-api-reference/tags)
  * Added support for adding a tag to messages (thanks @ryacobi for the pull request)
  * Added support for retrieving the supported list of message tags 
* Upgraded RestFB dependency to latest 1.40.1
* Upgraded Spring Boot Starter to latest Spring Boot 1.5.3


### 1.2.0 (2017-02-13)

* Upgraded RestFB dependency to latest 1.36.0
  * Fix CallToAction object (https://github.com/restfb/restfb/issues/637)
* Upgraded Spring Boot Starter to latest Spring Boot 1.5.1
* Updated license header to 2017

### 1.1.0 (2016-11-20)

* Aligned with Messenger Platform 1.3 release
  * Added support for the new list template
  * Added support for the new referral callback
* Added support for changing thread settings
* Added support for push notification type
* Added Messenger#getUserProfile method with varargs fields argument
* Added Objects#requireNonNull check for required arguments
* Upgraded RestFB dependency to latest 1.34.0
* Updated default Facebook API version to 2.8 

### 1.0.1 (2016-10-23)

* Upgraded RestFB dependency to latest 1.32.0

### 1.0.0 (2016-10-06)

* Initial release
  * RestFB Messenger Core module
  * RestFB Messenger Spring Boot Starter module
