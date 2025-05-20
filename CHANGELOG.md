# 2.0.1
* Migrated deployment to _Sonatype Maven Central Portal_ [#155](https://github.com/xdev-software/standard-maven-template/issues/155)
* Updated dependencies

# 2.0.0
* Added support for Spring Security 6.4+ / Spring Boot 3.4+ #100
  * Spring now
    * uses a Regex-based templating system
    * no longer uses bootstrap
    * provides One-Time token/OTT and Passkey logins
* Changes to ``Extendable``-subsystem
  * Now uses the new Regex-based templating system
  * Correct a bunch of problems in Spring Security including
    * One-Time token/OTT and Passkeys are ignored when computing if the whole filter is enabled
    * [Passkeys] Removed invalid XML comment in scripts block
    * [Passkeys] Fixed incorrectly closed HTML-form/div-tag
    * [HtmlTemplating] Compile ``UNUSED_PLACEHOLDER_PATTERN`` regex once and not for each request
    * [HtmlTemplating] Render: Optimization: Use entrySet instead of keySet + getValue
    * Add correct setter for ``generateOneTimeTokenUrl``
    * Improved naming of methods
* Changes to ``Advanced``-subsystem
  * Keeps using Bootstrap
    * By default bootstrap is still loaded from ``cdn.jsdelivr.net`` but you can (and should) provide your own version
  * Keeps using the old templating system (without Regex)
    * Not all values are escaped by default as is with Spring's Regex based system
      * Usually they don't need to be escaped in the first place as they are set on the server side and can't be modified by a user
    * This is A LOT FASTER (in tests around 50x) than Spring's new Regex based system
  * Adopted changes; Added new configuration options
  * [Passkeys] Fixed a problem where more than one header results in invalid generated JavaScript code

# 1.0.3
* Updated dependencies
* Abstracted code

# 1.0.2
* Fix incorrect styling of ``main`` element on login screen

# 1.0.1
* Fix NPE when ``additionalStylingData`` is not set

# 1.0.0
<i>Initial release</i>
* All methods and functionality are designed to be overwritable (at least protected)
* The library consists of 2 main parts:
  * ``extendable`` → Includes the bare minimum to make the Spring components extendable
  * ``advanced`` → Contains components made on top of ``extendable`` with many customization options; Additionally:
    * Updated Bootstrap to version 5.3+
    * Improved SSO (OAuth2 / SAML2) UI
