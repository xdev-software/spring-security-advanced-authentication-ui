[![Latest version](https://img.shields.io/maven-central/v/software.xdev/spring-security-advanced-authentication-ui?logo=apache%20maven)](https://mvnrepository.com/artifact/software.xdev/spring-security-advanced-authentication-ui)
[![Build](https://img.shields.io/github/actions/workflow/status/xdev-software/spring-security-advanced-authentication-ui/check-build.yml?branch=develop)](https://github.com/xdev-software/spring-security-advanced-authentication-ui/actions/workflows/check-build.yml?query=branch%3Adevelop)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=xdev-software_spring-security-advanced-authentication-ui&metric=alert_status)](https://sonarcloud.io/dashboard?id=xdev-software_spring-security-advanced-authentication-ui)

# spring-security-advanced-authentication-ui

Modernizes the default Spring Web Authentication/Login UI and makes it easier customizable.

<details><summary>Show demo</summary>

<p align="center">
<img src="./assets/demo.png" alt="Demo" />
</p>

</details>

## Usage

The library provides an adapter that can be used like this:
```java
public SecurityFilterChain configure(final HttpSecurity http) throws Exception
{
    // Changing the text "Login with" to "Sign in with"
    http.with(new AdvancedLoginPageAdapter<>(http), c -> c
            .customizeLoginPage(p -> p.ssoLoginHeaderText("Sign in with")))
        .oauth2Login(c -> 
            // ...
        )
    // ...
}
```

A more detailed scenario is available in the [demo](./spring-security-advanced-authentication-ui-demo/).

> [!NOTE]
> By default [Bootstrap](https://github.com/twbs/bootstrap) is loaded from ``cdn.jsdelivr.net``.<br/>
> Due to privacy and stability reasons you should ship your own version!<br/>
> An example how this can be done is shown in the [demo](https://github.com/xdev-software/spring-security-advanced-authentication-ui/blob/4117d471e036de4dc2a58b2b484f2631afe7af50/spring-security-advanced-authentication-ui-demo/src/main/java/software/xdev/security/MainWebSecurity.java#L44-L51).

> [!NOTE]
> The ``Advanced``-subsystem uses the pre-``Spring Security 6.4`` / ``Spring Boot 3.4`` templating system (without Regex).<br/>
> * In contrast to Spring's new Regex based system not all values are escaped by default
>   * Usually they don't need to be escaped in the first place as they are set on the server side and can't be modified by a user
> * This is A LOT FASTER (in tests around 50x) than Spring's new Regex based system

## Installation
[Installation guide for the latest release](https://github.com/xdev-software/spring-security-advanced-authentication-ui/releases/latest#Installation)

> [!NOTE]  
> To minimize the risk of dependency conflicts all Spring (Boot) dependencies are declared as provided and are not shipped by default.

## Support
If you need support as soon as possible and you can't wait for any pull request, feel free to use [our support](https://xdev.software/en/services/support).

## Contributing
See the [contributing guide](./CONTRIBUTING.md) for detailed instructions on how to get started with our project.

## Dependencies and Licenses
View the [license of the current project](LICENSE) or the [summary including all dependencies](https://xdev-software.github.io/spring-security-advanced-authentication-ui/dependencies)
